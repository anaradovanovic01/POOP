#include "stock.h"

ostream & operator<<(ostream & os, const Stock & s) {
	vector<double>::iterator j; vector<double>::iterator k;
	os << s.name << endl;
	os << left<< setw(13) <<"Timestamp" << left << setw(14) << "Open" << left << setw(14) << "Close";
	os << left << setw(14) << "High" << left << setw(14) << "Low"; 	
	if (s.ma) {
		j = s.ma->getValues()->begin();
		os << "MA(" << s.ma->getN() << ")         ";
	}
	if (s.ema) {
		k = s.ema->getValues()->begin();
		os << "EMA(" << s.ema->getN() << ")";
	}
	os << endl;
	for (Candle* c : s.candles) {
		os << *c;
		if (s.ma) { os << left << setw(14) << setprecision(10) << *j; j++; }
		if (s.ema) { os << left << setw(14) << setprecision(10) << *k; k++; }
		os << endl;
	}
	return os;
}

void Stock::copy(const Stock & s) {
	this->name = s.name;
	for_each(s.candles.begin(), s.candles.end(), [this, s](Candle *c) {
		Candle* nc = new Candle(*c);
		this->addCandle(nc);
	});
	this->ma = new MA(*(s.ma));
	this->ema = new EMA(*(s.ema));
}

void Stock::move(Stock & s) {
	this->name = s.name;
	for_each(s.candles.begin(), s.candles.end(), [this, s](Candle* c){
		this->candles.push_back(c);
		c = nullptr;
	});
	s.candles.clear();
	this->ma = s.ma; this->ema = s.ema;
	s.ma = nullptr; s.ema = nullptr;
}

void Stock::remove() {
	for_each(candles.begin(), candles.end(), [](Candle *c) {
		delete c;
	});
	candles.clear();
	delete ma; 
	delete ema;
}

Stock & Stock::operator=(const Stock & s) {
	if (this != &s) {
		remove();
		copy(s);
	}
	return *this;
}

Stock & Stock::operator=(Stock && s) {
	if (this != &s) {
		remove();
		move(s);
	}
	return *this;
}

void Stock::addCandle(Candle * c) {
	candles.push_back(c);
}

int Stock::numOfCandles() const {
	return candles.size();
}

void Stock::calculateMA(int n) {
	ma = new MA(n);
	ma->calculate(getCandles());
}

void Stock::calculateEMA(int n) {
	ema = new EMA(n);
	ema->calculate(getCandles());
}
