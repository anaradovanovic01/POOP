#include "MA.h"

void MA::calculate(vector<Candle*> *candles) {
	int num; double v, sum; vector<Candle*>::iterator j;
	for (auto i = candles->begin(); i != candles->end(); i++) {
		sum = 0;
		j = i;
		for(num = n + 1; num > 0; num--) {
			sum += (*j)->getClose();
			if (j == candles->begin()) {
				num--; 
				break;
			}
			else j--;
		}
		v = sum / (n + 1 - num);
		values.push_back(v);
	}
}