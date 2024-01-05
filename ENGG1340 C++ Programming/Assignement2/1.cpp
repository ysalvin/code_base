#include <iostream>
#include <string>
using namespace std;

void swap(string lst[30], int i, int m){    // function to swap the element in the array
    string temp;    
    temp=lst[i];
    lst[i]=lst[m];
    lst[m]=temp;
}

void sort_length (string lst[30], int length){
    int m;
    for (int i=0; i<length; i++){           // selection sort is used, the array is sorted base on the length of the name
        m=i;
        for (int j=i; j<length; j++){
            if (lst[m].length()<lst[j].length()){       // compare the length of name
                m=j;
            }
        }
        swap(lst,i,m);      // call swap function
    }
}

void sort_word(string lst[30], int length){     
    int m;
    string m1, j1;
    for (int i=0; i<length; i++){       // selection sort is used, the array is sorted in lexicographical order of the name
        m=i;
        for (int j=i; j<length; j++){
            m1=lst[m];
            for (int k=0; k< m1.length(); k++){
                m1[k]=tolower(m1[k]);       // to lower the case of the first character (case insensitive)
            }      
            j1=lst[j];
            for (int k=0; k< j1.length(); k++){
                j1[k]=tolower(j1[k]);       // to lower the case of the first character (case insensitive)
            }   
            if ( m1 > j1 && lst[m].length()==lst[j].length()){      // compare the string and swap the element if and only if the lexicographical order and the length of the element is the same
                m=j;
            }
        }
        swap(lst,i,m);      // call swap function
    }
}

int main(){
    string name_lst[30];        // given max 30 names are inputed
    string name;
    int length=0;
    for (int i=0; i<30; i++){       // input name and store the name in a string array
        cin >> name;
        if (name == "???"){         // break the for loop if "???" is entered
            break;
        }
        name_lst[i]=name;
        length++;
    }

    sort_length(name_lst,length);   // sort length first
    sort_word(name_lst,length);     // then sort the array in lexicographical order

    for (int i=0; i<length; i++){
        cout << name_lst[i]<< endl;     //print the array
    }
    return 0;
}
