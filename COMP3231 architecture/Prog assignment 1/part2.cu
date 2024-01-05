#include <iostream>
#include <string>
#include <fstream>
#include <chrono>

#include <stdio.h>
#include <stdlib.h>

using namespace std;

//We may change this value!!
const int FILTER_WIDTH = 7;
const int BLOCK_SIZE = 512 ;


// We may change this value!!!
int FILTER[FILTER_WIDTH*FILTER_WIDTH] = {
	1,4,7,10,7,4,1,
	4,12,26,33,26,12,4,
	7,26,55,71,55,26,7,
	10,33,71,91,71,33,10,
	7,26,55,71,55,26,7,
	4,12,26,33,26,12,4,
	1,4,7,10,7,4,1
};

// Display the first and last 10 items
// For debug only
void displayResult(const int original[], const int result[], int size) {
	cout << "Display result: ";
	cout << "(original -> result)\n";

	for (int i = 0; i < 10; i++) {
		cout << original[i] << " -> " << result[i] << "\n";
	}
	cout << ".\n.\n.\n";

	for (int i = size - 10; i < size; i++) {
		cout << original[i] << " -> " << result[i] << "\n";
	}
}

void initColorData(string file, int **data, int *sizeX, int *sizeY) {
	int x;
	int y;
	long long i = 0;
	cout << "Reading "<< file << "... \n";
	ifstream myfile(file);
	if (myfile.is_open()) {
		myfile >> x;
		myfile >> y;

		int *temp = new int[x * y * 3];
		for( i=0; i < x * y * 3; i++){
			myfile >> temp[(int)i];
		}
		myfile.close();
		*data = temp;
		*sizeX = x;
		*sizeY = y;
	}
	else {
		cout << "ERROR: File " << file << " not found!\n";
		exit(0);
	}
	cout << i << " entries imported\n";
}

void saveResult(string file, int data[], int sizeX, int sizeY) {
	long long i = 0;
	cout << "Saving data to "<< file <<"... \n";
	ofstream myfile(file, std::ofstream::out);
	if (myfile.is_open()) {
		myfile << sizeX << "\n";
		myfile << sizeY << "\n";
		for (i = 0; i < sizeX * sizeY; i++){
			myfile << data[3* i] << " " << data[3* i + 1] << " " << data[3* i+ 2]<< "\n";
		}
		myfile.close();
	}
	else {
		cout << "ERROR: Cannot save to " << file << "!\n";
		exit(0);
	}
	cout << i << " entries saved\n";
}

// TODO: implement the kneral function for 2D smoothing 



__global__ void sharpen(int *data, int *result, int sizeX, int sizeY, int *FILTER){
    // use block index to locate the image pixel
    int image_pixel = threadIdx.x + blockIdx.x * blockDim.x;
    int x = image_pixel % sizeX;
    int y = image_pixel / sizeX;

    if (x < sizeX && y < sizeY){
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;

        for (int i = 0; i < FILTER_WIDTH; ++i) {
            for (int j = 0; j < FILTER_WIDTH; ++j) {
                // for each loop of filter, find the center of the filter
                int centerX = x - FILTER_WIDTH / 2 ;
                int centerY = y - FILTER_WIDTH / 2 ;

				if (centerX >= 0 && centerX < sizeX - 1) {
					centerX += j;
				}
				if (centerY >= 0 && centerY < sizeY - 1) {
					centerY += i;
				}
				int filter_value =  FILTER[i * FILTER_WIDTH + j];
                // add to the sum only if the filter center is within the image boundary
                if (centerX >= 0 && centerX < sizeX && centerY >= 0 && centerY < sizeY) {
                    sumR += data[(centerY * sizeX * 3) + centerX * 3] *	filter_value;
                    sumG += data[(centerY * sizeX * 3) + centerX * 3 + 1] * filter_value;
                    sumB += data[(centerY * sizeX * 3) + centerX * 3 + 2] * filter_value;
                }
            }
        }

        result [y * sizeX * 3 + x * 3 ] = sumR;
        result [y * sizeX * 3 + x * 3 + 1] = sumG;
        result [y * sizeX * 3 + x * 3 + 2] = sumB;
    }
}

void normalize_output(int result[], int sizeX, int sizeY){
	int size = sizeX * sizeY;
	int *scaled =  (int * ) malloc(sizeof(int)*size*3);
	for (int i=0; i<3; i++){
		int source_min = INT_MAX;
		int source_max = 0;
		for (int j=0; j<size; j++){
			if (result[j*3+i] < source_min)
            	source_min = result[j*3+i];
			if (result[j*3+i] > source_max)
				source_max = result[j*3+i];
		}
		int source_scale = source_max - source_min;
		for (int j=0; j<size; j++) {
			int zsrc = result[j*3+i] - source_min;

			scaled[j*3+i] = zsrc * 255 / source_scale;
    	}
	}
	memcpy(result, scaled, sizeof(int)*size*3);
}


// GPU implementation
void GPU_Test(int data[], int result[], int sizeX, int sizeY) {
	// input:
	//	int data[] - int array holding the flattened original image
	//	int sizeX - the width of the image
	//	int sizeY - the height of the image
	// output:
	//	int result[] - int array holding the image

	// each threads for a signal filter 
	// const int BLOCK_SIZE = 512 ;
	// each pixel on the image is a block
	const int n_blocks = ((sizeX*sizeY)/BLOCK_SIZE);
	const int data_size = sizeX * sizeY * sizeof(int) * 3;

	// TODO: allocate device memory and copy data onto the device

	// declare device copies 
	int *d_data;
	int *d_result;
	int *d_FILTER;

	// allocate space fro device copies of 
	cudaMalloc((void **) &d_result, data_size);
	cudaMalloc((void **) &d_data, data_size);
	cudaMalloc((void **) &d_FILTER, FILTER_WIDTH * FILTER_WIDTH * sizeof(int));

	// Copy data to device
	cudaMemcpy(d_data, data, data_size, cudaMemcpyHostToDevice);
	cudaMemcpy(d_result, result, data_size, cudaMemcpyHostToDevice);
	cudaMemcpy(d_FILTER, FILTER, FILTER_WIDTH * FILTER_WIDTH * sizeof(int), cudaMemcpyHostToDevice);

	// Start timer for kernel
	auto startKernel = chrono::steady_clock::now();

	// TODO: call the kernel function
	sharpen<<<n_blocks, BLOCK_SIZE>>>(d_data, d_result, sizeX, sizeY, d_FILTER);
	// End timer for kernel and display kernel time
	cudaDeviceSynchronize(); // <- DO NOT REMOVE
	auto endKernel = chrono::steady_clock::now();
	cout << "Kernel Elapsed time: " << chrono::duration <double, milli>(endKernel - startKernel).count() << "ms\n";

	// TODO: copy result from device to host
	cudaMemcpy(result, d_result, data_size, cudaMemcpyDeviceToHost);
	normalize_output(result, sizeX, sizeY);

	// TODO: free device memory <- important, keep your code clean
	cudaFree(d_data);
	cudaFree(d_result);
	cudaFree(d_FILTER);

}


// CPU implementation
void CPU_Test(int data[], int result[], int sizeX, int sizeY) {
	// input:
	//	int data[] - int array holding the flattened original image
	//	int sizeX - the width of the image
	//	int sizeY - the height of the image
	// output:
	//	int result[] - int array holding the image

	// TODO: smooth the image with filter size = FILTER_WIDTH
	//       apply zero padding for the border

	for (int y=0; y<sizeY; y++){
		for (int x=0; x<sizeX; x++){
			int sumR = 0;
			int sumG = 0;
			int sumB = 0;

			// for every cell, iterate over the filter
			 for (int i = 0; i < FILTER_WIDTH; ++i) {
                for (int j = 0; j < FILTER_WIDTH; ++j) {
                    // for each loop of filter, find the center of the filter
                    int centerX = x - FILTER_WIDTH / 2;
                    int centerY = y - FILTER_WIDTH / 2;

					if (centerX >= 0 && centerX < sizeX - 1) {
						centerX += j;
					}
					if (centerY >= 0 && centerY < sizeY - 1) {
						centerY += i;
					}

                    // add to the sum only if the filter center is within the image boundary, which is possible in reality
                    if (centerX >= 0 && centerX < sizeX && centerY >= 0 && centerY < sizeY) {
                        // sum += data[centerY * sizeX + centerX ] * FILTER[i * FILTER_WIDTH + j ];
						sumR += data[(centerY * sizeX *3 ) + centerX * 3 ] * FILTER[i * FILTER_WIDTH + j ];
						sumG += data[(centerY * sizeX *3 ) + centerX * 3 + 1] * FILTER[i * FILTER_WIDTH + j ];
						sumB += data[(centerY * sizeX *3 ) + centerX * 3 + 2] * FILTER[i * FILTER_WIDTH + j ];

                    }
                }
            }
			result [y * sizeX * 3 + x * 3 ] = sumR;
        	result [y * sizeX * 3 + x * 3 + 1] = sumG;
        	result [y * sizeX * 3 + x * 3 + 2] = sumB;
		}
	}
	normalize_output(result, sizeX, sizeY);
}

// The image is flattened into a text file of pixel values.
int main(int argc, char *argv[]) {
	string inputFile = (argc == 1) ? "image_color.txt" : argv[1];

	int sizeX;
	int sizeY;
	int *dataForCPUTest;
	int *dataForGPUTest;	

	initColorData(inputFile, &dataForCPUTest, &sizeX, &sizeY);
	initColorData(inputFile, &dataForGPUTest, &sizeX, &sizeY);

	int size = sizeX * sizeY * 3;
	int *resultForCPUTest = new int[size];
	int *resultForGPUTest = new int[size];

	cout << "\n";

	cout << "CPU Implementation\n";

	auto startCPU = chrono::steady_clock::now();
	CPU_Test(dataForCPUTest, resultForCPUTest, sizeX, sizeY);
	auto endCPU = chrono::steady_clock::now();

	cout << "Elapsed time: " << chrono::duration <double, milli>(endCPU - startCPU).count() << "ms\n";

	// displayResult(dataForCPUTest, resultForCPUTest, size);

	saveResult("color_result_CPU.txt",resultForCPUTest, sizeX, sizeY);

	cout << "\n";
	cout << "GPU Implementation\n";

	auto startGPU = chrono::steady_clock::now();
	GPU_Test(dataForGPUTest, resultForGPUTest, sizeX, sizeY);
	auto endGPU = chrono::steady_clock::now();

	cout << "Elapsed time: " << chrono::duration <double, milli>(endGPU - startGPU).count() << "ms\n";

	// displayResult(dataForGPUTest, resultForGPUTest, size);
	saveResult("color_result_GPU.txt",resultForGPUTest, sizeX, sizeY);

	return 0;
}
