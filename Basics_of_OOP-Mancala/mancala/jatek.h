/**
 * \file jatek.h
 * J�t�k oszt�ly deklar�l�sa �s inline f�ggv�nyei
 */

#ifndef JATEK_H_INCLUDED
#define JATEK_H_INCLUDED

#include "memtrace.h"
#include "jatekos.h"

/// J�t�k oszt�ly
/// Tartalma:
/// 1, A j�t�k�ll�s setterei, getterei
/// 2, J�t�k lefoly�s�t v�gz� f� f�ggv�nyek
/// 3, A program tesztel�s�t v�gz� f�ggv�nyek
class Jatek {
    DinTomb<Jatekos*> players;  ///< J�t�kosokat �s adataikat t�rol� dinamikus t�mb
    DinTomb<int> cellak;        ///< A t�bl�t (cell�k, b�zisok �s goly�ik) tartalmaz� din. t�mb
    bool elsokov;               ///< Soron k�vetkez� j�t�kos az els�-e
    int menetszam;              ///< H�ny l�p�s t�rt�nt a j�t�k sor�n eddig
public:
    /// Konstruktor (a cell�k �s j�t�kosok hozz�ad�sa k�l�n t�rt�nik)
    /// @param elso - az els� j�t�kos k�vetkezzen-e
    Jatek(bool elso=true): elsokov(elso), menetszam(0) {}

    /// �j j�t�k in�cializ�l�sa felhaszn�l� �ltal
    void init();

    /// Cellasz�m lek�rdez�se
    /// @return - t�bl�n l�v� cell�k sz�ma (b�zissal, mindk�t oldallal)
    int getcellaszam() const { return cellak.size(); }

    /// K�vetkez� j�t�kos lek�rdez�se
    /// @return - els� k�vetkezik-e
    bool getkov() const { return elsokov; }

    /// K�vetkez� j�t�kos be�ll�t�sa
    /// @param elso - az els� k�vetkezzen-e, ez lesz be�ll�tva
    void setkov(const bool& elso) { elsokov=elso; }

    /// Menetsz�m lek�rdez�se
    /// @return - h�ny l�p�st tettek meg a j�t�k kezdete �ta
    int getmenet() const { return menetszam; }

    /// Menetsz�m be�ll�t�sa
    /// @param menet - be�ll�tja h�ny l�p�st tettek meg a j�t�k kezdete �ta
    void setmenet(const int& menet) { menetszam=menet; }

    /// J�t�k f� menet�nek kezel�se
    void jatek();

    /// J�t�k v�ge
    bool vege() const;

    /// J�t�k �ll�s�nak (t�bla) kirajzol�sa
    void kiir() const;

    /// J�t�k v�ge eset�n a marad�k goly�k b�zisba rak�sa
    void maradek_pakol();

    /// J�t�k f�jlba ment�se
    void mentes();

    /// Elmentett j�t�k f�jlb�l bet�lt�se
    void betolt(const char*);

    /// Teszt 2: j�t�k in�cializ�l�sa, adatokkal felt�lt�se
    bool add_test();

    /// Teszt 3: elmentett f�jl bet�lt�se
    bool file_test();

    /// Teszt 4: alapl�p�s �s j�t�kv�gi funkci�k m�k�d�se
    bool lepes_veg_teszt();

    /// Teszt 5: okos sz�m�t�g�p algoritmus�nak ellen�rz�se egyszer� p�ld�n
    bool okosalgo_teszt();

    ///Destruktor
    ~Jatek() {}
};

#endif // JATEK_H_INCLUDED
