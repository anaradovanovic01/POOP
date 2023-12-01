#ifndef _MA_h
#define _MA_h
#include "indicator.h"

class MA : public Indicator {
public:
	
	MA(int nn) : Indicator(nn) { indicatorName = "MA"; }

	void calculate(vector<Candle*>*) override;
};
#endif