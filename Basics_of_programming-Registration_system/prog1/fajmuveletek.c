#include "fajlmuveletek.h"
#include "szabalyosbemenet.h"
#include "hallgmuveletek.h"
#include "oktmuveletek.h"

//A hallgato.txt fajl beolvasasa a hallgato listaba
Hallgato *beolvashallg() {
    FILE *fp;
    fp=fopen("hallgato.txt", "r");
    if(fp==NULL) {
        perror("File megnyitasa sikertelen");
        return NULL;
    }
    Hallgato *eleje=NULL;
    Hallgato *akt=NULL;
    char str[70+1];
    while(fgets(str, 70+1, fp)!=NULL) {
        Hallgato *hallg;
        hallg=(Hallgato*)malloc(sizeof(Hallgato));
        if(hallg==NULL) {
            perror("Nincs eleg memoria a hallgatok adatainak mentesehez.\n");
            return NULL;
        }
        int indsor=0;
        int indadat=0;
        while(str[indsor]!=';') {
            hallg->neptun[indadat++]=str[indsor++];
        }
        hallg->neptun[indadat]='\0';
        indsor+=1;
        indadat=0;
        while(str[indsor]!=';') {
            hallg->nev[indadat++]=str[indsor++];
        }
        hallg->nev[indadat]='\0';
        indsor+=1;
        indadat=0;
        while(str[indsor]!=';') {
            hallg->eloadas[indadat++]=str[indsor++];
        }
        hallg->eloadas[indadat]='\0';
        indsor+=1;
        indadat=0;
        while(str[indsor]!='\n') {
            hallg->gyak[indadat++]=str[indsor++];
        }
        hallg->gyak[indadat]='\0';
        hallg->kov=NULL;
        if(eleje==NULL) {
            eleje=hallg;
            akt=hallg;
        }
        else {
            akt->kov=hallg;
            akt=hallg;
        }
    }
    fclose(fp);
    return eleje;
}

//Az oktato.txt fajl beolvasasa az oktato listaba
Oktato *beolvasokt() {
    FILE *fp;
    fp=fopen("oktato.txt", "r");
    if(fp==NULL) {
        perror("File megnyitasa sikertelen");
        return NULL;
    }
    Oktato *eleje=NULL;
    Oktato *akt=NULL;
    char str[83+1];
    while(fgets(str, 83+1, fp)!=NULL) {
        Oktato *okt;
        okt=(Oktato*)malloc(sizeof(Oktato));
        if(okt==NULL) {
            perror("Nincs eleg memoria az oktatok adatainak mentesehez.\n");
            return NULL;
        }
        int indsor=0;
        int indadat=0;
        while(str[indsor]!=';') {
            okt->nev[indadat++]=str[indsor++];
        }
        okt->nev[indadat]='\0';
        indsor+=1;
        while(str[indsor]!=';') {
            okt->mennyi=str[indsor++]-'0';
        }
        indsor+=1;
        for(int i=0; i<(okt->mennyi)-1; ++i) {
            indadat=0;
            while(str[indsor]!=';') {
                okt->csop[i][indadat++]=str[indsor++];
            }
            okt->csop[i][indadat]='\0';
            indsor+=1;
        }
        indadat=0;
        while(str[indsor]!='\n') {
                okt->csop[(okt->mennyi)-1][indadat++]=str[indsor++];
            }
        okt->csop[(okt->mennyi)-1][indadat]='\0';
        okt->kov=NULL;
        if(eleje==NULL) {
            eleje=okt;
            akt=okt;
        }
        else {
            akt->kov=okt;
            akt=okt;
        }
    }
    fclose(fp);
    return eleje;
}

//Az eredmeny.txt fajl beolvasasa a hallgato listaba
bool beolvaseredmeny(Hallgato *hallg) {
    FILE* fp;
    fp=fopen("eredmeny.txt", "r");
    if(fp==NULL) {
        perror("File megnyitasa sikertelen");
        return false;
    }
    char neptun[6+1];
    while(fscanf(fp, "%s", neptun)==1) {
        Hallgato* akt=hallg;
        while(strcmp(akt->neptun, neptun)!=0) {
            akt=akt->kov;
        }
        fscanf(fp, "%d", &(akt->eredmeny.eamiss));
        fscanf(fp, "%d", &(akt->eredmeny.gyakmiss));
        fscanf(fp, "%d", &(akt->eredmeny.labmiss));
        for(int i=0; i<6; ++i) {
            fscanf(fp, "%d", &(akt->eredmeny.kiszh[i]));
        }
        fscanf(fp, "%d", &(akt->eredmeny.IZH));
        fscanf(fp, "%d", &(akt->eredmeny.IZHimsc));
        fscanf(fp, "%d", &(akt->eredmeny.IIZH));
        fscanf(fp, "%d", &(akt->eredmeny.IIZHimsc));
        fscanf(fp, "%d", &(akt->eredmeny.PZH));
        int boole;
        fscanf(fp, "%d", &boole);
        akt->eredmeny.hazi=(boole==1);
        fscanf(fp, "%d", &(akt->eredmeny.NHF));
        fscanf(fp, "%d", &(akt->eredmeny.szorg));
        fscanf(fp, "%d", &boole);
        akt->eredmeny.kovetelmeny=(boole==1);
        fscanf(fp, "%d", &(akt->eredmeny.pontszam));
        fscanf(fp, "%d", &(akt->eredmeny.jegy));
    }
    fclose(fp);
    return true;
}

//Hallgato lista fajlba mentese
void menteshallg(Hallgato *hallg) {
    FILE *fp1, *fp2;
    fp1=fopen("hallgato.txt", "w");
    if(fp1==NULL) {
        perror("File megnyitasa sikertelen");
        while(hallg!=NULL) {
            Hallgato *akt=hallg;
            hallg=hallg->kov;
            free(akt);
        }
        return;
    }
    fp2=fopen("eredmeny.txt", "w");
    if(fp2==NULL) {
        perror("File megnyitasa sikertelen");
        while(hallg!=NULL) {
            Hallgato *akt=hallg;
            hallg=hallg->kov;
            free(akt);
        }
        return;
    }
    while(hallg!=NULL) {
        fprintf(fp1, "%s;%s;%s;%s\n", hallg->neptun, hallg->nev, hallg->eloadas, hallg->gyak);
        fprintf(fp2, "%s %d %d %d ", hallg->neptun, hallg->eredmeny.eamiss, hallg->eredmeny.gyakmiss, hallg->eredmeny.labmiss);
        for(int i=0; i<6; ++i) {
            fprintf(fp2, "%d ", hallg->eredmeny.kiszh[i]);
        }
        fprintf(fp2, "%d %d %d %d ", hallg->eredmeny.IZH, hallg->eredmeny.IZHimsc, hallg->eredmeny.IIZH, hallg->eredmeny.IIZHimsc);
        fprintf(fp2, "%d %d %d %d ", hallg->eredmeny.PZH, hallg->eredmeny.hazi, hallg->eredmeny.NHF, hallg->eredmeny.szorg);
        fprintf(fp2, "%d %d %d\n", hallg->eredmeny.kovetelmeny, hallg->eredmeny.pontszam, hallg->eredmeny.jegy);
        Hallgato *akt=hallg;
        hallg=hallg->kov;
        free(akt);
    }
    fclose(fp1);
    fclose(fp2);
}

//Okato lista fajlba mentese
void mentesokt(Oktato *okt) {
    FILE *fp;
    fp=fopen("oktato.txt", "w");
    if(fp==NULL) {
        perror("File megnyitasa sikertelen");
        while(okt!=NULL) {
            Oktato *akt=okt;
            okt=okt->kov;
            free(akt);
        }
        return;
    }
    while(okt!=NULL) {
        fprintf(fp, "%s;%d", okt->nev, okt->mennyi);
        for(int i=0; i<(okt->mennyi); ++i) {
            fprintf(fp, ";%s", okt->csop[i]);
        }
        fprintf(fp, "\n");
        Oktato *akt=okt;
        okt=okt->kov;
        free(akt);
    }
    fclose(fp);
}

//Hallgato adatainak fajlba mentese
void lekerhallg(Hallgato *elso, Oktato *oktlista) {
    char kod[6+1];
    kodbe(kod);
    Hallgato* akt=kereshallg(elso, kod);
    if(akt==NULL) {
        printf("A hallgato nem szerepel a nyilvantartasban!\n");
        return;
    }
    printf("Hallgato neve: %s\nPontszam: %d pont\nJegy: %d\n", akt->nev, akt->eredmeny.pontszam, akt->eredmeny.jegy);
    FILE *fp;
    char fajlnev[6+4+1];
    strcpy(fajlnev, akt->neptun);
    strcat(fajlnev, ".txt");
    fajlnev[6+4]='\0';
    fp=fopen(fajlnev, "w");
    if(fp==NULL) {
        perror("Uj fajl letrehozasa sikertelen!\n");
        return;
    }
    fprintf(fp ,"NEPTUN: %s\nNEV: %s\n", akt->neptun, akt->nev);
    Oktato *eloado=oktatocsopszerint(oktlista, akt->eloadas);
    if(eloado==NULL) fprintf(fp ,"Eloadascsoport: %s; eloado: %s\n", akt->eloadas, "Nincs bejegyezve");
    else fprintf(fp ,"Eloadascsoport: %s; eloado: %s\n", akt->eloadas, eloado->nev);
    Oktato *gyakvez=oktatocsopszerint(oktlista, akt->gyak);
    if(gyakvez==NULL) fprintf(fp ,"Gyakorlatcsoport: %s; gyakorlatvezeto: %s\n", akt->gyak, "Nincs bejegyezve");
    else fprintf(fp ,"Gyakorlatcsoport: %s; gyakorlatvezeto: %s\n", akt->gyak, gyakvez->nev);
    fprintf(fp, "\nHianyzasok: eloadas %d db; gyakorlat %d db; labor %d db\n", akt->eredmeny.eamiss, akt->eredmeny.gyakmiss, akt->eredmeny.labmiss);
    fprintf(fp, "Kis ZH eredmenyek: ");
    for(int i=0; i<5; ++i) {
        fprintf(fp, "%d.: %d pont; ", i+1, akt->eredmeny.kiszh[i]);
    }
    fprintf(fp, "6.: %d pont\n", akt->eredmeny.kiszh[5]);
    fprintf(fp, "1. ZH: %d pont IMSC: %d pont; 2. ZH: %d pont IMSC: %d pont\n", akt->eredmeny.IZH, akt->eredmeny.IZHimsc, akt->eredmeny.IIZH, akt->eredmeny.IIZHimsc);
    fprintf(fp, "Pót ZH: %d pont; szorgalmi pontok: %d pont\n", akt->eredmeny.PZH, akt->eredmeny.szorg);
    fprintf(fp, "Nagy hazi kovetelmeny: %s; Pontszam: %d pont\n", ((akt->eredmeny.hazi==1)?"Elfogadva":"Elutasitva"), akt->eredmeny.NHF);
    fprintf(fp, "\nAlairas: %s\n", ((akt->eredmeny.kovetelmeny==1)?"Megszerezve":"Megtagadva"));
    fprintf(fp, "Osszesitett pontszam: %d pont\nErdemjegy: %d\n", akt->eredmeny.pontszam, akt->eredmeny.jegy);
    int imsc=0;
    if(akt->eredmeny.kovetelmeny==1&&akt->eredmeny.pontszam>=125) imsc=(akt->eredmeny.pontszam)-125;
    if(imsc>35) imsc=35;
    fprintf(fp, "\tiMSc pontok: %d pont\n", imsc);
    fclose(fp);
    printf("A hallgato adatainak fajlba mentese sikeresen megtortent!\n");
}

//Oktato adatainak fajlba mentese
void lekerokt(Oktato *elso, Hallgato *hallglista) {
    //Nev beolvasasa es megkeresese
    char nev[50+1];
    nevbe(nev);
    Oktato* akt=keresokt(elso, nev);
    if(akt==NULL) {
        printf("Az oktato nem szerepel a nyilvantartasban!\n");
        return;
    }
    printf("Oktato neve: %s\nCsoportjai:\n", akt->nev);
    for(int i=0; i<akt->mennyi; ++i)
        printf("\t%s\n", akt->csop[i]);
    //Fajlnev generalasa
    int nevhossz=strlen(akt->nev);
    char fajlnev[nevhossz+4+1];
    strcpy(fajlnev, akt->nev);
    strcat(fajlnev, ".txt");
    fajlnev[nevhossz+4]='\0';
    FILE *fp;
    fp=fopen(fajlnev, "w");
    if(fp==NULL) {
        perror("Uj fajl letrehozasa sikertelen!\n");
        return;
    }
    fprintf(fp ,"NEV: %s\n\nCsoportok es hallgatoik:\n", akt->nev);
    double osszpont=0;
    double osszjegy=0;
    double osszdb=0;
    for(int i=0; i<akt->mennyi; ++i) {
        double csoppont=0;
        double csopjegy=0;
        double csopdb=0;
        fprintf(fp, "%d.: %s\nHallgatok:\n", i+1, akt->csop[i]);
        Hallgato* hallg=hallglista;
        while(hallg!=NULL) {
            if(strcmp(akt->csop[i], hallg->eloadas)==0||strcmp(akt->csop[i], hallg->gyak)==0) {
                fprintf(fp, "\t%s, %s\n", hallg->neptun, hallg->nev);
                csopdb+=1;
                csoppont+=hallg->eredmeny.pontszam;
                csopjegy+=hallg->eredmeny.jegy;
            }
            hallg=hallg->kov;
        }
        osszpont+=csoppont;
        osszjegy+=csopjegy;
        osszdb+=csopdb;
        if(csopdb!=0) fprintf(fp, "A csoport hallgatóinak atlaga (pontszam, jegy): %.2lf, %.2lf\n\n", csoppont/csopdb, csopjegy/csopdb);
        else fprintf(fp, "\tNincs a csoportban hallgato\n\n");
    }
    fprintf(fp, "Az oktato csoportjainak osszatlaga:\n");
    fprintf(fp, "Atlag pontszam: %.2lf pont\nAtlag jegy: %.2lf", osszpont/osszdb, osszjegy/osszdb);
    fclose(fp);
    printf("Az oktato adatainak fajlba mentese sikeresen megtortent!\n");
}

