#ifndef _parser_h
#define _parser_h
#include <iostream>
#include <fstream>
#include "stock.h"
using namespace std;

class Parser {
protected:
	string fileName;

public:

	Parser(string f) : fileName(f) {}

	virtual Stock* parse() = 0;

	virtual double getCurrentPrice() = 0;

};
#endif