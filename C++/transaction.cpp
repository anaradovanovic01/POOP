#include "transaction.h"

int Transaction:: ID = 1;

double Transaction::getRelativeDiff(double x, double y) const {
	if (x > y) return (x / y - 1) * 100;
	else return (y / x - 1) * 100;
}

void Transaction::copy(const Transaction & t) {
	this->id = t.id;
	this->stock = new Stock(*(t.stock));
	this->quantity = t.quantity;
	this->price = t.price;
}

void Transaction::move(Transaction& t) {
	this->id = t.id;
	this->stock = t.stock;
	t.stock = nullptr;
	this->quantity = t.quantity;
	this->price = t.price;
}

ostream & operator<<(ostream & os, const Transaction & t) {
	double currentPrice = t.getCurrentPrice(); 
	os << " " << left << setw(5) << t.id << left << setw(10) << t.stock->getName() << left << setw(10) << t.quantity;
	os << left << setw(15) << t.price << left << setw(15) << currentPrice;
	if ((t.price - currentPrice) >= 0) SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 12);
	else SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 10);
	os << left << setw(25) << setprecision(6) << abs(currentPrice - t.price) << setprecision(4) << t.getRelativeDiff(currentPrice, t.price) << "%";
	SetConsoleTextAttribute(GetStdHandle(STD_OUTPUT_HANDLE), 7);
	return os;
}

Transaction & Transaction::operator=(const Transaction & t) {
	if (this != &t) {
		remove();
		copy(t);
	}
	return *this;
}

Transaction & Transaction::operator=(Transaction && t) {
	if (this != &t) {
		remove();
		move(t);
	}
	return *this;
}

double Transaction::getCurrentPrice() const {
	fstream file;
	string stockName = stock->getName() + ".json";
	file.open(stockName);
	if (!file.is_open()) {
		//time_t seconds = time(NULL);
		//string timestamp = to_string(seconds);
		string timestamp = "1616072670";
		Crawler crawler(stock->getName(), timestamp, timestamp);
		crawler.makeJSON();
	}
	JSONParser jsp(stockName);
	return jsp.getCurrentPrice();
}