package stocks;

import exceptions.CouldntFindInfoAboutStock;

public abstract class Parser {
	
	protected String fileName;

	public Parser(String fileName) {
		this.fileName = fileName;
	}
	
	public abstract Stock parse() throws CouldntFindInfoAboutStock;

	public abstract double getCurrentPrice() throws CouldntFindInfoAboutStock;

}
