#include <iostream>

using namespace std;

const int MAXLEN = 50;  // max length for input sequence of characters
const int MAXKEY = 10;  // max length for key

void encryption (char text[MAXLEN], int text_length, char key[MAXKEY],int key_length){
  char output;
  int k;
  for (int i=0; i<= text_length; i++){
    if ( (int(text[i])>=65 && int(text[i])<=90) || (int(text[i])>=97 && int(text[i])<=122)) {     // check whether it is a character of not
      k=key[i%key_length];                // give the key of that character
      if(k>=65 && k<=90){ 
        k=k-65;
      }
      else{
        k-=97;
      }
      output=text[i];
      if(output>=65 && output<=90){
        output-=65;
      }
      else{
        output-=97;
      }
      output=(output+k)%26;
      if (int(text[i])>=65 && int(text[i])<=90){     //change uppercase to lowercase, lowercase to uppercase
        cout<<char(output+97);
      }
      else{
        cout<<char(output+65);
      }
    }
    else{
      if (text[i]=='!'){
        cout << text[i] << endl;
      }
      else{
        cout << text[i];
      }
    }
  }
}

void decryption (char text[MAXLEN], int text_length, char key[MAXKEY],int key_length){
  char output;
  int k;
  for (int i=0; i<= text_length; i++){
    if ( (int(text[i])>=65 && int(text[i])<=90) || (int(text[i])>=97 && int(text[i])<=122)) {    // check whether it is a character of not
      k=key[i%key_length];
      if(k>=65 && k<=90){
        k-=65;
      }
      else{
        k-=97;
      }
      output=text[i];
      if(output>=65 && output<=90){
        output-=65;
      }
      else{
        output-=97;
      }
      if ((output-k)<0){
        output+=26;
      }
      output=(output-k)%26;
      if (int(text[i])>=65 && int(text[i])<=90){
        cout<<char(output+97);
      }
      else{
        cout<<char(output+65);
      }
    }
    else{
      if (text[i]=='!'){
        cout << text[i] << endl;
      }
      else{
        cout << text[i];
      }
    }
  }
}

int main()
{
  // to store user inputs
  char s;             // 'e' for encryption, 'd' for decryption
  int key_length;
  int text_length=0;
  char text[MAXLEN];  // the sequence of characters to encrypt/decrypt
  char key[MAXKEY];   // the key
  cin >> s;           // input 'e' or 'd'
  for (int i=0; i<=50; i++){    // input the text using a for loop
    cin >> text[i];
    text_length++;
    if (text[i] == '!'){    // break when '!' is inputed
      text_length--;
      break;
    }
  }
  cin >> key_length;
  for (int i=0; i < key_length; i++){   //input the key
    cin >> key[i];
  }
  if (s=='e'){                    // call function
  encryption(text, text_length, key, key_length);
  }
  else{
  decryption(text, text_length, key, key_length);
}
  return 0;
}
