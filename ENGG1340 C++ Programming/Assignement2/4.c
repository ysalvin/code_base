#include <stdio.h>
#include <string.h>

void reverse(char num[30], int length){
    char temp;
    for (int i=0; i<length/2; i++){  // swap function , using temp varaible
        temp=num[length-1-i];
        num[length-1-i]=num[i];
        num[i]=temp;
    }
}

void get_num(char rnum[30], int length, int num[30]){  // change char string to int array, for further mathematical calculation 
    for (int i=0; i<length; i++){
        num[i]=(rnum[i]-'0');
    }
}

int main() {
    char rnum[30];
    int num[30];
    int s1=0 ,s2=0, j;
    int length;
    scanf("%s",rnum);
    length=strlen(rnum);
    reverse(rnum, length);          // reverse the string
    printf("%s\n",rnum);            // print the reversed string
    get_num(rnum,length,num);

    for (int i=0; i<length; i+=2){      // find s1
        s1+=num[i];
    }

    for (int i=1; i<length; i+=2){      // find s2
        j=num[i]*2;
        if (j<10){
            s2+=j;
        }
        else{
            s2+=(j/10+j%10);
        }
    }
    printf("%d %d\n", s1, s2);      // print s1 and s2
    if (((s1+s2)%10)==0){           // print the result of the program 
        printf("valid\n");
    }
    else{
        printf("invalid\n");
    }

    return 0;
}