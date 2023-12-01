package stocks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.*;

public class JSONParser extends Parser {
	
	public JSONParser(String fileName) {
		super(fileName);
	}

	@Override
	public Stock parse() throws CouldntFindInfoAboutStock {
		try {
		String name, timestamp, open, close, low, high;
		List<String> timestamps = new ArrayList<>(); 
		List<Double> opens = new ArrayList<>(), closes = new ArrayList<>(), lows = new ArrayList<>(), highs = new ArrayList<>();
		File file = new File(fileName);
		if (!file.exists()) throw new CantOpenJSONFile();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str = br.readLine();
		br.close(); file.delete();
		Pattern p = Pattern.compile(".*symbol\":\"([^\"]*).*");
		Matcher m = p.matcher(str);
		if(m.matches()) {
			 name = m.group(1);
		}
		else throw new CouldntFindInfoAboutStock();
		
		Pattern p1 = Pattern.compile(".*timestamp\":\\[([^\\]]*)\\].*");
		Matcher m1 = p1.matcher(str);
		if(m1.matches()) {
			 timestamp = m1.group(1);
			 Pattern p2 = Pattern.compile("([^,]+)");
			 Matcher m2 = p2.matcher(timestamp);
			 while(m2.find()) {
				 timestamps.add(m2.group(1));
			 }
		} else throw new CouldntFindInfoAboutStock();
		
		Pattern p3 = Pattern.compile(".+open\":\\[([^\\]]*)\\].+");
		Matcher m3 = p3.matcher(str);
		if(m3.matches()) {
			 open = m3.group(1);
			 Pattern p2 = Pattern.compile("([^,]+)");
			 Matcher m2 = p2.matcher(open);
			 while(m2.find()) {
				 String s = m2.group(1);
				 if(s.equals("null")) break;
				 opens.add(Double.parseDouble(m2.group(1)));
			 }
		}
		
		Pattern p4 = Pattern.compile(".+close\":\\[([^\\]]*)\\].+");
		Matcher m4 = p4.matcher(str);
		if(m4.matches()) {
			 close = m4.group(1);
			 Pattern p2 = Pattern.compile("([^,]+)");
			 Matcher m2 = p2.matcher(close);
			 while(m2.find()) {
				 String s = m2.group(1);
				 if(s.equals("null")) break;
				 closes.add(Double.parseDouble(m2.group(1)));
			 }
		}
		
		Pattern p5 = Pattern.compile(".+high\":\\[([^\\]]*)\\].+");
		Matcher m5 = p5.matcher(str);
		if(m5.matches()) {
			 high = m5.group(1);
			 Pattern p2 = Pattern.compile("([^,]+)");
			 Matcher m2 = p2.matcher(high);
			 while(m2.find()) {
				 String s = m2.group(1);
				 if(s.equals("null")) break;
				 highs.add(Double.parseDouble(m2.group(1)));
			 }
		}
		
		Pattern p6 = Pattern.compile(".+low\":\\[([^\\]]*)\\].+");
		Matcher m6 = p6.matcher(str);
		if(m6.matches()) {
			 low = m6.group(1);
			 Pattern p2 = Pattern.compile("([^,]+)");
			 Matcher m2 = p2.matcher(low);
			 while(m2.find()) {
				 String s = m2.group(1);
				 if(s.equals("null")) break;
				 lows.add(Double.parseDouble(m2.group(1)));
			 }
		}

		Stock stock = new Stock(name);
		
		for(int i = 0; i < opens.size(); i++) {
			Candle candle = new Candle(timestamps.get(i), opens.get(i), closes.get(i), highs.get(i), lows.get(i));
			stock.addCandle(candle);
		}

		return stock;
		
		} catch (IOException e) {} 
		catch (CantOpenJSONFile e) { System.out.println("Problem pri otvaranju .json fajla u kome se cuvaju podaci o akciji."); System.exit(1); }
		return null;
	}
	
	@Override
	public double getCurrentPrice() throws CouldntFindInfoAboutStock {
		try {
			double price;
			File file = new File(fileName);
			if (!file.exists()) throw new CantOpenJSONFile();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			br.close();
			Pattern p = Pattern.compile(".*regularMarketPrice\":([^,]*).*");
			Matcher m = p.matcher(str);
			if(m.matches()) {
				price = Double.parseDouble(m.group(1));
			}
			else throw new CouldntFindInfoAboutStock();
			file.delete();
			return price;
		} catch (IOException e) {}
		catch (CantOpenJSONFile e) { System.out.println("Problem pri otvaranju .json fajla u kome se cuvaju podaci o akciji."); System.exit(1); }
		return 0;
	}
	
}
