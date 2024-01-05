#include <iostream>
#include <iomanip>
#include <cmath>

using namespace std;

const double e=2.72;

// IMPORTANT:  Do NOT change any of the function headers
//             It means that you will need to use the function headers as is

// Function: sigmoid activation function
// Input: double x: the input of sigmoid activation function
// Ouput: the output of sigmoid activation function
double sigmoid(double x)
{
  return (1/(1+pow(e,-x)));
}

// Function: tanh activate function
// Input: double x: the input of tanh activation function
// Ouput: double: the output of tanh activation function.
double tanh(double x)
{
  return (2*sigmoid((2*x))-1);
}

// Function: compute the next hidden value in an RNN cell
// Input: double x: current input value
//        double h: current hidden status in RNN cell
// Ouput: double: next hidden value in RNN cell
double ComputeH(double x, double h)
{
  double function_input= (0.5*x-2*h);
  return tanh(function_input);
}

// Function: compute the output value at current time step
// Input: double x: current input value
//        double h: current hidden status in RNN cell
// Ouput: double: current output value
double ComputeO(double x, double h)
{
  double function_input= (0.1*x+1.5*h);
  return sigmoid(function_input);
}

// Function: print the values stored in a 1D-array to screen
// Input: double xs[100]: the value of the sequence
//        int seq_len: the length of the sequence
void PrintSeqs(double xs[100], int seq_len)
{
  for (int i=0; i < seq_len; i++){
    if (i==seq_len-1){
      cout << std::fixed << std::setprecision(10) << xs[i] << " " << endl;
    }
    else{
      cout << std::fixed << std::setprecision(10) << xs[i] << " ";
    }
  }
}

// Function: main function
int main()
{
  int T;
  double h_inital;
  cin >> T >> h_inital;
  double x[100];
  double h[100]; // output array from h1,h2, ..., hT
  double o[100]; // output array from o0,o1, ..., oT-1
  for (int i=0; i<T; i++){ //input x into the array sequences x0,x1,x2,.....,xT-1
    cin >> x[i];
  }
  for (int i=0; i<T; i++){  //for loop, each loop repesent one RNN cell
  if (i==0){                // for the first cell, use h0 instead take the value form the h array
    h[0]=ComputeH(x[0],h_inital);
    o[0]=ComputeO(x[0],h_inital);
  }
  else{
    h[i]=ComputeH(x[i],h[i-1]);
    o[i]=ComputeO(x[i],h[i-1]);
  }
}
  PrintSeqs(h,T);
  PrintSeqs(o,T);
}
