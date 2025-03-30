#ifndef FAJLMUVELETEK_H_INCLUDED
#define FAJLMUVELETEK_H_INCLUDED

#include "alapok.h"
#include <stdlib.h>

//A hallgato.txt fajl beolvasasa a hallgato listaba
Hallgato *beolvashallg();

//Az oktato.txt fajl beolvasasa az oktato listaba
Oktato *beolvasokt();

//Az eredmeny.txt fajl beolvasasa a hallgato listaba
bool beolvaseredmeny(Hallgato *hallg);

//Hallgato lista fajlba mentese
void menteshallg(Hallgato *hallg);

//Okato lista fajlba mentese
void mentesokt(Oktato *okt);

//Hallgato adatainak fajlba mentese
void lekerhallg(Hallgato *elso, Oktato *oktlista);

//Oktato adatainak fajlba mentese
void lekerokt(Oktato *elso, Hallgato *hallglista);

#endif // FAJLMUVELETEK_H_INCLUDED
