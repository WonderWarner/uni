/**
 * \file main.cpp
 * Felhasználói felülettel megvalósított főprogram a mancala játék tényleges használatához
 * A felhasználó válaszát értelemszerűen adja, az angol ábécé karaktereit használja, ékezeteket nem
 */

/// @details    BME VIK PROG2 NHF
/// @author     Tömöri Péter
/// @version    1.0
/// @date       2023-05-27

#include <cstring>
#include <cmath>
#include <ctime>
#include <iomanip>
#include <fstream>
#include "memtrace.h"
#include "jatek.h"
#include "beolvasosablon.hpp"

using std::cin;
using std::cout;
using std::endl;
using std::setw;
using std::ifstream;

/// Kezdőrajz: mancala
void cim(){
    //Egy kis bitvarázslattal és shifteléssel
    unsigned long szamok[2*7+2*2] = { 0U, 2182119936U, 3324559872U, 2854789632U, 2453967360U, 2189199872U, 2189592064U, 2189591040U, 0U,
                                      0U, 1887699968U, 2168719872U, 2168719872U, 2183401728U, 2212765440U, 2215911552U, 1947705472U, 0U};
    for(int j=0; j<18; ++j) {
        for(int i=31; i>=0; --i) {
            printf("%c", ((szamok[j]>>i)&1)?'#':' ');
        }
    printf("\n");
    }
}

/// Teszt 1: Játék példány létrehozása
/// @return sikeres/sikertelen teszt
bool init_test(){
    cout<<"\n1. TESZT: mancala tabla letrehozasa\n";
    Jatek mancala;
    //Létrejön-e a játék
    if(mancala.getmenet()==0&&mancala.getcellaszam()==0) return true;
    cout<<"\nHiba az init_teszt fuggvenyben: Jatek peldanyt nem sikerult letrehozni\n";
    cout<<"Elvart menetszam: 0\nAktualis menetszam: "<<mancala.getmenet();
    cout<<"\nElvart cellaszam: 0\nAktualis cellaszam: "<<mancala.getcellaszam()<<endl;
    return false;
}

/// Tesztelést végző fõprogram
/// Meghívja az összes elkészített tesztprogramot és összegzi az eredményt
/// Mivel a cellak és players tárolók privátak, így a tagfüggvényeik is
/// Ezért a szükséges teszteseteknek a megfelelő osztályban (Jatek) hoztam létre a függvényeit
int teszt_main() {
    const int tesztek_szama=5;
    int sikeres=0;
    bool null_hiba=true;
    Jatek mancala2;
    Jatek mancala3;
    for(int i=1; i<=tesztek_szama; i++) {
        switch(i) {
            case 1:
                null_hiba=init_test();
                break;
            case 2:
                null_hiba=mancala2.add_test();
                break;
            case 3:
                null_hiba=mancala3.file_test();
                break;
            case 4:
                null_hiba=mancala3.lepes_veg_teszt();
                break;
            case 5:
                null_hiba=mancala2.okosalgo_teszt();
                break;
            default:
                cout<<"Egyeztetni kell a tesztesetek szamat a case-ek szamaval!\n";
        }
        if(null_hiba) {
            cout<<"\t"<<i<<". teszt SIKERES, johet a kovetkezo\n\n";
            sikeres++;
        }
        else cout<<"Sikertelen teszteset tortent! ("<<i<<". teszt)\nLehet, hogy emiatt a kovetkezok is sikertelenek lesznek.\n\n";
    }
    cout<<"\nTESZTEK:\nOsszes/sikeres\n"<<setw(6)<<tesztek_szama<<"/"<<sikeres;
    (sikeres==tesztek_szama)?(cout<<" :)"):(cout<<" :(");
    cout<<endl<<endl;
    return 0;
}

/// Játékot végrehajtó főprogram
/// Főmenüjében kiválasztható a játék elkezdésének módja (új/betöltés)
/// Elindítja a játékot
int main() {
    srand(time(0));
    cim();
    teszt_main();
    ///A tényleges játékot működtető főprogram
    ///A kommentezés a JPorta miatt van, mivel ennek a programrésznek szüksége van egy felhasználóra
    ///Kipróbáláshoz/játszáshoz ki kell kommentezni
/*
    ///Inícializálás és főmenü
    Jatek mancala;
    cout<< "Kerem valasszon az alabbiak kozul:\n\t1 - Uj jatek\n\t2 - Jatek betoltese\n\t3 - Kilepes\n";
    int valasz=beolv_ell(1, 3);
    //2. esethez (betöltés)
    ifstream olvas;
    char fajlnev[20+2+20+4+1];
    bool helyes=false;
    switch (valasz) {
        case 1:
            mancala.init();
            mancala.jatek();
            break;
        case 2:
            do {
                helyes=false;
                cout<<"Adja meg az elmentett jatekot tartalmazo fajl nevet, .txt vel egyutt\n";
                cin>>fajlnev;
                olvas.open(fajlnev);
                //Hibakezelés: csak létező fájlt enged megnyitni
                if(!olvas) {
                    cout<<"A fajl nem letezik :(\n";
                    helyes=true;
                    olvas.close();
                }
            } while(helyes);
            olvas.close();
            try {
                mancala.betolt(fajlnev);
            }
            ///Hibás fájlformátum kezelése (olyan fájlt akar betölteni, amit nem a program készített)
            catch(std::runtime_error) {
                cout<<"Fajlformatum hibas,\nellenorizze le a fajl helyesseget,\nmajd inditsa ujra a programot\n";
                return -1;
            }
            mancala.jatek();
            break;
        case 3:
            break;
    }
*/
    return 0;
}
