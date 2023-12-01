#include <iostream>
#include <fstream>
#include <algorithm>
#include <string>
#include <direct.h>
#include <windows.h>
#include <regex>
#include "user.h"
#include "candle.h"
#include "exceptions.h"
#include "stock.h"
#include "JSONParser.h"
#include "crawler.h"
#include "MA.h"
#include "EMA.h"
using namespace std;

int main() {
	//dodaj lambda fje
	try {
		int flag = 1, o, n, pom, id, quantity; char c;
		string username, password, timestamp1, timestamp2, stockName, json; User* u = nullptr;
		
		if (GetFileAttributes("files") == INVALID_FILE_ATTRIBUTES) _mkdir("files");

		//login/signup
			while (flag) {
				cout << "Meni:" << endl << "  1. Ulogujete se" << endl << "  2. Novi nalog" << endl << "Odaberite stavku iz menija: ";
				cin >> o;
				switch (o) {
				case 1: { //login
					do {
						cout << "Korisnicko ime: ";
						cin.ignore();
						getline(cin, username);
						regex rx(".*[ |\\|/|\*|:|\?|\"|\||<|>].*");
						smatch result;
						if (regex_match(username, result, rx)) {
							cout << "Greska! Korisnicko ime ne moze da sadrzi: razmak, /, \\, *, :, ?, \", |, <, >. Pokusajte ponovo!";
							cout << endl << endl;
							break;
						}
						cout << "Lozinka: ";
						getline(cin, password);
						if (regex_match(password, result, rx)) {
							cout << "Greska! Lozinka ne moze da sadrzi: razmak, /, \\, *, :, ?, \", |, <, >. Pokusajte ponovo!";
							cout << endl <<  endl;
							break;
						}
						u = User::logIn(username, password);
					} while (!u);
					if (u) {
						cout << "Uspesno ste se ulogovali!" << endl << endl;
						flag = 0;
					}
					break;
				}
				case 2: {
					flag = 0;
					do {
						cout << "Korisnicko ime: ";
						cin.ignore();
						getline(cin, username);
						regex rx(".*[ |\\|/|\*|:|\?|\"|\||<|>].*");
						smatch result;
						if (regex_match(username, result, rx)) {
							cout << "Greska! Korisnicko ime ne moze da sadrzi: razmak, /, \\, *, :, ?, \", |, <, >. Pokusajte ponovo!";
							cout << endl << endl;
							continue;
						}
						cout << "Lozinka: ";
						getline(cin, password);
						if (regex_match(password, result, rx)) {
							cout << "Greska! Lozinka ne moze da sadrzi: razmak, /, \\, *, :, ?, \", |, <, >. Pokusajte ponovo!";
							cout << endl << endl;
							continue;
						}
						u = User::signUp(username, password);
					} while (!u);
					cout << "Uspesno ste se registrovali!" << endl << endl;
					flag = 0;
					break;
				}
				default:
					cout << "Uneta opcija ne postoji u meniju, pokusajte ponovo\n";
				}
			}
			flag = 1;

		//Pocetni meni
		cout << "Meni:" << endl << "  1. Prikupi podatke o odredjenoj akciji" << endl << "  2. Kupi akciju" << endl;
		cout << "  3. Prodaj akciju" << endl << "  4. Prikazi portfolio korisnika" << endl << "  0. Kraj rada";
		while (flag) {
			cout << endl << "Odaberite stavku iz menija: ";
			cin >> o;
			switch (o) {
			case 1: { //prikupi podakte o akciji
				cout << "Unesite period (dva timestapm-a) i ime akcije za koju trazite podatke: ";
				cin >> timestamp1; cin >> timestamp2; cin >> stockName;
				Crawler crawler(stockName, timestamp1, timestamp2);
				json = crawler.makeJSON();
				JSONParser js(json);
				Stock* s = js.parse();
				if (s->numOfCandles()) {
					cout << endl << "Da li zelite da vam se uz osnovne podatke o akciji (open, close, high i low) prikazu i statisticki pokazatelji "
						"Moving Average - MA(n) i Exponential Moving Average EMA(n)." << endl << "  1. Ne" << endl << "  2. MA";
					cout << endl << "  3. EMA" << endl << "  4. MA i EMA" << endl << "Odaberite stavku iz menija: ";
					cin >> pom;
					if (pom != 1) {
						cout << "Unesite n: ";
						cin >> n;
					}
					switch (pom) {
					case 1:
						break;
					case 2:
						s->calculateMA(n);
						break;
					case 3:
						s->calculateEMA(n);
						break;
					case 4:
						s->calculateMA(n);
						s->calculateEMA(n);
						break;
					}
					cout << endl << *s << endl;
				}
				else cout << "Uneli ste nevalidne podatke!" << endl;
				delete s;
				break;
			}
			case 2: { //kupi akciju
				cout << "Unesite ime akcije i kolcinu koju hocete da kupite: ";
				cin >> stockName; cin >> quantity;
				//time_t seconds = time(NULL);
				//string timestamp = to_string(seconds);
				string timestamp = "1616072670";
				Crawler crawler(stockName, timestamp, timestamp);
				json = crawler.makeJSON();
				Stock* s = new Stock(stockName);
				Transaction* t = u->buyStock(s, quantity);
				if (t) {
					cout << "Uspesno ste kupili akciju." << endl;
					u->addTransaction(t);
				}
				break;
			}
			case 3: { //prodaj akciju
				cout << "Unesite id transakcije koju prodajete i kolcinu koju hocete da prodate: ";
				cin >> id; cin >> quantity;
				u->sellStock(id, quantity);
				break;
			}
			case 4: { //prikazi portfolio
				cout << endl << *(u);
				break;
			}
			case 0: { //kraj
				cout << endl << "Da li ste stigurni da zelite da zavrsite rad? D za da, N za ne: ";
				cin >> c;
				if (c == 'd' || c == 'D') {
					flag = 0;
					delete u;
				} 
				break;
			}
			default: {
				cout << "Uneta opcija ne postoji u meniju, pokusajte ponovo\n";
			}
			}
		}
	}
	catch (exception e) {
		cout << e.what() << endl;
	}
	return 0;
}