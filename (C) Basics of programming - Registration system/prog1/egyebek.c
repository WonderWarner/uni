#include "egyebek.h"

//Nyitokep
void infoc() {
    unsigned long szamok[9] = { 0U, 1931988508U, 581177634U, 581374240U, 581177632U, 581177634U, 1919159836U, 0U };
    for(int j=0; j<9; ++j) {
        for(int i=31; i>=0; --i) {
            printf("%c", ((szamok[j]>>i)&1)?'#':' ');
        }
    printf("\n");
    }
}

//Kilepes elotti rajz
void bagoly(){
    unsigned long szamok[24] = { 2097664,5244160,8915072,8687744,16998464,33554464,34080800,
                                101979184,69872144,101979184,151521352,142721168,138971408,70522384,
                                34743328,85050448,76105872,38357280,19483200,26823872,13070720,3261952,526336,520192};
    for(int j=0; j<24; ++j) {
        for(int i=31; i>=0; --i) {
            printf("%c", ((szamok[j]>>i)&1)?'#':' ');
        }
        printf("\n");
    }
}

//Uj hallgato letrehozasakor eredmeny inicializalasa 0-kkal
void eredmeny_inicializalas(Hallgato *hallg) {
    hallg->eredmeny.eamiss=0;
    hallg->eredmeny.gyakmiss=0;
    hallg->eredmeny.labmiss=0;
    for(int i=0; i<6; ++i) {
        hallg->eredmeny.kiszh[i]=0;
    }
    hallg->eredmeny.IZH=0;
    hallg->eredmeny.IZHimsc=0;
    hallg->eredmeny.IIZH=0;
    hallg->eredmeny.IIZHimsc=0;
    hallg->eredmeny.PZH=0;
    hallg->eredmeny.hazi=0;
    hallg->eredmeny.NHF=0;
    hallg->eredmeny.szorg=0;
    hallg->eredmeny.kovetelmeny=0;
    hallg->eredmeny.pontszam=0;
    hallg->eredmeny.jegy=0;
}
