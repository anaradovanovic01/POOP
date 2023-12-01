#ifndef _exceptions_h
#define _exceptions_h
#include <exception>
using namespace std;

class FileDoesntExist : public exception {
public: FileDoesntExist() : exception("Podaci o korisniku nisu pronadjeni.") {}
};

class CantOpenJSONFile : public exception {
public: CantOpenJSONFile() : exception("Problem pri otvaranju .json fajla u kome se cuvaju podaci o akciji.") {}
};

class CouldntRemoveFile : public exception {
public: CouldntRemoveFile() : exception("Neuspelo uklanjanje pomocnog fajla za parsiranje.") {}
};

class IndexOutOfReach : public exception {
public: IndexOutOfReach() : exception("Indeks nije u validnom opsegu.") {}
};

class NotEnoughMoney : public exception {
public: NotEnoughMoney() : exception("Nemate dovoljno novca na racunu.") {}
};

class ActionDoesntExist : public exception {
public: ActionDoesntExist() : exception("Ne posedujete akciju u dovoljnoj kolicini koju pokusavate da prodate.") {}
};

class CouldntFindInfoAboutStock : public exception {
public: CouldntFindInfoAboutStock() : exception("Uneli ste nevalidne podatke o akciji.") {}
};

#endif