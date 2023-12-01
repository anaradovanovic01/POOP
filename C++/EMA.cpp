#include "EMA.h"

double EMA::ema(int n, vector<Candle*> c) {
	if (n == 1) return c[0]->getClose();
	else return c[n-1]->getClose() * (2 / (n + 1)) + ema((n - 1), c) * (1 - (2 / (n + 1)));
}

void EMA::calculate(vector<Candle*> *candles) {
	vector<Candle*> c; double v; int num; vector<Candle*>::iterator j;
	for (auto i = candles->begin(); i != candles->end(); i++) {
		j = i;
		for (num = n + 1; num > 0; num--) {
			c.push_back(*j);
			if (j == candles->begin()) break;
			else j--;
		}
		double v = ema(c.size(), c);
		values.push_back(v);
		c.clear();
	}
}
