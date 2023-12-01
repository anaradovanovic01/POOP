#ifndef _user_h_
#define _user_h_
#include <iostream>
#include <string>
#include <fstream>
#include <list>
#include <regex>
#include "exceptions.h"
#include "transaction.h"
using namespace std;

class User {
	string usr;
	string psswrd;
	double money;
	list<Transaction*> transactions;

	void copy(const User& u);
	void move(User& u);
	void remove();

public:
	User(string u, string p, double m);

	User(const User& u) { copy(u); }
	User(User&& u) { move(u); }

	~User() { remove(); }
	
	User& operator=(const User& u);
	User& operator=(User&& u);

	void addTransaction(Transaction* t) { transactions.push_back(t); }

	Transaction* findTransaction(int i) const;

	static User* logIn(string username, string password);

	static User* signUp(string username, string password);

	friend ostream& operator << (ostream& os, const User& u);

	Transaction* buyStock(Stock* stock, int quantity);

	void sellStock(int i, int quantity);

};
#endif