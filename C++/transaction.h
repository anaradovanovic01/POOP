#pragma once
#include <iostream>
#include <windows.h>
#include "JSONParser.h"
#include "crawler.h"
#include "stock.h"
using namespace std;

class Transaction {
	static int ID;
	int id = ID++;
	Stock* stock;
	int quantity;
	double price;

	double getRelativeDiff(double, double) const;
	
	void copy(const Transaction& s);
	
	void move(Transaction& s);
	
	void remove() { delete stock; }

public:
	Transaction(Stock* s, int q) : stock(s), quantity(q) { price = getCurrentPrice(); }

	Transaction(Stock* s, int q, int i, double p) : stock(s), quantity(q), id(i), price(p) { ID++; }

	Transaction(const Transaction& t) { copy(t); }

	Transaction(Transaction&& t) { move(t); }
	
	~Transaction() { remove(); }
	
	Transaction& operator=(const Transaction& t);
	
	Transaction& operator=(Transaction&& t);

	Stock* getStock() const { return stock; }

	int getId() const { return id; }

	double getPrice() const { return price; }

	double getCurrentPrice() const;

	void changeQuantity(int n) { quantity -= n; }

	friend ostream& operator << (ostream& os, const Transaction& t);
};

