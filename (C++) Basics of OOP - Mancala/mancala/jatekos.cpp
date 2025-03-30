/**
 * \file jatekos.cpp
 * Minden Játékosból származó függvény definiálása
 */

#include <cstring>
#include <cmath>
#include <ctime>
#include "memtrace.h"
#include "jatekos.h"
#include "jatek.h"

/// Lépés végrehajtása
/// Minden típus a végén egyformán lép, kiválasztja, hogy melyik cellát üríti ki
/// @param cellak - tábla celláinak tömbje (referencia - módosítja)
/// @param kov - elsõ játékos következik-e (a következõ lépéshez fontos)
/// @param idx - melyik cellát üríti ki a játékos
void Jatekos::alaplepes(DinTomb<int>& cellak, bool& kov, int& idx) {
    int n=cellak.size();
    int db=cellak[idx]; //tudjuk hány golyót kell lerakni
    cellak[idx]=0; //kiválaszott cella kiürítése
    for( ;db!=0; db--) {
        idx+=1;
        if(idx==n) idx=0;   //ha a végére ért vissza a tömb elejére
        if(idx==n-1) {
            if(!elso) cellak[idx]++;
            else db++;      //ha elsõ játékos akkor ne rakjon az ellenfél bázisába (de akkor a golyók száma se csökkenjen)
        }
        else if(!elso&&idx==(n-2)/2) {
            db++;   //ha második akkor ne rakjon az elsõ bázisába
        }
        else cellak[idx]++; //egyébként rakjanak le egyet (nõ a cella értéke, a db pedig a ciklusban csökken)
    }
    ///Ha a saját bázisukba került az utolsó, akkor újra õ következik (nem változik a kov értéke)
    if((elso&&idx==(n-2)/2)||((!elso)&&idx==n-1)) {
        return;
    }
    ///Ha a saját oldalukon egy lyukba fejezték ba ami eddig üres volt (most 1 az értéke) és azzal szemben nem üres lyuk van
    ///Átpakolják mindkét lyuk golyóit a bázisukba
    if(cellak[idx]==1&&cellak[n-2-idx]>0&&((elso&&idx>=0&&idx<(n-2)/2)||(!elso&&idx>(n-2)/2&&idx<n-1))) {
        cellak[idx]=0;
        if(idx<(n-2)/2) {
            cellak[(n-2)/2]+=(1+cellak[n-2-idx]);
        }
        else cellak[n-1]+=(1+cellak[n-2-idx]);
        cellak[n-2-idx]=0;
    }
    ///A másik játékos következik (ha nem akkor ide már nem jut el)
    kov=!kov;
}

/// Kiíró operátor (DinTomb kiir használata esetén)
/// @param os - ostream típusú objektum
/// @param pl - Játékos akinek az adatait kiírjuk
/// @return os
std::ostream& operator<<(std::ostream& os, const Jatekos& pl) {
    os<<pl.getnev();
    if(pl.getnehezseg()!=0) os<< ", "<<pl.getnehezseg();
    return os;
}

/// Lépés végrehajtása Ember játékosnak
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @return - egyedül Embernél és CoPilotnél van jelentõssége
/// @return - funkció választása: 0 - lép, 1 - elmenti a játékot, 2 - kilép
int Ember::lepes(DinTomb<int>& cellak, bool& kov) {
    std::cout<<"Kerem adja meg melyik cellat uritene ki:\n"
        <<"   - amennyiben elmentene a jatekot, irja be hogy \"save\"\n"
        <<"   - amennyiben mentes nelkul kilepne, irja be hogy \"exit\"\n";
    char str[4+1];
    int idx;
    bool hibasidx=true;
    bool hibasgolyodb=true;
    ///A tábla kiíratásának megfelelõen beolvas egy azonosító karaktert
    ///Hibakezelés: Ha hibás a karakter, nem fogadja el, újat kér
    do {
        hibasidx=true;
        hibasgolyodb=true;
        std::cin>>str;
        ///Mentés esetén return 1
        if(strcmp(str, "save")==0) return 1;
        ///Mentés nélküli kilépés esetén return 2
        if(strcmp(str, "exit")==0) return 2;
        if(strlen(str)==1) {
            char c=str[0];
            if(elso) idx=c-'A';
            else idx=c-'a';
            ///Nem megfelelõ karaktert írt be, nincs ilyen azonosítójú lyuka
            if(idx>=0&&(size_t)idx<=(cellak.size()-2)/2-1) {
                hibasidx=false;
                if(!elso) idx+=cellak.size()/2;
                ///Nem választhat üres lyukat
                if(cellak[idx]!=0) hibasgolyodb=false;
                else std::cout<<"Ures cellat valaszott, valasszon masikat:\n";
            }
            else std::cout<<"Hibas index, adja meg ujra:\n";
        }
        ///Nem egy azonosító karatkert adott meg
        else std::cout<<"Hibas bemenet, probalja ujra:\n";
    } while(hibasidx||hibasgolyodb);
    ///Megfelelõ index esetén végrehajtja a lépést (módosítja a cellákat, következõ játékost is)
    alaplepes(cellak, kov, idx);
    ///Lépés esetén return 0
    return 0;
}

/// Lépés végrehajtása RandomPC játékosnak
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @return 0: egyetlen funkció a lépés
int RandomPC::lepes(DinTomb<int>& cellak, bool& kov) {
    int n=(cellak.size()-2)/2;  //hány számból kell választania egyet
    int szam, idx;
    ///Attól függõen hogy hanyadik játékos a megfelelõ indexre állítja
    ///Ha üres cellát választ újra választania kell
    do {
        szam=rand()%n;
        if(elso) idx=szam;
        else idx=szam+n+1;
    } while(cellak[idx]==0);
    char c;
    if(elso) c='A'+szam;
    else c='a'+szam;
    //hogy tudja a felhasználó is
    std::cout<<"A szamitogep altal valaszott cella: " <<c <<std::endl;
    ///Megfelelõ index esetén végrehajtja a lépést (módosítja a cellákat, következõ játékost is)
    alaplepes(cellak, kov, idx);
    return 0;
}

/// Kiértékelõ: értékeli az aktuális táblát a számítógép szemszögébõl a bázisok segítségével, rekurzióval
/// Mûködése a valasztas fv-hez hasonló, DE
/// - A választás meghívásakor biztos a számítógép következik, míg itt változó
/// --> Esetszétválasztást kell végezni, mert ha az ellenfél jön a számára legkedvezõbb esettel kell számolni
/// - A végleges index meghatározása nem ennek a függvénynek a felelõssége
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @param diff - játékos nehézsége: hány lépéssel számoljon elõre
/// @return - Az aktuális lépésig mekkora értékű ez az opció, mennyirre kedvező
int OkosPC::kiertekelo(const DinTomb<int>& cellak, const bool& kov, int diff) {
    ///Végignézi az összes lehetséges kimenetet
    ///Csak a saját celláira kell (azzal tud lépni)
    int mettol=0;
    int meddig=(cellak.size()-2)/2;
    if(!elso) {
        mettol+=(meddig+1);
        meddig+=mettol;
    }
    ///Kilépési feltételek:
    ///1, Ha már véget ért a játék, akkor nincs mit tovább nézni
    ///2, Ha elérte a diff==0-t, vagyis eljutott a diff-edik lépésig amíg elõre kellett számolnia.
    bool vege=true;
    for(int i=mettol; i<meddig; i++) {
        if(cellak[i]!=0) vege=false;
    }
    if(vege||(diff==0)) {
        int eredmeny;
        eredmeny=cellak[cellak.size()-1]-cellak[(cellak.size()-2)/2];
        if(elso) eredmeny *=-1;
        return eredmeny; //visszatérhet a bázisok különbségével (pc szemszögébõl)
    }
    ///Ha a számítógép jön számára a lehetõ legjobb kimenetelt kell nézni
    if((elso&&kov)||(!elso&&!kov)) {
        int legjobb_ertek=-52000; //ennél csak jobb lesz
        ///Végignézi az összes választási lehetõségének kimenetelét
        for(int i=mettol; i<meddig; i++) {
            int idx=i;
            if (cellak[i]==0) continue; //üres cellát nem választhat
            DinTomb<int> uj_cellak=cellak;  //másolat készítése (copy konstruktor DinTomb-ben)
            bool uj_kov=kov;
            ///Megnézi, hogy ha ezt választaná milyen játékállás lenne
            alaplepes(uj_cellak, uj_kov, idx);
            ///Ezt a játékállást is kiértékeli, úgy, hogy megnézi az összes lehetõséget, de már eggyel kevesebb mélységig
            int ertek=kiertekelo(uj_cellak, uj_kov, diff-1);
            ///Lehetõ legjobb érték lett-e
            if(ertek>legjobb_ertek) {
                legjobb_ertek=ertek;
            }
        }
        return legjobb_ertek;
    }
    ///Ha nem a számítógép jön, akkor a számítógép számára lehetõ legrosszabb kimenetelt kell nézni
    else {
    ///A játékot az ellenfél szemszögébõl kell nézni, viszont az õ függvényeihez nem férünk hozzá
    ///Az alaplepest a számítógépre hívjuk meg
    ///A számítógép elsõjét fogja figyelembe venni
    ///Ez megoldható úgy, hogy módosítjuk az elso értékét, mintha a számítógép a másik oldalon játszana
    ///(például így jó bázist fog kihagyni a golyók elhelyezésekor)
    ///Erre a cellák végignézésekor és az alaplépéskor szükség van, DE
    ///Utána a kiértékeléshez (lehetõ legrosszabb eset), vissza kell állítani
    ///(hogy a következõ lépés vizsgálatakor is jól mûködjön, és majd max ott is megcseréljük)
        int legrosszabb_ertek=52000; //ennél csak rosszabb lesz
        mettol=0;
        meddig=(cellak.size()-2)/2;
        ///Most pont akkor kell hozzáadni (hogy a másik oldalt nézze), ha õ az elsõ
        if(elso) {
            mettol+=(meddig+1);
            meddig+=mettol;
        }
        ///Végignézi az összes választási lehetõségének kimenetelét
        for (int i=mettol; i<meddig; i++) {
            elso=!elso; ///< Itt történik meg a csere
            int idx=i;
            if (cellak[i]==0) { //üres cellát nem választhat
                elso=!elso;     //ilyenkor is vissza kell állítani, hogy ne csússzon el
                continue;
            }
            DinTomb<int> uj_cellak=cellak; //másolat
            bool uj_kov=kov;
            ///Aktuális kiválasztásával mi történik
            alaplepes(uj_cellak, uj_kov, idx);
            elso=!elso; ///Vissza kell állítani, hogy a kiértékelést már jól csinálja
            int ertek=kiertekelo(uj_cellak, uj_kov, diff-1);
            ///Lehetõ legrosszabb érték-e
            if (ertek<legrosszabb_ertek) {
                legrosszabb_ertek=ertek;
            }
        }
        return legrosszabb_ertek;
    }
}

/// Választás: kiválasztja a számítógép szemszögébõl legkedvezõbb lyukat amivel lép
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @param diff - játékos nehézsége: hány lépéssel számoljon elõre
/// @return - Az algoritmus szerinti legkedvezõbb lépéssel, a kiürítendõ cella indexével tér vissza
int OkosPC::valasztas(const DinTomb<int>& cellak, const bool& kov, int diff) {
    ///Végignézi az összes lehetséges kimenetet
    ///Csak a saját celláira kell (azzal tud lépni)
    int mettol=0;
    int meddig=(cellak.size()-2)/2;
    if(!elso) {
        mettol+=(meddig+1);
        meddig+=mettol;
    }
    ///Elsõnek mindig õ következik: ilyenkor a legjobb kimenetelt kell nézni
    int legjobb_ertek=-52000; //ennél csak jobb kimenetel lehet
    int legjobb_index=-1;     //biztosan kap majd egy valid értéket
    for(int i=mettol; i<meddig; i++) {
        int idx=i;
        if (cellak[i]==0) continue;     //üres cellát nem választhat
        DinTomb<int> uj_cellak=cellak;  //másolat készítése (van DinTomb másoló konstruktor)
        bool uj_kov=kov;
        ///Megnézzük, hogy az aktuális cellát választva milyen játékállás lesz
        alaplepes(uj_cellak, uj_kov, idx);
        ///Ezt az új állást kiértékeljük úgy, hogy már csak eggyel kevesebb mélységig kell vizsgálnia
        int ertek=kiertekelo(uj_cellak, uj_kov, diff-1);
        ///A lehetõ legjobb esetet kell nézni, ezért ha ez az, el is mentjük az indexet (és az értéket is a késõbbi összehasonlításhoz)
        if(ertek>legjobb_ertek) {
            legjobb_ertek=ertek;
            legjobb_index=i;
        }
    }
    return legjobb_index;
}

/// Lépés végrehajtása OkosPC játékosnak
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @return 0: egyetlen funkció a lépés
int OkosPC::lepes(DinTomb<int>& cellak, bool& kov) {
    ///Végrehajtja az algoritmust és visszatér a válaszott cella indexével
    ///Nem referenciával adja át, így nem módosítja a tényleges játékállást
    int idx=valasztas(cellak, kov, nehezseg);
    char c;
    int szam=(elso)?(idx):(idx-(cellak.size()/2));
    if(elso) c='A'+szam;
    else c='a'+szam;
    //hogy a felhasználó is tudja
    std::cout<<"A szamitogep altal valaszott cella: " <<c <<std::endl;
    ///Végrehajtja a lépést (módosítja a cellákat, következõ játékost is)
    alaplepes(cellak, kov, idx);
    return 0;
}

/// Lépés végrehajtása CoPilot játékosnak
/// @param cellak - tábla celláinak tömbje
/// @param kov - elsõ játékos következik-e
/// @return - egyedül Embernél és CoPilotnél van jelentõssége
/// @return - funkció választása: 0 - lép, 1 - elmenti a játékot, 2 - kilép
int CoPilot::lepes(DinTomb<int>& cellak, bool& kov) {
    ///Elõször végrehajtja az okos algoritmust (az adattagjának segítségével)
    int idx=okospc.valasztas(cellak, kov, nehezseg);
    char c;
    int szam=(elso)?(idx):(idx-(cellak.size()/2));
    if(elso) c='A'+szam;
    else c='a'+szam;
    ///Az algoritmus által javasolt cellát a felhasználó tudtára adja
    std::cout<<"A szamitogep altal javasolt cella: " <<c <<std::endl;
    ///Majd végrehajtja az Ember osztály lépését, ahol az ember dönt melyik cellát választja
    return Ember::lepes(cellak, kov);
}
