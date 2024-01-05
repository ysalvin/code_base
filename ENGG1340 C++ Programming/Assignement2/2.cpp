// use https://www.sudoku-solutions.com/ to generate a board
// this website also supports using particular techniques to solve for a board
// from preferences, choose Naked-Singles only or partial solving
// then click "Solve Partially", you may then cross-check with your progran output

#include <iostream>

using namespace std;

// IMPORTANT:  Do NOT change any of the function headers
//             It means that you will need to use the function headers as is
//             You may add other functions wherever appropriate

// get user input and store the game board in the 2D array b
void ReadBoard( int b[][9] )
{
  for (int i=0; i<3; i++){
    for (int j=0; j<3; j++){
      cin >> b[i*3][j*3] >> b[i*3][(j*3+1)] >> b[i*3][(j*3+2)] >> b[(i*3+1)][j*3] >> b[(i*3+1)][(j*3+1)] >> b[(i*3+1)][(j*3+2)] >> b[(i*3+2)][j*3] >> b[(i*3+2)][(j*3+1)] >> b[(i*3+2)][(j*3+2)];
    }
  }
}

// display the game board stored in the 2D array b
void PrintBoard( int b[][9] )
{
  char b2[9][9];      // in order to print 'x' in the board, the board is first changed to a char array
  for (int i=0; i<9; i++){
    for (int j=0; j<9; j++){
      if(b[i][j]==0){
        b2[i][j]='x';
      }
      else{
        b2[i][j]=char(b[i][j]+'0');  
      }
    }
  }
  for (int i=0; i<3; i++){    // access the element of the 2D board array using 2 nested for loop
    for (int j=0; j<3; j++){
      cout << b2[i*3][j*3] << ' ' << b2[i*3][(j*3+1)] << ' ' << b2[i*3][(j*3+2)] << " | " << b2[(i*3+1)][j*3] << ' ' << b2[(i*3+1)][(j*3+1)] << ' ' << b2[(i*3+1)][(j*3+2)] << " | " << b2[(i*3+2)][j*3] << ' ' << b2[(i*3+2)][(j*3+1)] << ' ' << b2[(i*3+2)][(j*3+2)] << ' ' << endl;
    }
    if (i !=2){
          cout << "------+-------+-------" << endl;
    }
  }
}

void check_single( int b[][9], int digit[10], int i, int j ){
  for (int a=0; a<9; a++){      // check cell, remove all not allowed digit from the digit[10] array
    if (b[i][a]!=0){
      digit[(b[i][a])]=0;
    }
  }
  for (int x=0; x<3; x++){      // check row, remove all not allowed digit from the digit[10] array
    for (int y=0; y<3; y++){
      if ( b [(i/3)*3+x] [(j/3)*3+y]  !=0){
        digit[( b [(i/3)*3+x] [(j/3)*3+y]  )]=0;
      }
    }
  }
  for (int x=0; x<3; x++){      // check col, remove all not allowed digit from the digit[10] array
    for (int y=0; y<3; y++){
      if ( b [ i%3+x*3 ] [j%3+y*3]  !=0){
        digit[( b [i%3+x*3] [j%3+y*3]  )]=0;
      }
    }
  }
  // cout << "i is " << i << " j is " << j << "  digit 0-9 are  " << digit[0] << digit[1] << digit[2] << digit[3] << digit[4] << digit[5] << digit[6] << digit[7] << digit[8] << endl;
}

// solve a game board stored in b using the Naked Single technique only
// the (partial) solution is stored in the input array b
void SolveBoard( int b[][9] )
{
  // /cout << "check0" <<endl ;
  int count0=0;
  int num=0;
  for (int i=0; i<9; i++){
    for (int j=0; j<9; j++){
      // cout <<i << j <<endl;
      if (b[i][j]==0){ 
        int digit[10]={0,1,2,3,4,5,6,7,8,9};  // this array store the possible allowed digit in each unfilled cell, digit[0] is not used 
        // cout <<i << j <<endl;
        check_single(b,digit,i,j);
        count0=0;
        num=0;
        for (int k=1; k<10; k++){    // count the number of 0 in the digit array, digit[0] is not used
          if (digit[k]==0){
            count0++;
          }
          else{
            num=digit[k];
          }
        }
        if (count0==8 && num !=0){  // if there is only one allowed digit, fill that cell with the allowed digit 
          b[i][j]=num;
          SolveBoard( b );      // if one new cell is filled, function sloveboard will be called again and check each unfilled cell from the beginning
        }                       // since this function is void, no return is need
      }
    }
  }
  // cout <<"check1";
}

// You do not need to change anything in the main() function
int main()
{
  int board[9][9];    // the 9x9 game board

  ReadBoard( board );

  cout << "Input Sudoku board:" << endl;
  PrintBoard( board );

  // solve the board using the naked single technique only
  SolveBoard( board );

  cout << endl;
  cout << "Final Sudoku board:" << endl;
  PrintBoard( board );

  return 0;
}
