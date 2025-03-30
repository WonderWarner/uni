#include "szabalyosbemenet.h"

//Neptun kod szabalyossaganak vizsgalata
bool szabalyos_neptun(char *kod) {
    if(strlen(kod)!=6) {
        perror("Hibas, irja be ujra");
        return false;
    }
    for(int i=0; i<6; ++i)
        if(!isupper(kod[i])&&!isdigit(kod[i])) {
            perror("Hibas, irja be ujra");
            return false;
        }
    return true;
}

//Nev szabalyossaganak vizsgalata
bool szabalyos_nev(char *nev) {
    if(strlen(nev)>50) {
        perror("Hibas, irja be ujra");
        return false;
    }
    for(int i=0; nev[i]!='\0'; ++i)
        if(!isalpha(nev[i])&&(nev[i]!=' ')&&(nev[i]!='.')&&(nev[i]!='-')) {
            perror("Hibas, irja be ujra");
            return false;
        }
    return true;
}

//Csoportkod szabalyossaganak vizsgalata
bool szabalyos_gyakvagyea(char *csop) {
    if(strlen(csop)>5) {
        perror("Hibas, irja be ujra");
        return false;
    }
    for(int i=0; csop[i]!='\0'; ++i)
        if(!isalpha(csop[i])&&!isdigit(csop[i])&&(csop[i]!=' ')&&(csop[i]!='.')&&(csop[i]!='-')&&(csop[i]!='_')) {
            perror("Hibas, irja be ujra");
            return false;
        }
    return true;
}

//Neptun kod beolvasasa
void kodbe(char *kod) {
    bool neptunjo_e=false;
    while(!neptunjo_e) {
        printf("Adja meg a hallgato Neptun kodjat: ");
        gets(kod);
        neptunjo_e=szabalyos_neptun(kod);
    }
}

//Nev beolvasasa
void nevbe(char *nev) {
    bool nevjo_e=false;
    while(!nevjo_e) {
        nevjo_e=true;
        printf("Adja meg a nevet (max 50 karakter): ");
        gets(nev);
        nevjo_e=szabalyos_nev(nev);
    }
}

//Az oktato csoportjainak beolvasasakor ellenorzi, hogy nincs-e mar mas oktatohoz beirva a csoport
bool mascsoportja(Oktato *elso, char *csop) {
    Oktato *akt=elso;
    while(akt!=NULL) {
        for(int i=0; i<akt->mennyi; ++i)
            if(strcmp(akt->csop[i], csop)==0) {
                printf("A megadott csoportot mar mas oktatja!\n");
                return true;
            }
        akt=akt->kov;
    }
    return false;
}
