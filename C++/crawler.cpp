#include "crawler.h"

string Crawler::makeJSON() const {
	string fileName;
	string url = "https://query1.finance.yahoo.com/v8/finance/chart/" + stockName + "?period1=" + timestamp1 + "&period2=" + timestamp2 + "&interval=1h";
	CURL* curl;
	CURLcode res;
	string readBuffer;
	curl = curl_easy_init();
	if (curl) {
		curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);
		res = curl_easy_perform(curl);
		curl_easy_cleanup(curl);
	}
	fileName = stockName + ".json";
	ofstream f(fileName);
	f << readBuffer;
	f.close();
	return fileName;
}
