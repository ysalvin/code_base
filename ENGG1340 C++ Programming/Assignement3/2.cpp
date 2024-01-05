#include<iostream>
#include<algorithm>
#include<string>
#include<map>
#include<vector>

using namespace std;

struct Page {
    int id;
    string path;
    int counter;
    Page(int id, string path) {
        this->id = id;
        this->path = path;
        counter = 0;
    };
};

// This function can facilitate sorting
bool operator<(const Page & a, const Page & b) {
    return (a.id < b.id);
}

vector<Page> pages;     // a vector storing info of each page (id, path , counter)

struct User {
    int id;
    vector<string> visits;
    User(int id) {
        this->id = id;
    };
    void add_visit(int page_id) {
        Page p(page_id, "");
        vector<Page>::iterator iter = pages.begin();
          for (int i=0; i<pages.size(); i++){
            if(pages[i].id == p.id) {        // counter the number of visit in each page by a for loop and searching for that page
              visits.push_back(pages[i].path);
            }
          }
        // vector<Page>::iterator iter = lower_bound(pages.begin(), pages.end(), p);
        // if(iter->id == page_id)
        //     visits.push_back(iter->path);
    };
    int size() const {
        return visits.size();
    };
    void print_visits() {
        sort(visits.begin(), visits.end());
        vector<string>::iterator iter;
        for(iter = visits.begin(); iter != visits.end(); iter++) {
            cout << "- " << *iter << endl;
        }
    }
};

vector<User> users;

// Please implement the following function to facilitate the sorting of users
bool operator<(const User & a, const User & b) {
    if (a.size() == b.size()){
        return (a.id < b.id);
    }
    return (a.size() > b.size());
}

// Please implement the following function
void add_page(const Page& p) {
    pages.push_back(p);
}

// Please implement the following function
bool cmp_page_count(const Page & a, const Page & b) {
   if (a.counter == b.counter){
        return (a.path < b.path);
    }
    return (a.counter > b.counter);
}

// Please implement the following function
void print_pages(int number) {
    for (int i=0; i<5; i++){
        cout << pages[i].counter << ':' << pages[i].path << endl;
    }
}

// Please implement the following function
void add_user(User u) {
    users.push_back(u);
}

// Please implement the following function, add visit record for one user
void add_visit(int page_id) {
    users[users.size()-1].add_visit(page_id);
}

// Please implement the following function
void print_users(int number) {
    for (int i=0; i<5; i++){
        cout << users[i].size() << ':' << users[i].id << endl;
        users[i].print_visits();
    }
}

int main() {

    string type;
    while(cin >> type) {                // keep input new string until the end of user input
        if(type == "USER") {            // user record
          int user_id;
          cin >> user_id;
          User u(user_id);
          add_user(u);
        }

        else if(type == "PAGE") {     // page record
          int page_id;
          string page_path;
          cin >> page_id;
          cin >> page_path;
          Page p(page_id, page_path);
          add_page(p);
        }

        else if(type == "VISIT") {    // visiting record
          int page_id;
          cin >> page_id;
          Page p(page_id, "");
          sort(pages.begin(), pages.end(), cmp_page_count);
          // vector<Page>::iterator iter = lower_bound(pages.begin(), pages.end(),p);
          vector<Page>::iterator iter = pages.begin();
          for (int i=0; i<pages.size(); i++){
            if(pages[i].id == p.id) {        // counter the number of visit in each page by a for loop and searching for that page
              pages[i].counter++;
              // cout << "yes";
            }
          }
          // if(iter->id == p.id) {        // counter the number of visit in each page by a for loop and searching for that page
          //   iter->counter++;
          //   cout << "yes";
          // }
          // cout << p.id << endl;
          add_visit(p.id);
        }
    }

    // for (int i=0; i< pages.size(); i++){
    //     cout << pages[i].id << ":" << pages[i].path << ":" << pages[i].counter << endl;
    // }
    //  for (int i=0; i< users.size(); i++){
    //     cout << users[i].id << endl;
    //     for (int j=0; j < users[i].size(); j++){
    //         cout << "-" << users[i].visits[j] << endl;
    //     }
    // }

    sort(pages.begin(), pages.end(), cmp_page_count);
    cout << "*** 5 most popular pages ***" << endl;
    print_pages(5);
    sort(pages.begin(), pages.end());

    sort(users.begin(), users.end());
    cout << "*** 5 most active users ***" << endl;
    print_users(5);

    // for (int i=0; i< pages.size(); i++){
    //     cout << pages[i].id << ":" << pages[i].path << ":" << pages[i].counter << endl;
    // }
    //  for (int i=0; i< users.size(); i++){
    //     cout << users[i].id << endl;
    //     for (int j=0; j < users[i].size(); j++){
    //         cout << "-" << users[i].visits[j] << endl;
    //     }
    // }
    return 0;

}
