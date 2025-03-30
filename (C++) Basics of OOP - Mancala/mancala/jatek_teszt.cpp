/**
 * \file jatek_teszt.cpp
 * A Játék osztály teszteléshez szükséges függvényeinek megvalósítása
 */

#include <fstream>
#include "memtrace.h"
#include "jatek.h"

using namespace std;

/// Teszt 2: játék inícializálása, adatokkal feltöltése
/// @return sikeres/sikertelen teszt
bool Jatek::add_test() {
    cout<<"2. TESZT: mancala tabla feltoltese adatokkal\n";
    ///DinTomb: cellák hozzáadásának tesztelése
    //6 cella plusz bázis játékosonként, 4 golyó cellánként (kivéve a bázist)
    int cella_ell[8]={3, 3, 3, 0, 3, 3, 3, 0};
    for(int i=0; i<8; i++) {
        cellak.hozzaad(cella_ell[i]);
    }
    ///Méret jó lett-e
    if(cellak.size()!=8) {
        cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
            <<"\tDinTomb mukodese rossz: cellak hozzaadasa sikertelen\n"
            <<"\tElvart cellaszam: 8\n\tAktualis cellaszam: "<<cellak.size()<<endl
            <<"\tLehetseges hibak: size fv vagy hozzaad fv\n";
        return false;
    }
    ///Értékek jók-e
    for(int i=0; i<8; i++) {
        if(cellak[i]!=cella_ell[i]) {
            cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
                <<"\tDinTomb mukodese rossz: hozzaadott cellak erteke hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(int i=0; i<8; i++) {
                cout<<cella_ell[i]<<" ";
            }
            cout<<"\n\t\tTeljes kapott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: hozzaad fv., index operator, size fv\n";
            return false;
        }
    }
    ///Túlindexelés esetén kivételkezelés
    ///Elég csak a tesztprogramban, mert a játék során minden inputra történik ellenőrzés
    ///maga a program meg jól lett megírva és nem indexel túl
    try {
        cellak[-70]=10;
    }
    catch (std::out_of_range) {
        cout<<"\tTulindexeles detektalva -> jo mukodes\n";
    }
    ///DinTomb: játékosok hozzáadásának tesztelése
    bool elsok[3]={true, false, true};
    string nevek[3]={"okospc", "okospc", "Teszt Elek"};
    int nehezseg[3]={2, 1, 0};
    pl_tipus typek[3]={okos, okos, ember};
    players.hozzaad(new OkosPC(elsok[0], nehezseg[0]));
    players.hozzaad(new OkosPC(elsok[1], nehezseg[1]));
    players.hozzaad(new Ember(elsok[2], nevek[2]));
    ///Méret jó lett-e
    if(players.size()!=3) {
        cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
            <<"\tDinTomb mukodese rossz: Jatekosok hozzaadasa sikertelen\n"
            <<"\tElvart jatekosszam: 3\n\tAktualis jatekosszam: "<<players.size()<<endl
            <<"\tLehetseges hibak: size fv vagy DinTomb Jatekos*-ra specializalt fv-ei\n";
        return false;
    }
    ///Értékek jók-e
    for(int i=0; i<3; i++) {
        //Hanyadik játékos
        if(elsok[i]!=players[i]->getelso()) {
            cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
                <<"\tDinTomb mukodese rossz: Jatekos sorszama hibas\n"
                <<"\tElvart sorszam: "<<elsok[i]+1 <<"\n\tAktualis sorszam: "<<players[i]->getelso()+1<<endl
                <<"\tLehetseges hibak: konstruktor, getter vagy DinTomb Jatekos*-ra specializalt fv-ei\n";
            return false;
        }
        //Név
        if(nevek[i]!=players[i]->getnev()) {
            cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
                <<"\tDinTomb mukodese rossz: Jatekos neve hibas\n"
                <<"\tElvart nev: "<<nevek[i] <<"\n\tAktualis nev: "<<players[i]->getnev()<<endl
                <<"\tLehetseges hibak: konstruktor, getter vagy DinTomb Jatekos*-ra specializalt fv-ei\n";
            return false;
        }
        //Nehézség
        if(nehezseg[i]!=players[i]->getnehezseg()) {
            cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
                <<"\tDinTomb mukodese rossz: Jatekos nehezsege hibas\n"
                <<"\tElvart nehezseg: "<<nehezseg[i] <<"\n\tAktualis nehezseg: "<<players[i]->getnehezseg()<<endl
                <<"\tLehetseges hibak: konstruktor, getter vagy DinTomb Jatekos*-ra specializalt fv-ei\n";
            return false;
        }
        //Típus
        if(typek[i]!=players[i]->gettyp()) {
            cout<<"Hiba a jatek peldany add_test tagfuggvenyeben:\n"
                <<"\tDinTomb mukodese rossz: Jatekos tipusa hibas\n"
                <<"\tElvart tipus sorszama: "<<typek[i] <<"\n\tAktualis tipus sorszama: "<<players[i]->gettyp()<<endl
                <<"\tLehetseges hibak: konstruktor, getter vagy DinTomb Jatekos*-ra specializalt fv-ei\n";
            return false;
        }
    }
    ///Ha minden sikerült megnézhetjük a kiíratást is, szépen kiír-e minden adatot és jó formátumban
    cout<<"\t";
    cellak.kiir();
    cout<<"\t";
    players.kiir();
    kiir();
    return true;
}

/// Teszt 3: elmentett fájl betöltése
/// @return sikeres/sikertelen teszt
bool Jatek::file_test() {
    cout<<"3. TESZT: elmentett fajl betoltese\n";
    ///Használt fájl adatai
    int menet=0; //menetszám
    bool elso=true;  //ki jöb
    size_t cellaszam=14;   //hány cella
    pl_tipus typ[2]={ember, buta}; //típusok
    string nev[2]={"A", "B"};   //nevek
    int diff[2]={0, 0};     //nehézségek
    int cella_ell[cellaszam]={4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0};
    ///Betöltés
    try {
        betolt("AvsB.txt"); //Fájl neve
    }
    catch(runtime_error) {
        cout<<"\tHibas fajlformatum detektalva -> jo mukodes\n"; //de most nem kellene hibasnak lennie
    }
    ///Sikerült-e az adatok elmentése
    //Elsõ sor adatai
    if(menetszam!=menet||elsokov!=elso||cellak.size()!=cellaszam) {
        cout<<"Hiba a jatek peldany file_test tagfuggvenyeben:\n"
            <<"\tBetoltes mukodese rossz: hibas menetszam vagy cellaszam vagy kovetkezo jatekos\n"
            <<"\tElvart menetszam: "<<menet <<"\n\tAktualis menetszam: "<<menetszam<<endl
            <<"\tElvart nem kovetkezo jatekos indexe: "<<elso+1 <<"\n\tAktualis nem kovetkezo jatekos indexe: "<<elsokov+1<<endl
            <<"\tElvart cellaszam: "<<cellaszam <<"\n\tAktualis cellaszam: "<<cellak.size()<<endl
            <<"\tLehetseges hibak: betolt fv vagy DinTomb cella hozzaad (2. teszt)\n";
        return false;
    }
    //Játékosok
    for(int i=0; i<2; i++) {
        if(typ[i]!=players[i]->gettyp()||nev[i]!=players[i]->getnev()||diff[i]!=players[i]->getnehezseg()) {
            cout<<"Hiba a jatek peldany file_test tagfuggvenyeben:\n"
                <<"\tBetoltes mukodese rossz: "<<i+1<<". jatekosok adatai tevesek\n"
                <<"\tElvart tipus sorszama: "<<typ[i] <<"\n\tAktualis tipus sorszama: "<<players[i]->gettyp()<<endl
                <<"\tElvart nev: "<<nev[i] <<"\n\tAktualis nev: "<<players[i]->getnev()<<endl
                <<"\tElvart nehezseg: "<<diff[i] <<"\n\tAktualis nehezseg: "<<players[i]->getnehezseg()<<endl
                <<"\tLehetseges hibak: betolt fv vagy DinTomb Jatekos* hozzaad (2. teszt)\n";
            return false;
        }
    }
    //Cellaértékek
    for(size_t i=0; i<cellaszam; i++) {
        if(cellak[i]!=cella_ell[i]) {
            cout<<"Hiba a jatek peldany file_test tagfuggvenyeben:\n"
                <<"\tBetoltes mukodese rossz: betoltott cellak erteke vagy szama hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(size_t i=0; i<cellaszam; i++) {
                cout<<cella_ell[i]<<" ";
            }
            cout<<"\n\t\tTeljes betoltott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: betolt fv vagy DinTomb cella hozzaad fv (2. teszt)\n";
            return false;
        }
    }
    ///Így hogy elmentem, ha kétszer egymás után futtatjuk a programot és rosszul működik lehet tudni, hogy a mentéssel baj van
    mentes(); //outputon pár csúnya sort okoz (de a felhasználónak elég hasznos)
    ///Hibás fájlformátum esetén történik-e kivételkezelés
    try {
        betolt("hiba.txt");
    }
    catch(runtime_error) {
        cout<<"\tHibas fajlformatum detektalva -> jo mukodes\n";
    }
    return true;
}

/// Teszt 4: alaplépés és játékvégi funkciók mûködése
///Ez a teszt a 3. tesztesetben betöltött játékban lép (csak nem a játékosok lépnek, hanem  a teszt automatikusan)
///Ellenõrzi a golyók megfelelõ elhelyezését, és minden játékszabály helyes mûködését
///Üres cellát és bázis nem lehet választani, de ezt a fõprogram nem is engedi, a hibakezelés ott van megoldva
/// @return sikeres/sikertelen teszt
bool Jatek::lepes_veg_teszt() {
    cout<<"4. TESZT: alaplepes es jatekvegi funkciok\n";
    //kezdetben 6 cella plusz bázis játékosonként, 4 golyó cellánként (kivéve a bázist)
    ///4-es index elsõ játékos
    int idx=4;
    if(!elsokov) idx+=7;
    int cella_ell1[14]={4, 4, 4, 4, 0, 5, 1, 5, 5, 4, 4, 4, 4, 0};
    players[!elsokov]->alaplepes(cellak, elsokov, idx);
    for(size_t i=0; i<14; i++) {
        if(cellak[i]!=cella_ell1[i]) {
            cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
                <<"\tAlaplepes mukodese rossz: cellak erteke hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell1[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(size_t j=0; j<14; j++) {
                cout<<cella_ell1[j]<<" ";
            }
            cout<<"\n\t\tTeljes kapott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: Alaplepes fv\n";
            return false;
        }
    }
    ///1-es index 2. játékos
    //Újra a 2. játékos jön, a bázisában fejezte be
    //Ezt nem kell külön ellenõrizni, mert ha rosszul mentette a következõ lépés eredménye hibás
    idx=1;
    if(!elsokov) idx+=7;
    int cella_ell2[14]={4, 4, 4, 4, 0, 5, 1, 5, 0, 5, 5, 5, 5, 1};
    players[!elsokov]->alaplepes(cellak, elsokov, idx);
    for(size_t i=0; i<14; i++) {
        if(cellak[i]!=cella_ell2[i]) {
            cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
                <<"\tAlaplepes mukodese rossz: cellak erteke hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell2[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(size_t j=0; j<14; j++) {
                cout<<cella_ell2[j]<<" ";
            }
            cout<<"\n\t\tTeljes kapott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: Alaplepes fv\n";
            return false;
        }
    }
    ///0-s index 2. játékos
    idx=0;
    if(!elsokov) idx+=7;
    int cella_ell3[14]={4, 4, 4, 4, 0, 5, 1, 0, 1, 6, 6, 6, 6, 1};
    players[!elsokov]->alaplepes(cellak, elsokov, idx);
    for(size_t i=0; i<14; i++) {
        if(cellak[i]!=cella_ell3[i]) {
            cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
                <<"\tAlaplepes mukodese rossz: cellak erteke hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell3[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(size_t j=0; j<14; j++) {
                cout<<cella_ell3[j]<<" ";
            }
            cout<<"\n\t\tTeljes kapott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: Alaplepes fv\n";
            return false;
        }
    }
    ///0-s index elsõ játékos
    idx=0;
    if(!elsokov) idx+=7;
    //Saját üres cellában fejezte be, extra golyókat kap (4, 8 indexû cellák golyói a 6-osba)
    int cella_ell4[14]={0, 5, 5, 5, 0, 5, 3, 0, 0, 6, 6, 6, 6, 1};
    players[!elsokov]->alaplepes(cellak, elsokov, idx);
    for(size_t i=0; i<14; i++) {
        if(cellak[i]!=cella_ell4[i]) {
            cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
                <<"\tAlaplepes mukodese rossz: cellak erteke hibas\n"
                <<"\tHibas cella indexe (0-tol): "<<i<<endl
                <<"\tElvart cellaertek: "<<cella_ell4[i]<<" \n\tAktualis cellaertek: "<<cellak[i]<<endl
                <<"\t\tTeljes elvart tabla:\n\t\t";
            for(size_t j=0; j<14; j++) {
                cout<<cella_ell4[j]<<" ";
            }
            cout<<"\n\t\tTeljes kapott tabla:\n\t\t";
            cellak.kiir();
            cout<<"\tLehetseges hibak: Alaplepes fv\n";
            return false;
        }
    }
    ///Vége van-e a játéknak (nincs)
    bool elvart=false;
    if(elvart!=vege()) {
        cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
            <<"\tJatek vege ellenorzese rossz\n"
            <<"\t\tTeljes tabla:\n\t\t";
        cellak.kiir();
        cout<<"\t\tSoron kovetkezne a "<<(!elsokov)+1 <<". jatekos\n"
            <<"\tLehetseges hibak: vege fv\n";
        return false;
    }
    ///Alternativ jatek (vege_ell.txt) ami végetér
    Jatek vegell;
    vegell.betolt("vege_teszt.txt");
    ///Vége van-e a játéknak (igen)
    //A vegell.jatek() fv is megívható lenne, de a teszteredmények szép kiíratása miatt most nem
    elvart=true;
    if(elvart!=vegell.vege()) {
        cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
            <<"\tvege_teszt.txt fajl betoltese, vege fv meghivasa\n"
            <<"\tJatek vege ellenorzese rossz\n"
            <<"\t\tTeljes tabla:\n\t\t";
        vegell.cellak.kiir();
        cout<<"\t\tSoron kovetkezne a "<<(!elsokov)+1 <<". jatekos\n"
            <<"\tLehetseges hibak: vege fv, betolt fv (3. teszt)\n";
        return false;
    }
    ///Maradékok pakolása az alternatív játékban
    int cellak_veg[10]={0, 0, 0, 0, 20, 0, 0, 0, 0, 12};
    vegell.maradek_pakol();
    for(int i=0; i<10; i++) {
        if(vegell.cellak[i]!=cellak_veg[i]) {
            cout<<"Hiba a jatek peldany lepes_veg_test tagfuggvenyeben:\n"
                <<"\tvege_teszt.txt fajl betoltese, maradek_pakol fv meghivasa\n"
                <<"\tMaradekok pakolasa rossz\n"
                <<"\t\tElvart tabla:\n\t\t";
            for(int j=0; j<10; j++) {
                cout<<cellak_veg[j]<<" ";
            }
            cout<<"\n\t\tTeljes tabla:\n\t\t";
            vegell.cellak.kiir();
            cout<<"\tLehetseges hibak: maradek_pakol fv, betolt fv (3. teszt)\n";
            return false;
        }
    }
    return true;
}

/// Teszt 5: okos számítógép algoritmusának ellenõrzése egyszerû példán
///Ez a teszt a 2. tesztesetben elkészített játékban számolja ki az aktuális játékos ideális lépést
/// @return sikeres/sikertelen teszt
bool Jatek::okosalgo_teszt() {
    cout<<"5. TESZT: okosalgoritmus mukodese\n";
    ///1. lépéshez
    //jelenlegi tábla: 3, 3, 3, 0, 3, 3, 3, 0
    //elsõ játékos okos, 2 nehézséggel, õ következik
    //ha nem a 0. indexet választja akkor nem õ jön újra, így két lépés alatt legjobb esetben 0 lesz a két bázis közti különbség
    //ha a 0. indexet választja újra õ jön és így két lépés alatt legrosszabb esetben is 2-vel több lesz az õ bázisában
    //--> a 0. indexet fogja választani
    ///2. lépéshez
    //tábla: 0, 4, 4, 1, 3, 3, 3, 0
    //az elsõ játékos következik
    //mostmár nem tud újra õ következni
    //bármi lesz a két lépés az õ bázisában 2 lesz a végén,
    //az ellenfél bázisában pedig 1
    //ezért a legelsõ helyes indexet választja, ami az 1-es
    ///3. lépéshez
    //tábla: 0, 0, 5, 2, 4, 4, 3, 0
    //a második játékos következik, õ csak 1 lépést tud elõre nézni, melyikkel szerez a legtöbbet
    //mindegyikkel ugyanaz lesz, ezért a 0. indexût választja (ami a tömbben 4. index)
    ///4. lépéshez
    //tábla: 1, 0, 5, 2, 0, 5, 4, 1
    //az elsõ játékos következik
    //Ha az elsõ cellát választja akkor a játékszabály miatt 6-tal nõ a bázisának értéke
        //mivel az utolsó cella saját és vele szemben olyan van ami nem üres, ezért megkapja mindkettõ golyóit
        //tehát 1-et és még ami azzal szemben (5-ös index) van: 5 golyó
    //Bármit is választ az ellenfél, a lehetõ legjobb lépés (2-es nehézséggel nézve) biztosan ez
    ///5. lépéshez
    //tábla: 0, 0, 5, 8, 0, 0, 4, 1
    //következik a második játékos, egyetlen cellát választhat
    ///6. lépéshez
    //tábla: 1, 1, 6, 8, 0, 0, 0, 2
    //következik az elsõ játékos
    //Ha az elso kettobol valaszt vege a jateknak és a lehetõ legtöbb golyóval nyer
    //Ha az utolsót választja az elsõ lépés után nem nõ a különbség, a második után meg csak csökken
    //-->0. indexet fogja választani
    for(int i=0; i<6; i++) {
        ///Elvárt index beállítása az aktuális lépés után
        int elvart_idx=(i%2); //az elsõ 3 lépésnél még ez mûködik de utána nem
        if(i==3||i==5) elvart_idx=0;
        if(i==4) elvart_idx=2;
        if(!elsokov) elvart_idx+=4;
        ///Mivel tudjuk, hogy a játékos OkosPC (mert ezt teszteljük), ezért nem gond a cast
        int kapott_idx=dynamic_cast<OkosPC*>(players[!elsokov])->valasztas(cellak, elsokov, players[!elsokov]->getnehezseg());
        if(elvart_idx!=kapott_idx) {
            cout<<"Hiba az okosalgo_teszt tagfuggvenyeben:\n"
            <<"\tHibas az okosalgoritmus mukodese\n"
            <<"\tHanyadik lepes soran tortent a hiba: "<<i+1<<". lepes\n"
            <<"\tElvart index: "<<elvart_idx
            <<"\n\tKapott index: "<<kapott_idx
            <<"\n\tJatekos adatai:"
            <<"\n\t\tNev: "<<players[!elsokov]->getnev()
            <<"\n\t\tNehezseg: "<<players[!elsokov]->getnehezseg()
            <<"\n\t\tTipus sorszama: "<<players[!elsokov]->gettyp()
            <<"\n\t\tA jelenlegi jatekos sorszama: "<<(!elsokov)+1
            <<"\n\tLehetseges hibak: kiertekelo vagy valasztas fv\n";
        return false;
        }
        players[!elsokov]->alaplepes(cellak, elsokov, elvart_idx);
    }
    //Végén a tábla: 0, 2, 6, 8, 0, 0, 0, 2
    //Lehetne vege és maradek_pakol fv-eket is ellenõrizni, de arra egy másik teszt már van.
    return true;
}
