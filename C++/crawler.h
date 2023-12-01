#pragma once
#include <iostream>
#include <string>
#define CURL_STATICLIB
#include <curl/curl.h>
#include <fstream>
using namespace std;

static size_t WriteCallback(void* contents, size_t size, size_t nmemb, void* userp) {
	((std::string*)userp)->append((char*)contents, size * nmemb);
	return size * nmemb;
}

class Crawler {
	string stockName;
	string timestamp1 , timestamp2;

public:
	Crawler(string s, string t1, string t2) : stockName(s), timestamp1(t1), timestamp2(t2) {}

	string makeJSON() const;

};