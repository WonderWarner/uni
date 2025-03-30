#ifndef SZABALYOSBEMENET_H_INCLUDED
#define SZABALYOSBEMENET_H_INCLUDED

#include "alapok.h"
#include <ctype.h>

//Neptun kod szabalyossaganak vizsgalata
bool szabalyos_neptun(char *kod);

//Nev szabalyossaganak vizsgalata
bool szabalyos_nev(char *nev);

//Csoportkod szabalyossaganak vizsgalata
bool szabalyos_gyakvagyea(char *csop);

//Neptun kod beolvasasa
void kodbe(char *kod);

//Nev beolvasasa
void nevbe(char *nev);

//Az oktato csoportjainak beolvasasakor ellenorzi, hogy nincs-e mar mas oktatohoz beirva a csoport
bool mascsoportja(Oktato *elso, char *csop);


#endif // SZABALYOSBEMENET_H_INCLUDED
