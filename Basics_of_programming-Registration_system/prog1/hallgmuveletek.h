#ifndef HALLGMUVELETEK_H_INCLUDED
#define HALLGMUVELETEK_H_INCLUDED

#include "alapok.h"
#include <stdlib.h>

//Listaban hallgato keresese neptun kod alapjan
Hallgato* kereshallg(Hallgato *elso, char *kod);

//Hallgato vegleges eredmenyenek kiszamitasa
void erdszamit(Hallgato *akt);

//Uj hallgato adatainak beszurasa a lista elejere
Hallgato *ujhallg(Hallgato *elso);

//Listaban szereplo hallgato torlese
Hallgato *torleshallg(Hallgato *elso, char *kod);

//Listaban szereplo hallgato adatainak modositasa
void adatmodhallg(Hallgato *elso, Hallgato *akt);

//Listaban szereplo hallgato eredmenyenek modositasa
void eredmenymodhallg(Hallgato *akt);

//Hallgato modositasa/torlese/eredmeny megadasa menuopcio utani almenu
Hallgato *modhallg(Hallgato *elso);

#endif // HALLGMUVELETEK_H_INCLUDED
