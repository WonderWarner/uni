#include "oktmuveletek.h"
#include "szabalyosbemenet.h"

//Listaban oktato keresese nev alapjan
Oktato *keresokt(Oktato *elso, char *nev) {
    Oktato *akt=elso;
    while(akt!=NULL) {
        if(strcmp(akt->nev, nev)==0) {
            return akt;
        }
        akt=akt->kov;
    }
    return NULL;
}

//Listaban oktato keresese csoport alapjan
Oktato *oktatocsopszerint(Oktato* elso, char *csop) {
    Oktato *akt=elso;
    while(akt!=NULL) {
        for(int i=0; i<akt->mennyi; ++i) {
            if(strcmp(akt->csop[i], csop)==0) {
                return akt;
            }
        }
        akt=akt->kov;
    }
    return NULL;
}

//Uj oktato letrehozasa a lista elejere
Oktato *ujokt(Oktato *elso) {
    char nev[50+1];
    nevbe(nev);
    Oktato *talalat=keresokt(elso, nev);
    if(talalat!=NULL) {
        printf("A letrehozando uj oktato mar szerepel a nyilvantartasban,\n");
        printf(" vagy meg nem lett torolve egy regebbi oktato megegyezo nevvel.\n");
        return elso;
    }
    Oktato *uj=(Oktato *)malloc(sizeof(Oktato));
    if(uj==NULL) {
        perror("Nincs eleg memoria, sikertelen muvelet.\n");
        free(uj);
        return elso;
    }
    strcpy(uj->nev, nev);
    //Eloadas/gyakorlat mennyisegenek beolvasasa es ellenorzese
    bool mennyjo_e=false;
    while(!mennyjo_e) {
        mennyjo_e=true;
        printf("Hany csoportja van az oktatonak? (min 1, max 5): ");
        if(scanf("%d", &(uj->mennyi))!=1) {
            char c;
            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
            perror("Ervenytelen valasz!");
            mennyjo_e=false;
        }
        else if(uj->mennyi<1||uj->mennyi>5) {
            mennyjo_e=false;
            perror("Hibas, irja be ujra");
        }
    }
    printf("%d\n", uj->mennyi);
    getchar();
    //Csoportok beolvasasa es ellenorzese
    for(int i=0; i<uj->mennyi; ++i) {
        bool csopjo_e=false;
        while(!csopjo_e) {
            csopjo_e=true;
            printf("Adja meg a(z) %d. csoporjat (max 5 karakter): ", i+1);
            gets(uj->csop[i]);
            csopjo_e=(szabalyos_gyakvagyea(uj->csop[i])&&!mascsoportja(elso, uj->csop[i]));
        }
    }
    uj->kov=elso;
    printf("Uj oktato sikeresen hozzaadva!\n");
    return uj;
}

//Oktato torlese a listabol
Oktato *torlesokt(Oktato *elso, char* nev) {
    Oktato *lemarado=NULL;
    Oktato *akt=elso;
    while(akt!=NULL && strcmp(akt->nev, nev)!=0 ) {
        lemarado=akt;
        akt=akt->kov;
    }
    if(akt==NULL)
        return elso;
    if(lemarado==NULL) {
        Oktato *ujeleje=akt->kov;
        free(akt);
        elso=ujeleje;
        return elso;
    }
    lemarado->kov=akt->kov;
    free(akt);
    return elso;
}

//Listaban szereplo okato adatainak modositasa
void adatmodokt(Oktato *elso, Oktato *akt) {
    char valasz;
    //Nev
    printf("Szeretne megvaltoztatni a nevet? [I/N]\n\t~eddigi nev: %s\n", akt->nev);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        bool letezik_e;
        char nev[50+1];
        do {
            letezik_e=true;
            nevbe(nev);
            Oktato* talalat=keresokt(elso, nev);
            if(talalat!=NULL) {
                printf("Nem adhato ez a nev, mert masik oktato hasznalja!\n");
                letezik_e=false;
            }
        } while(!letezik_e);
        strcpy(akt->nev, nev);
    }
    int eredetimennyi=akt->mennyi;
    printf("%d\n", eredetimennyi);
    //Mennyi
    printf("Szeretne megvaltoztatni az oktato csoportjainak szamat? [I/N]\n\t~eddigi csoportok: %d db\n", akt->mennyi);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        bool helyes;
        char c;
        do {
            helyes=true;
            printf("Csoportok szama (db): ");
            if(scanf("%d", &akt->mennyi)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
            }
            else if(akt->mennyi<0||akt->mennyi>5) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Csoportok
    for(int i=0; i<akt->mennyi; ++i) {
        printf("Szeretne megvaltoztatni a %d. csoportot? [I/N]\n\t~eddigi %d. csoportnev: %s\n", i+1, i+1, i+1>eredetimennyi?"Nem volt ennyi csoportja":akt->csop[i]);
        scanf("%c", &valasz);
        getchar();
        if(valasz=='I'||valasz=='i') {
            bool csopjo_e=false;
            char ujcsopnev[5+1];
            while(!csopjo_e) {
                csopjo_e=true;
                printf("\tUj %d. csoportnev: ", i+1);
                gets(ujcsopnev);
                csopjo_e=(szabalyos_gyakvagyea(ujcsopnev)&&!mascsoportja(elso, ujcsopnev));
            }
            strcpy(akt->csop[i], ujcsopnev);
        }
    }
    printf("Oktato adatainak modositasa sikeresen megtortent!\n");
}

//Oktato modositasa/torlese menuopcio utani almenu
Oktato *modokt(Oktato *elso) {
    char nev[50+1];
    nevbe(nev);
    Oktato* talalat=keresokt(elso, nev);
    if(talalat==NULL) {
        printf("Az oktato nem szerepel a nyilvantartasban!\n");
        return elso;
    }
    printf("Mit szeretne tenni?\n");
    printf("\t[1] Oktato torlese\n");
    printf("\t[2] Oktato adatainak modositasa\n");
    printf("\t[0] Vissza\n");
    int ans;
    char c;
    bool helyes=true;
    do {
        helyes=true;
        if(scanf("%d", &ans)!=1) {
            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
            perror("Ervenytelen valasz!");
            helyes=false;
        }
        else {
            getchar();
            switch(ans) {
                case 1:
                    elso=torlesokt(elso, nev);
                    break;
                case 2:
                    adatmodokt(elso, talalat);
                    break;
                case 0:
                    break;
                default:
                    perror("Ervenytelen valasz!");
                    helyes=false;
            }
        }
    } while(!helyes);
    return elso;
}
