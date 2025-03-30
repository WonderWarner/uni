#include "lista.h"

//Uj, rendezett listaba fuzes (mi= 1: Neptun kod szerinti, 0:Pontszam szerinti)- ezt kesobb fel kell szabaditani
ListHallg *sorrendbeszur(ListHallg *kezd, Hallgato *akt, int mi) {
    ListHallg *uj=(ListHallg*)malloc(sizeof(ListHallg));
    if(uj==NULL) {
        perror("Nincs eleg memoria!");
        free(uj);
        return kezd;
    }
    uj->pont=akt->eredmeny.pontszam;
    uj->jegy=akt->eredmeny.jegy;
    strcpy(uj->neptun, akt->neptun);
    uj->kov=NULL;
    ListHallg *lemarado=NULL;
    ListHallg *mozgo=kezd;
    switch(mi) {
        case 0:
            while(mozgo!=NULL&&uj->pont<mozgo->pont) {
                lemarado=mozgo;
                mozgo=mozgo->kov;
            }
            break;
        case 1:
            while(mozgo!=NULL&&strcmp(uj->neptun, mozgo->neptun)>0) {
                lemarado=mozgo;
                mozgo=mozgo->kov;
            }
            break;
    }
    if(lemarado==NULL) {
        uj->kov=kezd;
        kezd=uj;
    }
    else {
        lemarado->kov=uj;
        uj->kov=mozgo;
    }
    return kezd;
}

//Adott letszamu legjobb hallgato kiirasa pontszam szerint
void tophallg(Hallgato *elso, int db) {
    printf("Legjobb %d hallgato:\n", db);
    Hallgato *akt=elso;
    ListHallg *kezd=NULL;
    //Kiirando hallgatok pontszam szerinti listaba fuzese
    while (akt!=NULL) {
        kezd=sorrendbeszur(kezd, akt, 0);
        akt=akt->kov;
    }
    //Felszabaditas es kiiras
    int hely=1;
    int i=0;
    while(kezd!=NULL) {
        ListHallg *mozgo=kezd;
        kezd=kezd->kov;
        if (i<db) {
            printf("%d. %s, %d pont\n", hely, mozgo->neptun, mozgo->pont);
            if(mozgo->pont!=kezd->pont) hely+=1;
        }
        free(mozgo);
        i+=1;
    }
}

//Adott pontszam feletti hallgatok kiirasa
void toppont(Hallgato *elso, int pont) {
    printf("%d pont feletti hallgatok:\n", pont);
    Hallgato *akt=elso;
    ListHallg *kezd=NULL;
    //Kiirando hallgatok pontszam szerinti listaba fuzese
    while (akt!=NULL) {
        if(akt->eredmeny.pontszam>=pont) kezd=sorrendbeszur(kezd, akt, 0);
        akt=akt->kov;
    }
    //Felszabaditas es kiiras
    while(kezd!=NULL) {
        ListHallg *mozgo=kezd;
        kezd=kezd->kov;
        printf("%s, %d pont\n", mozgo->neptun, mozgo->pont);
        free(mozgo);
    }
}

//Adott osztalyzattal rendelkezok fajlba irasa
void osztalyzat(Hallgato *elso, int jegy) {
    FILE *fp;
    switch(jegy) {
        case 1:
            printf("Fajl letrehozasa elegtelen.txt neven\n");
            fp=fopen("elegtelen.txt", "w");
            break;
        case 2:
            printf("Fajl letrehozasa elegseges.txt neven\n");
            fp=fopen("elegseges.txt", "w");
            break;
        case 3:
            printf("Fajl letrehozasa kozepes.txt neven\n");
            fp=fopen("kozepes.txt", "w");
            break;
        case 4:
            printf("Fajl letrehozasa jo.txt neven\n");
            fp=fopen("jo.txt", "w");
            break;
        case 5:
            printf("Fajl letrehozasa jeles.txt neven\n");
            fp=fopen("jeles.txt", "w");
            break;
    }
    if(fp==NULL) {
        perror("Fajl megnyitasa sikertelen!");
        return;
    }
    fprintf(fp, "\"%d\" osztalyzatuak listaja\n\n", jegy);
    Hallgato *akt=elso;
    ListHallg *kezd=NULL;
    //Kiirando hallgatok nevsor szerinti listaba fuzese
    while (akt!=NULL) {
        if(akt->eredmeny.jegy==jegy) {
            kezd=sorrendbeszur(kezd, akt, 0);
        }
        akt=akt->kov;
    }
    //Felszabaditas es kiiras
    while(kezd!=NULL) {
        ListHallg *mozgo=kezd;
        kezd=kezd->kov;
        fprintf(fp, "%s, %d pont\n", mozgo->neptun, mozgo->pont);
        free(mozgo);
    }
    printf("Fajl letrehozasa sikeresen megtortent!\n");
    fclose(fp);
}

//Pot ZH megirasara kotelezettek listaja
void potzhlista(Hallgato *elso) {
    printf("Pot ZH-ra kell mennie:\n");
    Hallgato *akt=elso;
    ListHallg *kezd=NULL;
    //Kiirando hallgatok nevsor szerinti listaba fuzese
    while (akt!=NULL) {
        //NZH pont szamitasa
        int nzhpont=0;
        if(akt->eredmeny.IIZH>akt->eredmeny.IZH) nzhpont=2*akt->eredmeny.IIZH;
        else nzhpont=akt->eredmeny.IZH+akt->eredmeny.IIZH;
        if((nzhpont<40||akt->eredmeny.IIZH<20)&&akt->eredmeny.PZH==0) {
            kezd=sorrendbeszur(kezd, akt, 1);
        }
        akt=akt->kov;
    }
    //Felszabaditas es kiiras
    while(kezd!=NULL) {
        ListHallg *mozgo=kezd;
        kezd=kezd->kov;
        printf("%s\n", mozgo->neptun);
        free(mozgo);
    }
}

//Pot hazi beadasara kotelezettek listaja
void pothazilista(Hallgato *elso) {
    printf("Pot hazi beadast kell vegeznie:\n");
    Hallgato *akt=elso;
    ListHallg *kezd=NULL;
    //Kiirando hallgatok nevsor szerinti listaba fuzese
    while (akt!=NULL) {
        if(akt->eredmeny.NHF<10||!(akt->eredmeny.hazi)) {
            kezd=sorrendbeszur(kezd, akt, 1);
        }
        akt=akt->kov;
    }
    //Felszabaditas es kiiras
    while(kezd!=NULL) {
        ListHallg *mozgo=kezd;
        kezd=kezd->kov;
        printf("%s\n", mozgo->neptun);
        free(mozgo);
    }
}


//Listak almenuje
void lista(Hallgato *elso) {
    printf("Valasszon listat!\n");
    printf("\t[1] Pontszam szerinti rangsor\n");
    printf("\t[2] Osztalyzat szerinti kilistazas fajlba\n");
    printf("\t[3] Pot ZH lista\n");
    printf("\t[4] NHF potlasi lista\n");
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
                    printf("Melyik szempont szerint listazzuk?\n");
                    printf("\t[1] Hallgatok szama\n");
                    printf("\t[2] Minimum pontszam\n");
                    printf("\t[0] Vissza\n");
                    int ans2;
                    do {
                        helyes=true;
                        if(scanf("%d", &ans2)!=1) {
                            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                            perror("Ervenytelen valasz!");
                            helyes=false;
                        }
                        else {
                            getchar();
                            switch(ans2) {
                                case 1:
                                    printf("A legjobb x hallgato kilistazasa! x=");
                                    int ans3;
                                    Hallgato *szamol=elso;
                                    int letszam=0;
                                    while(szamol!=NULL) {
                                        letszam+=1;
                                        szamol=szamol->kov;
                                    }
                                    do {
                                        helyes=true;
                                        if(scanf("%d", &ans3)!=1) {
                                            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                                            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                                            perror("Ervenytelen valasz!");
                                            helyes=false;
                                        }
                                        else
                                            if(ans3<0||ans3>=letszam) {
                                                helyes=false;
                                                printf("Hibas szam!\n");
                                            }
                                    } while(!helyes);
                                    tophallg(elso, ans3);
                                    break;
                                case 2:
                                    printf("Hallgatok kilistazasa p pontszamtol! p=");
                                    int ans4;
                                    do {
                                        helyes=true;
                                        if(scanf("%d", &ans4)!=1) {
                                            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                                            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                                            perror("Ervenytelen valasz!");
                                            helyes=false;
                                        }
                                        else if(ans4>175||ans4<0) {
                                                helyes=false;
                                                printf("Hibas szam!\n");
                                            }
                                    } while(!helyes);
                                    toppont(elso, ans4);
                                    break;
                                case 0:
                                    break;
                                default:
                                    perror("Ervenytelen valasz!");
                                    helyes=false;
                            }
                        }
                    } while(!helyes);
                    break;
                case 2:
                    printf("Hanyas osztalyzatuakat listazzunk ki? (1-5): ");
                    int ans5;
                    do {
                        helyes=true;
                        if(scanf("%d", &ans5)!=1) {
                            while(!isspace(c=getchar()));      //ha karaktert kap, igy ervenytelen valasznak
                            ungetc(c, stdin);                  //veszi es nem kerul vegtelen ciklusba
                            perror("Ervenytelen valasz!");
                            helyes=false;
                        }
                        else if(ans5>5||ans5<1) {
                                helyes=false;
                                printf("Hibas szam!\n");
                            }
                    } while(!helyes);
                    osztalyzat(elso, ans5);
                    break;
                case 3:
                    potzhlista(elso);
                    break;
                case 4:
                    pothazilista(elso);
                    break;
                case 0:
                    break;
                default:
                    perror("Ervenytelen valasz!");
                    helyes=false;
            }
        }
    } while(!helyes);
}
