#include <iostream>
#include <cstdlib>      // for calling srand(), rand()
#include <ctime>        // for calling time()

#define SPADE   "\xE2\x99\xA0"
#define CLUB    "\xE2\x99\xA3"
#define HEART   "\xE2\x99\xA5"
#define DIAMOND "\xE2\x99\xA6"
#define NUMCARDS  5

using namespace std;

void DealHand(int cards[]);
void PrintHand(int cards[]);
bool IsFourOfAKind(int cards[]);  // return if the hand is a four of a kind
bool IsFullHouse(int cards[]);    // return if the hand is a full house
bool IsFlush(int cards[]);        // return if the hand is a flush
bool IsThreeOfAKind(int cards[]); // return if the hand is a three of a kind
bool IsTwoPair(int cards[]);      // return if the hand is a two pair
bool IsOnePair(int cards[]);      // return if the hand is a one pair

int main()
{
  int hand[NUMCARDS];   // declare an array of 5 integers to store a hand
  DealHand(hand);
  PrintHand(hand);
  if (IsFourOfAKind(hand)) 
    cout << "\nfour of a kind" << endl;
  else if (IsFullHouse(hand))
    cout << "\nfull house" << endl;
  else if (IsFlush(hand))
     cout << "\nflush" << endl;
  else if (IsThreeOfAKind(hand))
     cout << "\nthree of a kind" << endl;
  else if (IsTwoPair(hand))
     cout << "\ntwo pair" << endl;
  else if (IsOnePair(hand))
     cout << "\none pair" << endl;
  else if (1)
    cout << "\nothers" << endl;
  return 0;
}

void DealHand(int cards[]){
  int seeds;
  cin >> seeds;
  srand(seeds);
  for (int j=0; j<=4; j++){
    cards[j]=rand()%52;
  }
}

void PrintHand(int cards[]){
  for (int j=0; j<=4; j++){
    switch(cards[j]%13){
      case 0:
        cout << 'A';
        break;
      case 10:
        cout << 'J';
        break;
      case 11:
        cout << 'Q';
        break;
      case 12:
        cout << 'K';
        break;
      default:
        cout << cards[j]%13+1;
    }
    switch(cards[j]/13){
      case 0:
        cout << SPADE << " ";
        break;
      case 1:
        cout << HEART << " ";
        break;
      case 2:
        cout << CLUB << " ";
        break;
      case 3:
        cout << DIAMOND << " ";
    }
  }
}

bool IsFourOfAKind(int cards[]){
  int point[13]={0,0,0,0,0,0,0,0,0,0,0,0,0};  // count the num of cards for each pount
  for (int j=0; j<=4; j++){
    point[cards[j]%13]+=1;
    }
  for (int i=0; i<=12; i++){
    if (point[i]==4)
    return true;
  }
  return false;
}

bool IsFullHouse(int cards[]){
  int point[13]={0,0,0,0,0,0,0,0,0,0,0,0,0}; // count the num of cards for each pount
  for (int j=0; j<=4; j++){
    point[cards[j]%13]+=1;
    }
  int full_house_pair[2]={0,0};  //number of 3pairs, number of 2 pairs
  for (int i=0; i<=12; i++){
    if (point [i]==3)
      full_house_pair[0]+=1;
    if (point [i]==2)
      full_house_pair[1]+=1;
  }
  if (full_house_pair[0]==1 && full_house_pair[1]==1){
    return true;
  }
  return false;
}

bool IsFlush(int cards[]){
  int suit[4]={0,0,0,0};      //cout the num of cards for each suit
  for (int j=0; j<=4; j++){
    suit[cards[j]/13]+=1;
  }
  for (int i=0; i<=3; i++){
    if (suit[i]==5){
      return true;
    }
  }
  return false;
}

bool IsThreeOfAKind(int cards[]){
  int point[13]={0,0,0,0,0,0,0,0,0,0,0,0,0};    // count the num of cards for each pount
  for (int j=0; j<=4; j++){
    point[cards[j]%13]+=1;
    }
  for (int i=1; i<=12; i++){
    if (point[i]==3){
      return true;
    }
  }
  return false;
}

bool IsTwoPair(int cards[]){
  int point[13]={0,0,0,0,0,0,0,0,0,0,0,0,0}; // count the num of cards for each pount
  for (int j=0; j<=4; j++){
    point[cards[j]%13]+=1;
    }
  int no_pair=0;
  for (int i=1; i<=12; i++){
    if (point[i]==2){
      no_pair+=1;
    }
  }
  if (no_pair==2){
    return true;
  }
  return false;
}

bool IsOnePair(int cards[]){
  int point[13]={0,0,0,0,0,0,0,0,0,0,0,0,0};    // count the num of cards for each pount
  for (int j=0; j<=4; j++){
    point[cards[j]%13]+=1;
    }
  int no_pair=0;
  for (int i=1; i<=12; i++){
    if (point[i]==2){
      no_pair+=1;
    }
  }
  if (no_pair==1){
    return true;
  }
  return false;
}
