#include "alapok.h"
#include "egyebek.h"
#include "hallgmuveletek.h"
#include "oktmuveletek.h"
#include "fajlmuveletek.h"
#include "lista.h"
#include "diagram.h"


int main(int argc, char *argv[]) {
    //Nyitokep
    infoc();
    //Hallgatolista letrehozasa
    Hallgato *hallglista=beolvashallg();
    if(hallglista==NULL) {
        menteshallg(hallglista);
        return -1;
    }
    //Oktatolista letrehozasa
    Oktato *oktlista=beolvasokt();
    if(oktlista==NULL) {
        menteshallg(hallglista);
        mentesokt(oktlista);
        return -1;
    }
    //Eredmenyek beolvasasa a meglevo hallgatolistahoz
    bool eredmenybeolvas_sikeres=beolvaseredmeny(hallglista);
    if(!eredmenybeolvas_sikeres) {
        menteshallg(hallglista);
        mentesokt(oktlista);
        return -1;
    }
//MENU
    bool kilepes=false;
    while(!kilepes) {
        printf("\nProg I. nyilvantartas ~ Kerem valasszon az alabbiak kozul:\n");
        printf("\t[1]\tUj hallgato letrehozasa\n");
        printf("\t[2]\tMeglevo hallgato adatainak modositasa, torlese, eredmeny beirasa\n");    //Csak meglevo hallgatonak lehet eredmenyt beirni
        printf("\t[3]\tHallgato adatainak lekerese, fajlba mentese\n");
        printf("\t[4]\tUj oktato letrehozasa\n");                                                //Nem lehet ket azonos nevu tanar
        printf("\t[5]\tMeglevo oktato adatainak modositasa, torlese\n");
        printf("\t[6]\tOktato adatainak lekerese, fajlba mentese\n");
        printf("\t[7]\tLista megjelenitese\n");
        printf("\t[8]\tStatisztika megjelenitese (diagram az erdemjegyek eloszlasarol)\n");
        printf("\t[0]\tKilepes\n");
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
                        hallglista=ujhallg(hallglista);
                        break;
                    case 2:
                        hallglista=modhallg(hallglista);
                        break;
                    case 3:
                        lekerhallg(hallglista, oktlista);
                        break;
                    case 4:
                        oktlista=ujokt(oktlista);
                        break;
                    case 5:
                        oktlista=modokt(oktlista);
                        break;
                    case 6:
                        lekerokt(oktlista, hallglista);
                        break;
                    case 7:
                        lista(hallglista);
                        break;
                    case 8:
                        diagram(hallglista);
                        break;
                    case 0:
                        bagoly();
                        printf("Koszonjuk hogy programunkat hasznalta!\n");
                        kilepes=true;
                        break;
                    default:
                        perror("Ervenytelen valasz!");
                        helyes=false;
                }
            }
        } while(!helyes);
    }
    menteshallg(hallglista);
    mentesokt(oktlista);
    return 0;
}
