#include "JSONParser.h"

Stock* JSONParser::parse() {
	ifstream file; string word, str;
	file.open(fileName);
	if (!file.is_open()) throw CantOpenJSONFile();
	file >> str;
	
	regex rx(".*symbol\":\"([^\"]*).*");
	smatch result;
	regex_match(str, result, rx);
	string name = result.str(1);

	regex rxx(".+?timestamp\":\\[([^\\]]*)\\].+");
	regex_match(str, result, rxx);
	string timestamps = result.str(1);
	
	regex rx1(".+?open\":\\[([^\\]]*)\\].+");
	regex_match(str, result, rx1);
	string open = result.str(1);

	regex rx2(".+?close\":\\[([^\\]]*)\\].+");
	regex_match(str, result, rx2);
	string close = result.str(1);

	regex rx3(".+?high\":\\[([^\\]]*)\\].+");
	regex_match(str, result, rx3);
	string high = result.str(1);

	regex rx4(".+?low\":\\[([^\\]]*)\\].+");
	regex_match(str, result, rx4);
	string low = result.str(1);

	Stock* s = new Stock(name);

	regex rx5("([^,]+)");
	sregex_iterator begin1(timestamps.begin(), timestamps.end(), rx5), begin2(open.begin(), open.end(), rx5), begin3(close.begin(), close.end(), rx5);
	sregex_iterator begin4(high.begin(), high.end(), rx5), begin5(low.begin(), low.end(), rx5);
	sregex_iterator end;

	while (begin1 != end) {
		smatch sm1 = *begin1; smatch sm2 = *begin2; smatch sm3 = *begin3; smatch sm4 = *begin4; smatch sm5 = *begin5;
		if (sm2.str(1) == "null") break;
		string t = sm1.str(1); double o = stof(sm2.str(1)); double c = stof(sm3.str(1)); double h = stof(sm4.str(1)); double l = stof(sm5.str(1));
		Candle* candle = new Candle(t, o, c, h, l);
		s->addCandle(candle);
		begin1++; begin2++; begin3++; begin4++; begin5++;
	}

	file.close();
	remove(fileName.c_str());
	return s;
}

double JSONParser::getCurrentPrice() {
	string str;
	fstream file(fileName);
	if (!file.is_open()) throw CantOpenJSONFile();
	file >> str;
	regex rx(".+?regularMarketPrice\":([^,]*).+");
	smatch result;
	if (!regex_match(str, result, rx))
		throw CouldntFindInfoAboutStock();
	double price = atof(result.str(1).c_str());
	file.close();
	remove(fileName.c_str());
	return price;
}
