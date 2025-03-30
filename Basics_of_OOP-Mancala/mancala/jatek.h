/**
 * \file jatek.h
 * Játék osztály deklarálása és inline függvényei
 */

#ifndef JATEK_H_INCLUDED
#define JATEK_H_INCLUDED

#include "memtrace.h"
#include "jatekos.h"

/// Játék osztály
/// Tartalma:
/// 1, A játékállás setterei, getterei
/// 2, Játék lefolyását végzõ fõ függvények
/// 3, A program tesztelését végzõ függvények
class Jatek {
    DinTomb<Jatekos*> players;  ///< Játékosokat és adataikat tároló dinamikus tömb
    DinTomb<int> cellak;        ///< A táblát (cellák, bázisok és golyóik) tartalmazó din. tömb
    bool elsokov;               ///< Soron következõ játékos az elsõ-e
    int menetszam;              ///< Hány lépés történt a játék során eddig
public:
    /// Konstruktor (a cellák és játékosok hozzáadása külön történik)
    /// @param elso - az elsõ játékos következzen-e
    Jatek(bool elso=true): elsokov(elso), menetszam(0) {}

    /// Új játék inícializálása felhasználó által
    void init();

    /// Cellaszám lekérdezése
    /// @return - táblán lévõ cellák száma (bázissal, mindkét oldallal)
    int getcellaszam() const { return cellak.size(); }

    /// Következõ játékos lekérdezése
    /// @return - elsõ következik-e
    bool getkov() const { return elsokov; }

    /// Következõ játékos beállítása
    /// @param elso - az elsõ következzen-e, ez lesz beállítva
    void setkov(const bool& elso) { elsokov=elso; }

    /// Menetszám lekérdezése
    /// @return - hány lépést tettek meg a játék kezdete óta
    int getmenet() const { return menetszam; }

    /// Menetszám beállítása
    /// @param menet - beállítja hány lépést tettek meg a játék kezdete óta
    void setmenet(const int& menet) { menetszam=menet; }

    /// Játék fõ menetének kezelése
    void jatek();

    /// Játék vége
    bool vege() const;

    /// Játék állásának (tábla) kirajzolása
    void kiir() const;

    /// Játék vége esetén a maradék golyók bázisba rakása
    void maradek_pakol();

    /// Játék fájlba mentése
    void mentes();

    /// Elmentett játék fájlból betöltése
    void betolt(const char*);

    /// Teszt 2: játék inícializálása, adatokkal feltöltése
    bool add_test();

    /// Teszt 3: elmentett fájl betöltése
    bool file_test();

    /// Teszt 4: alaplépés és játékvégi funkciók mûködése
    bool lepes_veg_teszt();

    /// Teszt 5: okos számítógép algoritmusának ellenõrzése egyszerû példán
    bool okosalgo_teszt();

    ///Destruktor
    ~Jatek() {}
};

#endif // JATEK_H_INCLUDED
