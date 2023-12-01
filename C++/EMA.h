#ifndef _EMA_h
#define _EMA_h
#include "indicator.h"
#include "candle.h"

class EMA : public Indicator {
private:
	double ema(int n, vector<Candle*>);
public:
	EMA(int nn) : Indicator(nn) { indicatorName = "EMA"; }

	void calculate(vector<Candle*>*) override;

};
#endif