#include <iostream>
#include <iomanip>
#include <string>
#include <cctype>
#include <cstdlib>

using namespace std;

struct Node
{
    int value;
    Node * next;
};

// output the linked list
void print_list(Node * head)
{
    Node * current = head;
    while (current != NULL)
    {
        // process the current node, e.g., print the content
        cout << current->value << " -> ";
        current = current->next;
    }
    cout << "NULL\n";
}

// output the large number stored in the linked list
void print_num(Node * head)
{   
    Node * current = head;
    string output="";
    string value;
    while (current != NULL)
    {   
        if (current->next==NULL){
            value = to_string(current->value);
        }
        else{
            value = to_string(current->value);
            if (value.length()==1){
                value="00"+value;
            }
            else if (value.length()==2){
               value="0"+value; 
            }
        }
        output=value + output ;
        current = current->next;
    }
    cout << output <<endl;
}

// insert a value as a node to the tail of a linked list
void head_insert(Node * & head, Node * & tail, int v)
{   
    Node * p = new Node;
	p->value = v;
	p->next = NULL;

	if (head == NULL) {
		head = p;
		tail = p;
	}
	else {
		tail->next = p;
		tail = p;
	}
}

// delete the head node from a linked list
void delete_head( Node * & head)
{
    if (head != NULL) {
        Node * p = head;
        head = head->next;
        delete p;
    }
}

// free an entire linked list
void delete_list(Node * & head)
{
    while ( head != NULL )
    {
        delete_head(head);
    }
}

// double the capacity of an array
// array: input array
// size: original size of array, updated to new size of array
void grow_array( char * & array, int & size )
{
    if (array == NULL)
        return;

    int newSize = size * 2;

    // doubled the size of the array;
    char * tmp = new char [ newSize ];
    // copy original contents
    for (int i = 0; i < size; ++i)
        tmp[i] = array[i];

    delete [] array;

    array = tmp;
    size = newSize;
}

// get a number from a user
// by reading character by character until a space is hit
// use dynamic array to store the digits
// digits:  character array that stores the digits of the number
// numDigits: number of digits read from input
void input_num(char * & digits, int & numDigits)
{
    int arraysize = 32;
    digits = new char [arraysize];
    char c;
    int numRead = 0;

    // read each digit as a character until a white space is hit
    c = cin.get();
    while (!isspace(c))
    {
        if (numRead >= arraysize)
            grow_array( digits, arraysize );

        digits[numRead] = c;
        numRead++;

        c = cin.get();
    }
    numDigits = numRead;
}

// get a large integer from user input
// and store in a linked list of Node
// each node stores the value of a chunk of 3 digits taken from the large integer
// e.g., if the input is 43323000089500012, the linked list is
// 12 -> 500 -> 89 -> 323 -> 43-> NULL
//
Node * create_num_list()
{
    // declare a pointer pointing to the head of the link list
    Node * head = NULL, * tail = NULL;

    string str;
    char * digits = NULL;  // a dynamic array for storing an input number
    int numDigits;
    int val;

    // get a number from the user
    input_num( digits, numDigits);

    // scan the digits in reverse, and create a list of nodes for
    // the value of every 3 digits
    str.clear();
    for (int i = numDigits-1; i >=0; --i) {
        str = digits[i] + str;
        if (str.length()==3) {
            val = atoi(str.c_str());

            //insert a value as a node to the head of the linked list
            head_insert(head, tail, val);

            str.clear();
        }
    }
    // the digits array is scanned and there are still digits
    // stored in str that are not inserted into the list yet
    if (!str.empty()) {
        val = atoi(str.c_str());

        // insert a value as a node to the head of the linked list
        head_insert(head, tail, val);
    }

    if (digits != NULL) {
        delete [] digits;

    }

    // return the pointer to the linked list
    return head;
}



// return the length of a linked list
int list_length(Node * head)
{
 	// Modify this print function to one that
	// count the number of nodes in a linked list

    int num = 0;

    Node * current = head;
    while (current != NULL)
    {
        // process the current node, e.g., print the content
        ++num;
        current = current->next;
    }

    return num;
}     

int getnum(Node * head, int pos){       //given a postion n, the function return the value of the nth node of the linked list
    Node * current = head;
    int i=0;
    while (current != NULL)
    {
        if (i==pos)
            return current->value;
        current = current->next;
        i++;
    }
    return 0;                       // if the position n given is larger than the length of the linked list, return 0 instead
}

//caclaute the sum of the linked list
Node * sum(Node * n1,Node * n2){
    int length;
    if (list_length(n1)>=list_length(n2)){  // take the length of the longer linked list and the value will be used in the for loop below
        length=list_length(n1);
    }
    else{
        length=list_length(n2);
    }
    Node * head = NULL, * tail = NULL;  // make a new linked list
    bool overflow = false;              // when the additon of two node if greater than or equal to 1000, add one to the next node, use boolean variable to make this operation
    int addition;                       // a int variable to store the result of two additon

    for (int i=0; i<length; i++){
        addition = getnum(n1,i)+ getnum(n2,i)+overflow;
        overflow=false;
        if ( addition>=1000){
            addition-=1000;
            overflow= true;
        }
        head_insert(head, tail, addition);      // add new node to by using list_forward method
    }
    if (overflow){
        head_insert(head, tail, 1);
    }
    return head;
}

int main()
{
    Node * n1, * n2, * n3;

    n1 = create_num_list();
    cin.get();       // skip the '+' sign
    cin.get();       // the space after the '+' sign
    n2 = create_num_list();

    // call print_list() on n1 and n2 for checking
    print_list(n1);
    print_list(n2);

    // call sum() to find the sum of the two linked list
    n3 = sum(n1,n2);
    print_list(n3);
    print_num(n3);

    // free the linked lists
    delete_list(n1);
    delete_list(n2);
    delete_list(n3);

    return 0;
}