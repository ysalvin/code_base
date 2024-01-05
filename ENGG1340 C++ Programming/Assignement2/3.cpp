#include <iostream>
#include <string>

/**
 * A morse code decoder.
 */

using namespace std;

// IMPORTANT:  Do NOT change any of the function headers
//             It means that you will need to use the function headers as is
//             You may add other functions wherever appropriate


/**
 * Decode the morse code `s` and return the text character
 */

string decode_1(string s){   // using if-else to decode
    if (s[0]=='_'){
        return "T";
    }
    else{
        return "E";
    }
}

string decode_2(string s){      // using if-else to decode
    if (s[0]=='_') {
        if (s[1]=='_'){
            return "M";
        }
        else{
            return "N";
        }
    }
    else{
        if (s[1]=='_'){
            return "A";
        }
        else{
            return "I";
        }
    }
}

string decode_3(string s){          // using if-else to decode
    if (s[0]=='_') {
        if (s[1]=='_'){
            if (s[2]=='_'){
                return "O";
            }
            else{
                return "G";
            }
        }
        else{
            if (s[2]=='_'){
                return "K";
            }
            else{
                return "D";
            }
        }
    }
    else{
        if (s[1]=='_'){
            if (s[2]=='_'){
                return "W";
            }
            else{
                return "R";
            }
        }
        else{
            if (s[2]=='_'){
                return "U";
            }
            else{
                return "S";
            }
        }
    }
}

string decode_4(string s){  // using a 2D array (like a map) to decode
    const string code[12][2]={{"H","...."},{"V","..._"},{"F",".._."},{"L","._.."},{"P",".__."},{"J",".___"},{"B","_..."},{"X","_.._"},{"C","_._."},{"Y","_.__"},{"Z","__.."},{"Q","__._"}};
    for (int i=0; i<12; i++){
        if (s==code[i][1]){
            return code[i][0];
        }
    }
    return "0";
}

string decode_5(string s){      // using a 2D array (like a map) to decode
    const string code[10][2]={{"1",".____"},{"2","..___"},{"3","...__"},{"4","...._"},{"5","....."},{"6","_...."},{"7","__..."},{"8","___.."},{"9","____."},{"0","_____"}};
    for (int i=0; i<10; i++){
        if (s==code[i][1]){
            return code[i][0];
        }
    }
    return "0";
}

string decode_letter (string s){        // decode the code, first seperate the code to differnt function based on the length of code
    switch (s.length()){
        case 1:
            return decode_1(s);
        case 2:
            return decode_2(s);
        case 3:
            return decode_3(s);
        case 4:
            return decode_4(s);
        default:
            return decode_5(s);   
    }
}


string morseCodeToText(string s) {
	string text = "";
    string letter_code="";
    for (int i=0;i<s.length(); i++){       // seperate the code to get the morse code for single character 
        if (s[i]=='|' && s[i+1]=='|'){    
            text=text+decode_letter(letter_code)+" "; // insert a whitespace between word
            letter_code="";
        }
        else if(s[i]=='|'){
            if (letter_code.length() != 0){
               text=text+decode_letter(letter_code); // insert the decored character to text
            }
            letter_code="";
        }
        else{
            letter_code=letter_code+(s[i]);
        }
    }
    text=text+decode_letter(letter_code);
	return text;
}

int main()
{
		string s;
		cin >> s;
		cout << morseCodeToText(s) << '\n';

    return 0;
}