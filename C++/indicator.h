#pragma once
#include <iostream>
#include <iomanip>
#include <vector>
#include "candle.h"
using namespace std;

class Indicator {
protected:
	string indicatorName;
	vector<double> values;
	int n;

public: 
	Indicator(int nn) : n(nn) {}

	vector<double>* getValues() { return &values; }

	int getN() const { return n; }

	virtual void calculate(vector<Candle*>*) = 0;

	friend ostream& operator << (ostream& os, const Indicator& i) {
		os << right << setw(3) << i.indicatorName << "(" << i.n << ")" << endl;
		for_each(i.values.begin(), i.values.end(), [&os](double v) {os << setprecision(10) << v << endl; });
		return os;
	}

};