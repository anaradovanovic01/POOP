#pragma once
#include <iostream>
#include <string>
#include <array>
#include <iomanip>
#include <windows.h>
using namespace std;

class Candle {
	string timestamp;
	array<double, 4> values; //Open, Close, High, Low

public:
	Candle(string t, double o, double c, double h, double l) : timestamp(t) {
		values[0] = o; values[1] = c; values[2] = h; values[3] = l;
	}

	double getOpen() const { return values[0]; }

	double getClose() const { return values[1]; }

	friend ostream& operator << (ostream &os, const Candle& c) {
		if (c.getClose() > c.getOpen()) SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 10);
		else SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 12);
		os << left << setw(13) << c.timestamp;
		for (auto i = c.values.begin(); i != c.values.end(); i++) {
			if(i != c.values.end() - 1) os << left << setw(14) << setprecision(10) << *i;
			else os << left << setw(14) << setprecision(10) << *i;
		}
		SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
		return os;
	}
};

