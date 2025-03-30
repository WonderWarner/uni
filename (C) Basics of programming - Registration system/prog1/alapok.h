#ifndef ALAPOK_H_INCLUDED
#define ALAPOK_H_INCLUDED

#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include "debugmalloc.h"

typedef struct Eredmenyek {     //maxadat=65
    int eamiss;
    int gyakmiss;
    int labmiss;
    int kiszh[6];
    int IZH;
    int IZHimsc;
    int IIZH;
    int IIZHimsc;
    int PZH;
    bool hazi;
    int NHF;
    int szorg;
    bool kovetelmeny;
    int pontszam;
    int jegy;
} Eredmenyek;

typedef struct Hallgato {       //maxadat=70
    char neptun[6+1];
    char nev[50+1];
    char gyak[5+1];
    char eloadas[5+1];
    Eredmenyek eredmeny;
    struct Hallgato *kov;
} Hallgato;
typedef struct Oktato {         //maxadat=83
    char nev[50+1];
    int mennyi;
    char csop[5][5+1];     //min 1 max 5 csop
    struct Oktato *kov;
} Oktato;

#endif // ALAPOK_H_INCLUDED
