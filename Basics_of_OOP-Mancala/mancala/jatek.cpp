/**
 * \file jatek.cpp
 * Játék osztály függvényeinek definiálása (teszteket kivéve)
 */

#include <ctype.h>
#include <fstream>
#include <iomanip>
#include "memtrace.h"
#include "jatek.h"
#include "beolvasosablon.hpp"

using std::cin;
using std::cout;
using std::endl;
using std::string;
using std::setw;
using std::ifstream;
using std::ofstream;

/// Új játék inícializálása felhasználó által
void Jatek::init() {
    int adat[2]; //hány cella és cellánként hány golyó
    cout<< "Adja meg, hogy egy oldalon hany cella legyen\n(a jatekos bazisat leszamitva, maximum 15)\n";
    adat[0]=beolv_ell(1, 15); //beolvasosablon-nal, hibakezelés is
    cout<< "Hany golyo legyen egy cellaban (max 99)?\n";
    adat[1]=beolv_ell(1, 99); //beolvasosablon-nal, hibakezelés is
    ///A tömb első eleme a bal játékos első cellája, az utolsó eleme a jobb játekos bázisa
    ///cellák hozzáadása, értékkel feltöltése
    for(int j=0; j<2; j++) {
        for(int i=0; i<adat[0]; i++) {
            cellak.hozzaad(adat[1]);
        }
        cellak.hozzaad(0); //a bázisokban kezdetben 0 golyó van
    }
    ///Játékosok adatainak bekérése
    string nev;
    bool masodik;
    for(int i=0; i<2; i++) {
        cout<< "Adja meg a(z) "<<i+1 <<". jatekos fajtajat:\n"
            << "\t0 - ember (felhasznalo)\n" << "\t1 - \"buta\" szamitogep\n"
            << "\t2 - \"okos\" szamitogep\n" << "\t3 - co-pilot mod (okos szamitogep javaslata, de ember jatekos)\n";
        adat[0]=beolv_ell(0,3); //beolvasosablon-nal, hibakezelés is
        ///Típusnak megfelelően csak a hozzá szükséges adatok bekérése, és kért típusnak megfelelő játékos létrehozása
        switch ((pl_tipus)adat[0]) {
        case ember:
            cout<< "Kerem irja be a(z) " <<i+1<<". jatekos nevet (max 20 karakter):\n";
            nev=beolv_ell_str(20); //beolvasosablon-nal, hibakezelés is
            masodik=i;
            players.hozzaad(new Ember(!masodik, nev));  ///DinTomb dinamikusan tárolja a dinamikusan foglalt játékos pointerét
                                                        ///felszabadítás a DinTomb feladata
            break;
        case buta:
            masodik=i;
            players.hozzaad(new RandomPC(!masodik));
            if(i) nev="randompc2";
            else nev="randompc1";
            players[i]->setnev(nev);
            break;
        case okos:
            cout<< "Kerem irja be a(z) " <<i+1<<". jatekos (okospc) nehezseget (1-5):\n";
            adat[1]=beolv_ell(1, 5); //beolvasosablon-nal, hibakezelés is
            masodik=i;
            players.hozzaad(new OkosPC(!masodik, adat[1]));
            if(i) nev="okospc2";
            else nev="okospc1";
            players[i]->setnev(nev);
            break;
        case co:
            cout<< "Kerem irja be a(z) " <<i+1<<". jatekos nevet (max 20 karakter):\n";
            nev=beolv_ell_str(20); //beolvasosablon-nal, hibakezelés is
            cout<<"Adja meg a jatekost segito gep nehezseget (1-5):\n";
            adat[1]=beolv_ell(1, 5); //beolvasosablon-nal, hibakezelés is
            masodik=i;
            players.hozzaad(new CoPilot(!masodik, nev, adat[1]));
            break;
        }
    }
}

/// Elmentett játék fájlból betöltése
/// Csak akkor hívódik meg, ha a paraméterként megadott nevű fájl tényleg létezik
/// Azért lett előre bekérve és paraméterrel átadva,
///     hogy a teszteléskor használhassuk a függvényt automatikusan
/// Elmentésnek megfelelő formátum beolvasása
/// @param nev - a betöltendő fájl neve (.txt-vel)
/// @return - hibás fájlformátum esetén kivételt dob és kilép a programból
void Jatek::betolt(const char* fajlnev) {
    ifstream olvas;
    olvas.open(fajlnev);
    int menet, n;
    bool kov;
    ///Hány lépés telt el, ki következik és hány cella van összesen
    olvas>>menet >>kov >> n;
    if(olvas.fail()||menet<0||(kov!=1&&kov!=0)||n<0||n>32) {
        olvas.close();
        throw std::runtime_error("Hibas fajlformatum!");
    }
    setmenet(menet);
    setkov(kov);
    int pl_typ;
    string nev;
    int diff;
    ///Játékosok típusának, nevének és nehézségüknek beolvasása
    ///(az első játékos lett először beleírva)
    for(int i=0; i<2; i++) {
        bool masodik=i;
        olvas>>pl_typ>>nev>>diff;
        if(olvas.fail()||pl_typ<0||pl_typ>3||nev.length()>20||diff<0||diff>5) {
            olvas.close();
            throw std::runtime_error("Hibas fajlformatum!");
        }
        ///Megfelelő típusú játékos létrehozása dinamikusan (felszabadítás a DinTomb feladata)
        switch((pl_tipus)pl_typ) {
            case ember:
                players.hozzaad(new Ember(!masodik, nev));
                break;
            case buta:
                players.hozzaad(new RandomPC(!masodik));
                players[i]->setnev(nev);
                break;
            case okos:
                players.hozzaad(new OkosPC(!masodik, diff));
                players[i]->setnev(nev);
                break;
            case co:
                players.hozzaad(new CoPilot(!masodik, nev, diff));
                break;
        }
    }
    ///Cellák létrehozása, értéküknek megadása
    int cellaertek;
    for(int i=0; i<n; i++) {
        olvas>>cellaertek;
        if(olvas.fail()||cellaertek<0||cellaertek>30*99) {
            olvas.close();
            throw std::runtime_error("Hibas fajlformatum!");
        }
        cellak.hozzaad(cellaertek);
    }
    olvas.close();
}

/// Játék fájlba mentése
/// Ilyen formátumban lesz majd betöltve is később
void Jatek::mentes() {
    ///Fájlnév: "jatekos1neve"+"vs"+"jatekos2neve"+".txt"
    string fajlnev=players[0]->getnev()+"vs"+players[1]->getnev()+".txt";
    cout<<"A fajl neve: "<<fajlnev<<endl;
    ofstream ir(fajlnev); //fájl létrehozása
    ir<<menetszam<<" "<<elsokov<<" "<<cellak.size()<<endl;  //menetszám, ki következik majd, hány cella van
    ir<<players[0]->gettyp()<<" "<<players[0]->getnev()<<" "<<players[0]->getnehezseg()<<" "
      <<players[1]->gettyp()<<" "<<players[1]->getnev()<<" "<<players[1]->getnehezseg() <<endl; //játékosok adatai
    for(size_t i=0; i<cellak.size()-1; i++)
        ir<<cellak[i] <<" "; //cellaértékek sorba írása
    ir<<cellak[cellak.size()-1]<<endl;
    ir.close();
    cout<<"Jatek sikeresen elmentve :)\nA jatek kesobbi folytatasahoz jegyezze meg a jatekosok neveit=fajlnevet\n";
}

/// Játék állásának (tábla) kirajzolása
/// Megjelenés: a két játékos oldala (cellái) két oszlopként lesznek feltüntetve
/// A bal oldali oszlop az első játékosé, a jobb a másodiké
/// Az óramutató járásával ellentétesen haladva
///    az első játékos bázisa van lent a két oszlop között (alá írva az első játékos adatai)
///    a második játékos bázisa van fent a két oszlop között (felette a második játékos adatai)
/// A játékosok minden cellájához tartozik egy azonosító (első játékosnak a golyószámtól balra, másodiknak jobbra)
///    óramutató járásával ellentétesen első játékosnak (fentről lefele) A, B, C, ...
///    második játékosnak (lentről felfele) a, b, c, ...
///    15 a max cellaszám egy oldalon szóval csak latin betűk lesznek és könnyen lehet őket konvertálni oda-vissza az indexekből
/// Továbbá fel van tüntetve, hogy hány lépést tettek meg a játék kezdete óta, és hogy ki a soron következő játékos
void Jatek::kiir() const {
    cout<<"\n\tJATEK\n"
    <<"Eddigi lepesek szama: "<<menetszam<<endl<<endl
    << *players[1] <<" bazisa"<<endl
    <<"\t"<<setw(3)<<cellak[cellak.size()-1]<<endl;
    char cella1='A';
    char cella2;
    for(size_t i=0; i<(cellak.size()-2)/2; i++, cella1++) {
        cella2='a'+(cellak.size()-2)/2-i-1;
        cout<<"   " <<cella1<<": "<<setw(3)<<cellak[i]<<"   "<<setw(3)<<cellak[cellak.size()-1-1-i]<< " :"<<cella2<<endl;
    }
    cout<<"\t"<<setw(3)<<cellak[(cellak.size()/2)-1]<<endl
    << *players[0] <<" bazisa"<<endl
    <<"Kovetkezo jatekos: " << players[!elsokov]->getnev()<<endl<<endl;
}

/// Játék vége
/// @return véget ért-e a játék
bool Jatek::vege() const {
    ///A soron következő játékos oldalát kell végignézni, hogy van-e nem üres cella
    size_t hossz=(cellak.size()-2)/2;
    size_t mettol=(elsokov)?(0):(hossz+1);
    for(size_t i=mettol; i<mettol+hossz; i++) {
        if(cellak[i]!=0) return false; //ha van még megy a játék
    }
    return true; //ha nincs akkor eljut ide és a játéknak már vége
}

/// Játék vége esetén a maradék golyók bázisba rakása
/// A soron következő játékosnak biztos nem lesz maradék golyója, mert akkor nem ért volna véget
void Jatek::maradek_pakol() {
    size_t hossz=(cellak.size()-2)/2;
    int db=0;
    size_t i=0;
    for( ; i<hossz; i++) {
        db+=cellak[i];
        cellak[i]=0;
    }
    cellak[i++]+=db;
    db=0;
    for( ; i<(2*hossz)+1; i++) {
        db+=cellak[i];
        cellak[i]=0;
    }
    cellak[i]+=db;
}

/// Játék fő menetének kezelése
void Jatek::jatek() {
    int muvelet;
    ///Amíg nincs vége elvégezzük a soron következő játékos lépését
    while(!vege()) {
        kiir(); //játékállás kiírása a felhasználó számára
        ///A soron következő játékos lép
        if(elsokov) muvelet=players[0]->lepes(cellak, elsokov);
        else  muvelet=players[1]->lepes(cellak, elsokov);
        ///Felhasználó esetén (Ember vagy CoPilot) van rá esély, hogy lépés helyett
        ///Mentene
        if(muvelet==1){
            mentes(); //akkor ezt megtesszük
            return;
        }
        ///Vagy kilépne
        else if(muvelet==2) {
            cout<<"Biztos, hogy ki akar lepni? y/n\n"; //rákérdezünk azért, hogy biztos-e
            char c;
            bool hibas=true;
            do {
                cin>>c;
                if(c=='y'||c=='n') hibas=false;
                else cout<<"Hibas karakter, probalja ujra!\n"; //hibakezelés
            } while(hibas);
            if(c=='y') return;
        }
        ///Megjegyzés: exit-nél karakterenként olvas be
        ///-->ha több karaktert (stringet) ír a felhasználó, az első előforduló y/n lesz elfogadva
        else menetszam++;   //ha ezek nem voltak akkor lépés történt
    }
    ///Ide akkor kerülünk ha véget ért a játék, ekkor kirajzoljuk az állást
    kiir();
    cout<<"A jateknak vege!\nMaradekok bazisba helyezese:\n";
    ///Elvégezzük a maradékok bázisba pakolását
    maradek_pakol();
    ///Kiirjuk mégegyszer, biztos ami biztos
    kiir();
    ///És összegezzük az eredményeket, győztest hirdetünk
    size_t pont[2];
    pont[0]=cellak[(cellak.size()-2)/2];
    pont[1]=cellak[cellak.size()-1];
    cout<<"Osszesen "<<menetszam <<" lepes tortent\n"
    <<"Az elso jatekos pontszama: "<<pont[0]<<endl
    <<"A masodik jatekos pontszama: "<<pont[1]<<endl;
    cout<<"A gyoztes jatekos: ";
    if(pont[0]>pont[1]) cout<<players[0]->getnev()<<endl;
    else if(pont[0]<pont[1]) cout<<players[1]->getnev()<<endl;
    else cout<<"Dontetlen :)\n";
}
