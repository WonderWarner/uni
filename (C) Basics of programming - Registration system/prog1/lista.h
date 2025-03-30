#ifndef LISTA_H_INCLUDED
#define LISTA_H_INCLUDED

#include "alapok.h"
#include <stdlib.h>

typedef struct ListHallg {
    char neptun[50+1];
    int pont;
    int jegy;
    struct ListHallg *kov;
} ListHallg;

//Uj, rendezett listaba fuzes (mi= 1: Neptun kod szerinti, 0:Pontszam szerinti)- ezt kesobb fel kell szabaditani
ListHallg *sorrendbeszur(ListHallg *kezd, Hallgato *akt, int mi);

//Adott letszamu legjobb hallgato kiirasa pontszam szerint
void tophallg(Hallgato *elso, int db);

//Adott pontszam feletti hallgatok kiirasa
void toppont(Hallgato *elso, int pont);

//Adott osztalyzattal rendelkezok fajlba irasa nev szerint
void osztalyzat(Hallgato *elso, int jegy);

//Pot ZH megirasara kotelezettek listaja
void potzhlista(Hallgato *elso);

//Pot hazi beadasara kotelezettek listaja
void pothazilista(Hallgato *elso);

//Listak almenuje
void lista(Hallgato *elso);

#endif // LISTA_H_INCLUDED
