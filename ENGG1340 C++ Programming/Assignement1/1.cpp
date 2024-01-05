#include <iostream>
using namespace std;

bool isPalindrome(int x);
bool isProduct(int x);

int main(){
  int m,n; // m<=n
  char opt;
  cin >> m >> n >> opt;
  switch (opt){ //use switch to find the mode p, t, b
    case 'p': // 1. palindromic only
      for (int i=m; i<=n; i++){
          if (isPalindrome(i)){
            cout << i << endl;
        }
      }
      break;

    case 't': // 2. product of two 3-digit numbres only
      for (int i=m; i<=n; i++){
        if (isProduct(i)){
          cout << i << endl;
        }
      }
      break;

    case 'b': // 3. both palindromic andd product of two 3-digit numbers
      for (int i=m; i<=n; i++){
        if (isProduct(i) && isPalindrome(i)){
          cout << i << endl;
        }
      }
      break;
  }
  return 0;
}

bool isPalindrome(int x){
  int reverse=0, original=x;
  while (x>0){                // check whether the number is palindrome or not using % division
    reverse=10*reverse+x%10;
    x/=10;
    }
  if (reverse==original){     // return bool
    return true;
  }
  else{
    return false;
  }
}

bool isProduct(int x){
  for (int i=100; i<1000; i++){           // check whether x is a product of 3-digit letter of not
    if (x%i==0 && x/i<1000 && x/i>99){    // use a for loop to check every letter from 100 to 999, and check whether x/that number return a intergers mathematically and the quotient is a 3 digit number.
      return true;
    }
  }
    return false;
}
