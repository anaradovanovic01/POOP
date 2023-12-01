#ifndef _stock_h
#define _stock_h
#include <iostream>
#include <vector>
#include <fstream>
#include "candle.h"
#include "EMA.h"
#include "MA.h"
#include "exceptions.h"
using namespace std;

class Stock {
	string name;
	vector<Candle*> candles;
	MA* ma;
	EMA* ema;

	void copy(const Stock& s);
	void move(Stock& s);
	void remove();

public:

	Stock(string n) : name(n), ma(nullptr), ema(nullptr) {}

	Stock(const Stock& s) { copy(s); }
	Stock(Stock&& s) { move(s); }
	~Stock() { remove(); }
	Stock& operator=(const Stock& s);
	Stock& operator=(Stock&& s);

	void addCandle(Candle* c);

	int numOfCandles() const;

	string getName() const { return name; }

	vector<Candle*>* getCandles() { return &candles; }

	MA* getMA() const { return ma; }

	EMA* getEMA() const { return ema; }

	void calculateMA(int n);

	void calculateEMA(int n);
	
	friend ostream& operator << (ostream& os, const Stock& s);

};
#endif