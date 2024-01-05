#include <iostream>
#include <string>
#include <fstream>
#include <chrono>

#include <stdio.h>
#include <stdlib.h>

using namespace std;

const int FILTER_WIDTH = 3;

//We will only use this filter in part 1
int FILTER[FILTER_WIDTH*FILTER_WIDTH] = {
    0, -1, 0, 
    -1, 5, -1, 
    0, -1, 0
};

// Display the first and last 10 items
// For debug only
void displayResult(const int original[], const int result[], int size) {
	cout << "Display result: ";

	for (int i = 0; i < 10; i++) {
		cout << result[i] << "\n";
	}
	cout << ".\n.\n.\n";

	for (int i = size - 10; i < size; i++) {
		cout << result[i] << "\n";
	}
}

void initData(string file, int **data, int *sizeX, int *sizeY) {
	int x;
	int y;
	long long i = 0;
	cout << "Reading "<< file << "... \n";
	ifstream myfile(file);
	if (myfile.is_open()) {
		myfile >> x;
		myfile >> y;

		int *temp = new int[x * y];
		for( i=0; i < x * y; i++){
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

// Don't change this code
// We will evaluate your correctness based on the saved result, not printed output
void saveResult(string file, int data[], int sizeX, int sizeY) {
	long long i = 0;
	cout << "Saving data to "<< file <<"... \n";
	ofstream myfile(file, std::ofstream::out);
	if (myfile.is_open()) {
		myfile << sizeX << "\n";
		myfile << sizeY << "\n";
		for (i = 0; i < sizeX * sizeY; i++){
			myfile << data[i] << "\n";
		}
		myfile.close();
	}
	else {
		cout << "ERROR: Cannot save to " << file << "!\n";
		exit(0);
	}
	cout << i << " entries saved\n";
}

//TODO: Implement the kernel function

__global__ void sharpen(int *data, int *result, int *sizeX, int *sizeY, int *FILTER){
	// use block index to locate the image pixel
	int image_pixel = threadIdx.x + blockIdx.x * blockDim.x;;
	// use thread index to locate the filer pixel
	// int flter_pixel = blockIdx.x;
	int x = image_pixel % (*sizeX);
    int y = image_pixel / (*sizeX);

	if (x < (*sizeX) && y < (*sizeY)){
		int sum = 0;

		for (int i = 0; i < FILTER_WIDTH; ++i) {
			for (int j = 0; j < FILTER_WIDTH; ++j) {
				// for each loop of filter, find the center of the filter
				int centerX = x - FILTER_WIDTH / 2 + j;
				int centerY = y - FILTER_WIDTH / 2 + i;

				// add to the sum only if the filter center is within the image boundary, which is possible in reality
				if (centerX >= 0 && centerX < (*sizeX) && centerY >= 0 && centerY < (*sizeY)) {
					sum += data[centerY * (*sizeX) + centerX ] * FILTER[i * FILTER_WIDTH + j ];
				}
			}
		}
		result [y * (*sizeX) + x] = sum;
	}
}

// GPU implementation
void GPU_Test(int data[], int result[], int sizeX, int sizeY) {
	// input:
	//	int data[] - int array holding the flattened original image
	//	int sizeX - the width of the image
	//	int sizeY - the height of the image
	// output:
	//	int result[] - int array holding the output image

	// each threads for a signal filter 
	const int BLOCK_SIZE =256;
	// each pixel on the image is a block
	const int n_blocks = ((sizeX*sizeY)/BLOCK_SIZE);

	// TODO: malloc memory, copy input

	// declare device copies 
	int *d_data;
	int *d_result;
	int *d_sizeX, *d_sizeY;
	int *d_FILTER;

	// allocate space fro device copies of 
	cudaMalloc((void **) &d_result, sizeX * sizeY * sizeof(int));
	cudaMalloc((void **) &d_data, sizeX * sizeY * sizeof(int));
	cudaMalloc((void **) &d_sizeX, sizeof(int));
    cudaMalloc((void **) &d_sizeY, sizeof(int));
	cudaMalloc((void **) &d_FILTER, FILTER_WIDTH * FILTER_WIDTH * sizeof(int));

	// Copy data to device
	cudaMemcpy(d_data, data, sizeof(int) * sizeX * sizeY, cudaMemcpyHostToDevice);
	cudaMemcpy(d_result, result, sizeof(int) * sizeX * sizeY, cudaMemcpyHostToDevice);
	cudaMemcpy(d_sizeX, &sizeX, sizeof(int), cudaMemcpyHostToDevice);
    cudaMemcpy(d_sizeY, &sizeY, sizeof(int), cudaMemcpyHostToDevice);
	cudaMemcpy(d_FILTER, FILTER, FILTER_WIDTH * FILTER_WIDTH * sizeof(int), cudaMemcpyHostToDevice);

	// Start timer for kernel
	// Don't change this function
	auto startKernel = chrono::steady_clock::now();

	// TODO: call the kernel function
	sharpen<<<n_blocks, BLOCK_SIZE>>>(d_data, d_result, d_sizeX, d_sizeY, d_FILTER);
	// End timer for kernel and display kernel time
	cudaDeviceSynchronize(); // <- DO NOT REMOVE
	auto endKernel = chrono::steady_clock::now();
	cout << "Kernel Elapsed time: " << chrono::duration <double, milli>(endKernel - startKernel).count() << "ms\n";

	// TODO: copy result from device to host
	cudaMemcpy(result, d_result, sizeof(int) * sizeX * sizeY, cudaMemcpyDeviceToHost);

	// TODO: free device memory <- important, keep your code clean
	cudaFree(d_data);
	cudaFree(d_result);
	cudaFree(d_sizeX);
	cudaFree(d_sizeY);
	cudaFree(d_FILTER);
}


// CPU implementation
void CPU_Test(int data[], int result[], int sizeX, int sizeY) {
	// input:
	//	int data[] - int array holding the flattened original image
	//	int sizeX - the width of the image
	//	int sizeY - the height of the image
	// output:
	//	int result[] - int array holding the output image

	// TODO: sharpen the image with filter
	//       apply zero padding for the border

	// loop the y axis first, then the x axis
	for (int y=0; y<sizeY; y++){
		for (int x=0; x<sizeX; x++){
			int sum = 0;

			// for every cell, iterate over the filter
			 for (int i = 0; i < FILTER_WIDTH; ++i) {
                for (int j = 0; j < FILTER_WIDTH; ++j) {
                    // for each loop of filter, find the center of the filter
                    int centerX = x - FILTER_WIDTH / 2 + j;
                    int centerY = y - FILTER_WIDTH / 2 + i;

                    // add to the sum only if the filter center is within the image boundary, which is possible in reality
                    if (centerX >= 0 && centerX < sizeX && centerY >= 0 && centerY < sizeY) {
                        sum += data[centerY * sizeX + centerX ] * FILTER[i * FILTER_WIDTH + j ];
                    }
                }
            }
			result [y * sizeX + x] = sum;
		}
	}
}

// The input is a 2D grayscale image
// The image is flattened into a text file of pixel values.
int main(int argc, char *argv[]) {
	string inputFile = (argc == 1) ? "image_grey.txt" : argv[1];

	int sizeX;
	int sizeY;
	int *dataForCPUTest;
	int *dataForGPUTest;	

	initData(inputFile, &dataForCPUTest, &sizeX, &sizeY);
	initData(inputFile, &dataForGPUTest, &sizeX, &sizeY);

	int size = sizeX * sizeY;
	int *resultForCPUTest = new int[size];
	int *resultForGPUTest = new int[size];

	cout << "\n";

	cout << "CPU Implementation\n";

	auto startCPU = chrono::steady_clock::now();
	CPU_Test(dataForCPUTest, resultForCPUTest, sizeX, sizeY);
	auto endCPU = chrono::steady_clock::now();

	cout << "Elapsed time: " << chrono::duration <double, milli>(endCPU - startCPU).count() << "ms\n";
	// For debug
	// displayResult(dataForCPUTest, resultForCPUTest, size);

	saveResult("grey_result_CPU.txt",resultForCPUTest, sizeX, sizeY);

	cout << "\n";
	cout << "GPU Implementation\n";

	auto startGPU = chrono::steady_clock::now();
	GPU_Test(dataForGPUTest, resultForGPUTest, sizeX, sizeY);
	auto endGPU = chrono::steady_clock::now();

	cout << "Elapsed time: " << chrono::duration <double, milli>(endGPU - startGPU).count() << "ms\n";

	// For debug
	// displayResult(dataForGPUTest, resultForGPUTest, size);
	
	saveResult("grey_result_GPU.txt",resultForGPUTest, sizeX, sizeY);

	return 0;
}


