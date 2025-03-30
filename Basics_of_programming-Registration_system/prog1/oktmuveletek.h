#ifndef OKTMUVELETEK_H_INCLUDED
#define OKTMUVELETEK_H_INCLUDED

#include "alapok.h"
#include <stdlib.h>

//Listaban oktato keresese nev alapjan
Oktato *keresokt(Oktato *elso, char *nev);

//Listaban oktato keresese csoport alapjan
Oktato *oktatocsopszerint(Oktato* elso, char *csop);

//Uj oktato letrehozasa a lista elejere
Oktato *ujokt(Oktato *elso);

//Oktato torlese a listabol
Oktato *torlesokt(Oktato *elso, char* nev);

//Listaban szereplo okato adatainak modositasa
void adatmodokt(Oktato *elso, Oktato *akt);

//Oktato modositasa/torlese menuopcio utani almenu
Oktato *modokt(Oktato *elso);

#endif // OKTMUVELETEK_H_INCLUDED
