#ifndef _JSONParser_h
#define _JSONParser_h
#include <iostream>
#include <algorithm>
#include <cstdio>
#include <string>
#include <regex>
#include <fstream>
#include "Parser.h"
#include "stock.h"
#include "exceptions.h"
using namespace std;

class JSONParser : public Parser {

public:
	JSONParser(string f) : Parser(f) {}

	Stock* parse() override;

	double getCurrentPrice();
};
#endif