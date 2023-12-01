#include "user.h"

void User::copy(const User & u) {
	this->usr = u.usr;
	this->psswrd = u.psswrd;
	this->money = u.money;
	for_each(u.transactions.begin(), u.transactions.end(), [this](Transaction* t) {
		Transaction* nt = new Transaction(*t);
		this->transactions.push_back(nt);
	});
}

void User::move(User & u) {
	this->usr = u.usr;
	this->psswrd = u.psswrd;
	this->money = u.money;
	for_each (u.transactions.begin(), u.transactions.end(), [this](Transaction* t) {
		this->transactions.push_back(t);
		t = nullptr;
	});
	u.transactions.clear();
}

void User::remove() {
	for_each(transactions.begin(), transactions.end(), [](Transaction* t){ delete t; });
	transactions.clear();
}

User::User(string u, string p, double m) {
	usr = u;
	psswrd = p;
	money = m;
}

User & User::operator=(const User & u) {
	if (this != &u) {
		remove();
		copy(u);
	}
	return *this;
}

User & User::operator=(User && u) {
	if (this != &u) {
		remove();
		move(u);
	}
	return *this;
}

Transaction * User::findTransaction(int i) const {
	for (Transaction* t: transactions) {
		if (t->getId() == i) return t;
	}
	return nullptr;
}

User * User::logIn(string username, string password) {
	ifstream file, userfile; string line, word; char c; double money;
	file.open("files/users.txt");
	if (file.is_open()) {
		while (file >> word) {
			if (word == username) {
				file >> word;
				if (word == password) {
					string filename = "files/" + username + ".txt";
					userfile.open(filename);
					if (!userfile.is_open()) throw FileDoesntExist();
					string line; bool flag = 1;
					regex rx("(.*);(.*);(.*);(.*)");
					smatch result;
					userfile >> money;
					User* u = new User(username, password, money);
					while (userfile >> line) {
						bool l = regex_match(line, result, rx);
						int id = atoi(result.str(1).c_str());
						string stockName = result.str(2);
						int quantity = atoi(result.str(3).c_str());
						double price = atof(result.str(4).c_str());
						Stock* s = new Stock(stockName);
						Transaction* t = new Transaction(s, quantity, id, price);
						u->transactions.push_back(t);
					}
					file.close(); userfile.close();
					return u;
				}
				else {
					cout << "Pogresna sifra." << endl;
					file.close();
					return nullptr;
				}
			} 
			else file >> word;
		}
		file.close();
	}
	cout << "Korisnicko ime ne postoji. Da li zelite da napravite novi nalog sa unetim informacijama? D za da, N za ne\n";
	cin >> c;
	if (c == 'd' || c == 'D') return signUp(username, password);
	else return nullptr;	
}

User * User::signUp(string username, string password) {
	double money; ofstream file;
	cout << "Unesite pocetni ulog: ";
	cin >> money;
	file.open("files/users.txt", ios_base::app);
	if (!file.is_open()) ofstream file("files/users.txt");
	file << username + " " + password + "\n";
	string filename = "files/" + username + ".txt";
	ofstream userfile(filename);
	userfile << money;
	file.close();
	userfile.close();
	return new User(username, password, money);
}

Transaction* User::buyStock(Stock* stock, int quantity) {
	try {
		fstream file;
		file.open("files/" + usr + ".txt", ios_base::app);
		if (!file.is_open()) throw FileDoesntExist();
		Transaction* t = new Transaction(stock, quantity);
		double price = t->getPrice();
		if (money < price * quantity) {
			delete t; file.close();
			throw NotEnoughMoney();
		}
		money -= price * quantity;
		//transakcija: id;akcija;kolicina;cena  
		file << endl << to_string(t->getId()) + ";" + stock->getName() + ";" + to_string(quantity) + ";" + to_string(price);
		file.close();
		file.open("files/" + usr + ".txt");
		string whole = "", line; bool flag = 1;
		while (file >> line) {
			if (flag) {
				whole = to_string(money) + "\n";
				flag = 0;
			}
			else whole += line + "\n";
		}
		file.clear();
		file.seekg(0, ios::beg);
		file << whole;
		file.close();
		return t;
	}
	catch (NotEnoughMoney e) {
		cout << e.what() << endl;
		return nullptr;
	}
	catch (CouldntFindInfoAboutStock e) {
		cout << e.what() << endl;
		return nullptr;
	}
}

void User::sellStock(int i, int quantity) {
	try {
		Transaction* tr = findTransaction(i);
		if (!tr) throw ActionDoesntExist();
		Stock* stock = tr->getStock();
		fstream file;
		file.open("files/" + usr + ".txt");
		if (!file.is_open()) throw FileDoesntExist();
		string line, l, whole; bool flag = 0;
		string f = to_string(i) + ";" + stock->getName();
		while (file >> line) {
			int pos = line.find(f);
			if (pos >= 0) {
				flag = 1;
				break;
			}
		}
		if (!flag) throw ActionDoesntExist();
		//transakcija: id;akcija;kolicina;cena 
		regex rx("(.*);.*;(.*);(.*)"); 
		smatch result;
		regex_match(line, result, rx);
		int q = atoi(result.str(2).c_str());
		if (q < quantity) throw ActionDoesntExist();
		double price = tr->getCurrentPrice();
		money += price * quantity;
		file.clear();
		file.seekg(0, ios::beg);
		flag = 1;
		while (file >> l) {
			if (flag) {
				whole = to_string(money) + "\n";
				flag = 0;
			}
			else {
				if (l == line) {
					if (q > quantity) {
						string id = result.str(1);
						string p = result.str(3);
						string newLine = id + ";" + stock->getName() + ";" + to_string(q - quantity) + ";" + p + "\n";
						whole += newLine;
						tr->changeQuantity(quantity);
					}
					else {
						transactions.remove(tr);
					}
				}
				else whole += l + "\n";
			}
		}
		file.close();
		file.open("files/" + usr + ".txt", ios::out | ios::trunc);
		file << whole;
		file.close();
		cout << "Uspesno ste prodali akciju." << endl;
	}
	catch (ActionDoesntExist e) {
		cout << e.what() << endl;
	}
}

ostream & operator<<(ostream & os, const User & u) {
	os << "Trenutno stanje na racunu: " << setprecision(10) << u.money << endl << "Transakcije: ";
	if (u.transactions.empty()) os << "ne posedujete akcije." << endl;
	else {
		os << endl;
		os << left << setw(5) << "ID" << left << setw(10) << "Akcija" << left << setw(10) << "Kolicina";
		os << left << setw(15) << "Cena kupovine" << left << setw(15) << "Trenutna cena";
		os << left << setw(25) << "Apsolutna razlika cena" << left << setw(25) << "Relativna razlika cena" << endl;
		for_each(u.transactions.begin(), u.transactions.end(), [&os](Transaction* t) { os << *t << endl; });
	}
	return os;
}