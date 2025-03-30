#include "hallgmuveletek.h"
#include "szabalyosbemenet.h"
#include "egyebek.h"

//Listaban hallgato keresese neptun kod alapjan
Hallgato* kereshallg(Hallgato *elso, char *kod) {
    Hallgato *akt=elso;
    while(akt!=NULL) {
        if(strcmp(akt->neptun, kod)==0) {
            return akt;
        }
        akt=akt->kov;
    }
    return NULL;
}

//Hallgato vegleges eredmenyenek kiszamitasa
void erdszamit(Hallgato *akt) {
    akt->eredmeny.kovetelmeny=true;
    //Legjobb 4 kiszh szamitasa
    int kiszhpont=0;
    int kzh[6];
    for(int i=0; i<6; ++i)
        kzh[i]=akt->eredmeny.kiszh[i];
    for(int i=6-1; i>0; --i)
        for(int j=0; j<i; ++j)
            if(kzh[j]<kzh[j+1]) {
                int temp=kzh[j];
                kzh[j]=kzh[j+1];
                kzh[j+1]=temp;
            }
    for(int i=0; i<4; ++i)
        kiszhpont+=kzh[i];
    //NZH pont szamitasa
    int nzhpont=0;
    if(akt->eredmeny.PZH!=0) nzhpont=2*akt->eredmeny.PZH;
    else if(akt->eredmeny.IIZH>akt->eredmeny.IZH) nzhpont=2*akt->eredmeny.IIZH;
         else nzhpont=akt->eredmeny.IZH+akt->eredmeny.IIZH;
    //NZH imsc szamitasa
    int nzhimsc=0;
    if(akt->eredmeny.IZH>=0.75*40) nzhimsc+=akt->eredmeny.IZHimsc;
    if(akt->eredmeny.IIZH>=0.75*40) nzhimsc+=akt->eredmeny.IIZHimsc;
    //Osszpontszam
    akt->eredmeny.pontszam=kiszhpont+nzhpont+akt->eredmeny.NHF+nzhimsc+akt->eredmeny.szorg;
    //Jegy
    if(akt->eredmeny.pontszam>=125) akt->eredmeny.jegy=5;
    else if(akt->eredmeny.pontszam>=110) akt->eredmeny.jegy=4;
        else if(akt->eredmeny.pontszam>=90) akt->eredmeny.jegy=3;
            else if(akt->eredmeny.pontszam>=70) akt->eredmeny.jegy=2;
                else akt->eredmeny.jegy=1;
    //Alairas engedelyezve vagy megtagadva
    if(akt->eredmeny.eamiss>4||akt->eredmeny.gyakmiss>4||akt->eredmeny.labmiss>4||kiszhpont<20||nzhpont<40||(akt->eredmeny.IIZH<20&&akt->eredmeny.PZH==0)||!akt->eredmeny.hazi||akt->eredmeny.NHF<10) {
        akt->eredmeny.kovetelmeny=false;
        akt->eredmeny.jegy=1;
    }
}

//Uj hallgato adatainak beszurasa a lista elejere
Hallgato *ujhallg(Hallgato *elso) {
    char kod[6+1];
    kodbe(kod);
    Hallgato* talalat=kereshallg(elso, kod);
    if(talalat!=NULL) {
        printf("A letrehozando uj hallgato mar szerepel a nyilvantartasban,\n");
        printf(" vagy meg nem lett torolve egy regebbi hallgato megegyezo neptun koddal.\n");
        return elso;
    }
    Hallgato *uj=(Hallgato *)malloc(sizeof(Hallgato));
    if(uj==NULL) {
        perror("Nincs eleg memoria, sikertelen muvelet.\n");
        free(uj);
        return elso;
    }
    strcpy(uj->neptun, kod);
    nevbe(uj->nev);
    //Eloadas csoport beolvasasa es ellenorzese
    bool eajo_e=false;
    while(!eajo_e) {
        printf("Adja meg az eloadascsoporjat (max 5 karakter): ");
        gets(uj->eloadas);
        eajo_e=szabalyos_gyakvagyea(uj->eloadas);
    }
    //Gyakorlat csoport beolvasasa es ellenorzese
    bool gyakjo_e=false;
    while(!gyakjo_e) {
        printf("Adja meg a gyakorlatcsoporjat (max 5 karakter): ");
        gets(uj->gyak);
        gyakjo_e=szabalyos_gyakvagyea(uj->gyak);
    }
    eredmeny_inicializalas(uj);
    uj->kov=elso;
    printf("Uj hallgato sikeresen hozzaadva!\n");
    return uj;
}

//Listaban szereplo hallgato torlese
Hallgato *torleshallg(Hallgato *elso, char *kod) {
    Hallgato *lemarado=NULL;
    Hallgato *akt=elso;
    while(akt!=NULL && strcmp(akt->neptun, kod)!=0 ) {
        lemarado=akt;
        akt=akt->kov;
    }
    if(akt==NULL)
        return elso;
    if(lemarado==NULL) {
        Hallgato *ujeleje=akt->kov;
        free(akt);
        elso=ujeleje;
        return elso;
    }
    lemarado->kov=akt->kov;
    free(akt);
    printf("Hallgato sikeresen torolve!\n");
    return elso;
}

//Listaban szereplo hallgato adatainak modositasa
void adatmodhallg(Hallgato *elso, Hallgato *akt) {
    char valasz;
    //Neptun
    printf("Szeretne megvaltoztatni a neptun kodot? [I/N]\n\t~eddigi neptun kod: %s\n", akt->neptun);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        bool letezik_e;
        char kod[6+1];
        do {
            letezik_e=true;
            kodbe(kod);
            Hallgato* talalat=kereshallg(elso, kod);
            if(talalat!=NULL) {
                printf("Nem adhato ez a Neptun kod, mert masik hallgato hasznalja!\n");
                letezik_e=false;
            }
        } while(!letezik_e);
        strcpy(akt->neptun, kod);
    }
    //Nev
    printf("Szeretne megvaltoztatni a nevet? [I/N]\n\t~eddigi nev: %s\n", akt->nev);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i')
        nevbe(akt->nev);
    //Eloadas csop
    printf("Szeretne megvaltoztatni az eloadas csoportot? [I/N]\n\t~eddigi eloadas csoport: %s\n", akt->eloadas);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        bool eajo_e=false;
        while(!eajo_e) {
            printf("Irja be az uj eloadas csoportot: ");
            gets(akt->eloadas);
            eajo_e=szabalyos_gyakvagyea(akt->eloadas);
        }
    }
    //Gyakcsop
    printf("Szeretne megvaltoztatni a gyakorlat csoportot? [I/N]\n\t~eddigi gyakorlat csoport: %s\n", akt->gyak);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        bool gyakjo_e=false;
        while(!gyakjo_e) {
            printf("Irja be az uj gyakorlat csoportot: ");
            gets(akt->gyak);
            gyakjo_e=szabalyos_gyakvagyea(akt->gyak);
        }
    }
    printf("Hallgato adatainak modositasa sikeresen megtortent!\n");
}

//Listaban szereplo hallgato eredmenyenek modositasa
void eredmenymodhallg(Hallgato *akt) {
    char valasz;
    char c;
    bool helyes=true;
    //Eloadas hianyzas
    printf("Szeretne megvaltoztatni az eloadas hianyzasok szamat? [I/N]\n\t~eddigi eloadas hianyzas: %d\n", akt->eredmeny.eamiss);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("Eloadas hianyzasok szama: ");
            if(scanf("%d", &akt->eredmeny.eamiss)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
            }
            else if(akt->eredmeny.eamiss<0||akt->eredmeny.eamiss>14) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Gyakorlat hianyzas
    printf("Szeretne megvaltoztatni a gyakorlat hianyzasok szamat? [I/N]\n\t~eddigi gyakorlat hianyzas: %d\n", akt->eredmeny.gyakmiss);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("Gyakorlat hianyzasok szama: ");
            if(scanf("%d", &akt->eredmeny.gyakmiss)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
            }
            else if(akt->eredmeny.gyakmiss<0||akt->eredmeny.gyakmiss>14) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Labor hianyzas
    printf("Szeretne megvaltoztatni a labor hianyzasok szamat? [I/N]\n\t~eddigi labor hianyzas: %d\n", akt->eredmeny.labmiss);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("Labor hianyzasok szama: ");
            if(scanf("%d", &akt->eredmeny.labmiss)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
            }
            else if(akt->eredmeny.labmiss<0||akt->eredmeny.labmiss>14) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //KisZH
    for(int i=0; i<6; ++i) {
        printf("Szeretne megvaltoztatni a %d. kisZH eredmenyet? [I/N]\n\t~eddigi %d. kisZH eredmeny: %d pont\n", i+1, i+1, akt->eredmeny.kiszh[i]);
        scanf("%c", &valasz);
        getchar();
        if(valasz=='I'||valasz=='i') {
            do {
                helyes=true;
                printf("\tUj %d. kisZH eredmeny: ", i+1);
                if(scanf("%d", &akt->eredmeny.kiszh[i])!=1) {
                    while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                    ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                    perror("Ervenytelen valasz!");
                    helyes=false;
                }
                else if(akt->eredmeny.kiszh[i]<0||akt->eredmeny.kiszh[i]>10) {
                    printf("Nem megfelelo szam, irja be ujra!\n");
                    helyes=false;
                    }
            } while(!helyes);
        getchar();
        }
    }
    //Elso ZH
    printf("Szeretne megvaltoztatni az 1ZH eredmenyet? [I/N]\n\t~eddigi 1ZH eredmeny: %d pont\n", akt->eredmeny.IZH);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj 1ZH pont: ");
            if(scanf("%d", &akt->eredmeny.IZH)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.IZH<0||akt->eredmeny.IZH>40) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Elso ZH iMSc
    printf("Szeretne megvaltoztatni az 1ZH IMSC eredmenyet? [I/N]\n\t~eddigi 1ZH IMSC eredmeny: %d pont\n", akt->eredmeny.IZHimsc);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj 1ZH IMSC pont: ");
            if(scanf("%d", &akt->eredmeny.IZHimsc)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.IZHimsc<0||akt->eredmeny.IZHimsc>10) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Masodik ZH
    printf("Szeretne megvaltoztatni az 2ZH eredmenyet? [I/N]\n\t~eddigi 2ZH eredmeny: %d pont\n", akt->eredmeny.IIZH);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj 2ZH pont: ");
            if(scanf("%d", &akt->eredmeny.IIZH)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.IIZH<0||akt->eredmeny.IIZH>40) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Masodik ZH iMSc
    printf("Szeretne megvaltoztatni az 2ZH IMSC eredmenyet? [I/N]\n\t~eddigi 2ZH IMSC eredmeny: %d pont\n", akt->eredmeny.IIZHimsc);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj 2ZH IMSC pont: ");
            if(scanf("%d", &akt->eredmeny.IIZHimsc)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.IIZHimsc<0||akt->eredmeny.IIZHimsc>10) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Pot ZH
    printf("Szeretne megvaltoztatni a pot ZH eredmenyet? [I/N]\n\t~eddigi pot ZH eredmeny: %d pont\n", akt->eredmeny.PZH);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj pot ZH pont: ");
            if(scanf("%d", &akt->eredmeny.PZH)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.PZH<0||akt->eredmeny.PZH>40) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //hazi kovetelmeny
    printf("Szeretne megvaltoztatni a hazi kovetelmeny statuszat? [I/N]\n\t~eddigi hazi kovetelmeny statusz: ");
    printf("%s\n", akt->eredmeny.hazi?"Elfogadva (1)":"Elutasitva (0)");
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            int boole;
            printf("\tNagy hazi kovetelmenyek elfogadva? [Igen=1, Nem=0]: ");
            if(scanf("%d", &boole)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(boole!=1&&boole!=0) {
                    printf("Nem megfelelo szam, irja be ujra!\n");
                    helyes=false;
                }
                else akt->eredmeny.hazi=(boole==1);
        } while(!helyes);
        getchar();
    }
    //NHF pont
    printf("Szeretne megvaltoztatni a NHF eredmenyet? [I/N]\n\t~eddigi NHF eredmeny: %d pont\n", akt->eredmeny.NHF);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj NHF pont: ");
            if(scanf("%d", &akt->eredmeny.NHF)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.NHF<0||akt->eredmeny.NHF>20) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Szorgalmi pontok
    printf("Szeretne megvaltoztatni a szorgalmi feladatok pontszamat? [I/N]\n\t~eddigi szorgalmi pontszam: %d pont\n", akt->eredmeny.szorg);
    scanf("%c", &valasz);
    getchar();
    if(valasz=='I'||valasz=='i') {
        do {
            helyes=true;
            printf("\tUj szorgalmi pontszam: ");
            if(scanf("%d", &akt->eredmeny.szorg)!=1) {
                while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                perror("Ervenytelen valasz!");
                helyes=false;
                }
            else if(akt->eredmeny.szorg<0||akt->eredmeny.szorg>15) {
                printf("Nem megfelelo szam, irja be ujra!\n");
                helyes=false;
            }
        } while(!helyes);
        getchar();
    }
    //Vegso eredmenyek kiszamitasa
    erdszamit(akt);
    printf("Hallgato eredmenyeinek modositasa sikeresen megtortent!\n");
    printf("Uj pontszam: %d pont\nUj jegy: %d\n", akt->eredmeny.pontszam, akt->eredmeny.jegy);
}

//Hallgato modositasa/torlese/eredmeny megadasa menuopcio utani almenu
Hallgato *modhallg(Hallgato *elso) {
    char kod[6+1];
    kodbe(kod);
    Hallgato* talalat=kereshallg(elso, kod);
    if(talalat==NULL) {
        printf("A hallgato nem szerepel a nyilvantartasban!\n");
        return elso;
    }
    printf("Mit szeretne tenni?\n");
    printf("\t[1] Hallgato torlese\n");
    printf("\t[2] Hallgato adatainak modositasa\n");
    printf("\t[3] Eredmenyek modositasa\n");
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
                    elso=torleshallg(elso, kod);
                    break;
                case 2:
                    adatmodhallg(elso, talalat);
                    break;
                case 3:
                    eredmenymodhallg(talalat);
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
