/**
 * \file jatekos.h
 * Játékos ősosztály és gyerekeinek deklarálása, inline függvényeik
 */

#ifndef JATEKOS_H_INCLUDED
#define JATEKOS_H_INCLUDED

#include <iostream>
#include "memtrace.h"
#include "dintomb.hpp"

/// @enum - Játékos típusa: ember, buta (random), okos, co-pilot
enum pl_tipus {
    ember, buta, okos, co
};

/// Játékos ősosztály
class Jatekos {
protected:
    bool elso;          ///< Ő-e az első (az ő celláival kezdődik a tömb)
    std::string nev;    ///< Név
    int nehezseg;       ///< Nehézség (okos algoritmus rekurziójának mélysége)
    pl_tipus typ;       ///< Játékos típusa
public:
    /// Konstruktor
    /// @param first - első-e
    /// @param str - név, default: ""
    /// @param diff - nehézség, default: 0
    /// @param mi - játékos típusa, default: ember
    Jatekos(bool first, std::string str="", int diff=0, pl_tipus mi=ember): elso(first), nev(str), nehezseg(diff), typ(mi) {}

    /// Hanyadik játékos
    /// @return - igaz ha ő az első
    bool getelso() const {return elso; }

    /// Játékos névlekérdezés
    /// @return - név
    std::string getnev() const { return nev; }

    /// Játékos névbeállítás
    /// @param - beállítandó név
    void setnev(const std::string& str) { nev=str; }

    /// Játékosnehézség lekérdezés
    /// @return - nehézség
    int getnehezseg() const { return nehezseg; }

    /// Játékosnehézség beállítása
    /// @param - beállítandó nehézség
    void setnehezseg(const int& diff) { nehezseg=diff; }

    /// Játékostípus lekérdezés
    /// @return - típus
    pl_tipus gettyp() const { return typ; }

    /// Játékostípus beállítása
    /// @param - beállítandó típus
    void settyp(const pl_tipus& mi) { typ=mi; }

    /// Lépés végrehajtása játékos típusától függően
    /// Legyen absztrakt alaposztály (tisztán virtuális)
    /// --> nem lehet simán Jatekos példányt létrehozni
    /// Minden alosztálynak külön megvalósítva
    virtual int lepes(DinTomb<int>&, bool&)=0;

    /// Lépés végrehajtása
    void alaplepes(DinTomb<int>&, bool&, int&);

    ///Virtuális destruktor
    virtual ~Jatekos() {}
};

/// Kiíró operátor (DinTomb kiir használata esetén)
std::ostream& operator<<(std::ostream&, const Jatekos&);

/// Ember alosztály
/// Felhasználónak megfelelően inputról kéri be a lépést
class Ember: public Jatekos {
public:
    /// Konstruktor
    /// @param first - első-e
    /// @param str - név, default: "jatekos"
    /// @param diff - nehézség, default: 0
    /// @param mi - játékos típusa, default: ember
    Ember(bool first, std::string str="jatekos", int diff=0, pl_tipus mi=ember): Jatekos(first, str, diff, mi) {}

    /// Lépés végrehajtása Ember játékosnak
    virtual int lepes(DinTomb<int>&, bool&);

    ///Virtuális destruktor (van leszármazottja)
    virtual ~Ember() {}
};

/// Számítógép alosztály
/// Olyan játékos típusokhoz, amik felhasználó nélkül, maguktól működnek
class Szamitogep: public Jatekos {
public:
    /// Konstruktor
    /// @param first - első-e
    /// @param str - név, default: ""
    /// @param diff - nehézség, default: 0
    /// @param mi - játékos típusa, default: buta
    Szamitogep(bool first, std::string str="", int diff=0, pl_tipus mi=buta): Jatekos(first, str, diff, mi) {}

    /// Lépés végrehajtása
    /// Tisztán virtuális, hogy absztrakt osztály legyen
    /// Nincs Szamitogep játékos, csak annak a leszármazottai
    virtual int lepes(DinTomb<int>&, bool&)=0;

    ///Virtuális destruktor (van leszármazottja)
    virtual ~Szamitogep() {}
};

/// RandomPC alosztály
/// Olyan játékos, aki mindig véletlenszerűen választ a lehetséges megengedett lépései közül
class RandomPC: public Szamitogep {
public:
    /// Konstruktor
    /// @param first - első-e
    /// A többi paraméterét tudjuk (típus adott, név automatikus, nehézség irreleváns)
    RandomPC(bool first): Szamitogep(first, "randompc") {}

    /// Lépés végrehajtása
    int lepes(DinTomb<int>&, bool&);

    ///Destruktor
    ~RandomPC() {}
};

/// OkosPC alosztály
/// A megadott nehézségnek megfelelően előre megírt algoritmussal kiszámolja a számára legkedvezőbb lépést
class OkosPC: public Szamitogep {
public:
    /// Konstruktor
    /// @param first - első-e
    /// @param diff - nehézség, default: 1
    /// A többi paraméterét tudjuk (típus adott, név automatikus)
    OkosPC(bool first, int diff=1): Szamitogep(first, "okospc", diff, okos) {}

    /// Lépés végrehajtása
    int lepes(DinTomb<int>&, bool&);

    /// Egy aktuális lépés sikerességét számolja ki
    /// Ez hívódik meg rekurzióval
    int kiertekelo(const DinTomb<int>&, const bool&, int);

    /// Visszatér az algoritmus által javasolt cellaindexszel
    /// Innen indul a rekurzió
    int valasztas(const DinTomb<int>&, const bool&, int);

    ///Destruktor
    ~OkosPC() {}
};

/// CoPilot alosztály (Ember és OkosPC ötvözete)
/// Amellett, hogy emberként viselkedik, így a felhasználó dönti el mit lép
/// Segít neki egy megadott nehézségű OkosPC, ami egy általa ideálisnak gondolt javaslatot ad a felhasználónak
class CoPilot: public Ember {
    OkosPC okospc;      ///< Mivel az okos algoritmust elvégző függvények az OkosPC függvényei
                        /// A CoPilot pedig az Ember alosztálya, ezért szükség van egy OkosPC adattagra
                        /// Rajta keresztül végrehajthatjuk a co-pilot OkosPC részét is
public:
    /// Konstruktor
    /// @param first - első-e
    /// @param str - név, default: "copilot"
    /// @param diff - nehézség, default: 1
    CoPilot(bool first, std::string str="copilot", int diff=1): Ember(first, str, diff, co), okospc(first, diff) {}

    /// Lépés végrehajtása
    /// okospc lépése (adattaggal) majd az ember lépése (kompatibilitás)
    int lepes(DinTomb<int>&, bool&);

    ///Destruktor
    ~CoPilot() {}
};
#endif // JATEKOS_H_INCLUDED
