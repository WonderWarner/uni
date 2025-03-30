/**
 * \file dintomb.cpp
 * DinTomb sablon osztály és specializált függvényeinek megvalósítása
 */

#include <iostream>
#include <cstring>
#include "memtrace.h"
#include "dintomb.hpp"
#include "jatekos.h"

/// Új adat hozzáaása a dinamikus tömbhöz
/// @param adat - sablonnak megfelelõ típusú tárolóhoz hozzáadandó adat
/// @return - Memóriafoglalási hiba esetén kivételt dob
template <class T>
void DinTomb<T>::hozzaad(T adat) {
    T* uj=new T[++n];       // Eggyel több adatnak helyfoglalás, majd átmásolás az új adattal együtt
    ///Ha nem sikerult foglalni, automatikusan kivetelt dob (std::bad_alloc)
    for(size_t i=0; i<n-1; i++) {
        uj[i]=t[i];         //Kell a T-nek = operator (implicit jó) és a DinTomb-nek [] operator (megcsinálva)
    }
    uj[n-1]=adat;
    delete[] t;             // Régiek felszabadítása és a pointer megfelelõ helyre mutatásának beállítása
    t=uj;
}

/// Adattároló minden adatának felsorolása
template <class T>
void DinTomb<T>::kiir() const{
    for(size_t i=0; i<n-1; i++) {
        std::cout<<t[i]<<" ";   //szükséges, hogy T-nek legyen << operatora (és index is)
    }
    std::cout<< t[n-1] <<std::endl;
}

/// Kiír fv specializálása Jatekos pointereket tárolásakor
template <>
void DinTomb<Jatekos*>::kiir() const{
    for(size_t i=0; i<n-1; i++) {
        std::cout<<*t[i]<<" ";  //Kell a Jatekos-nak << operator (és index is)
    }
    std::cout<< *t[n-1] <<std::endl;
}

/// Destruktor
/// Felszabadítja a dinamikusan foglalt adattömböt
template<class T>
DinTomb<T>::~DinTomb() {
    delete[] t;
}

/// Destruktor Jatekos* tárolása esetén
/// Mivel a Jatekosok is dinamikusan vannak foglalva,
/// és a DinTomb feladata ennek a felszabadítása, ezért
/// elõbb végigmegy a játékosokon, majd utána szabadítja fel a pointereket tároló tömböt
template<>
DinTomb<Jatekos*>::~DinTomb() {
    for(size_t i=0; i<n; i++) {
        delete (t[i]);
    }
    delete[] t;
}



/// Teszt a DinTomb függvényeinek helyes mûködésére
void teszt() {
    DinTomb<int> b;
    b.hozzaad(1);
    b.hozzaad(2);
    b.kiir();
    DinTomb<Jatekos*> a;
    a.hozzaad(new Ember(true, "Mikorka Kalman"));
    a.hozzaad(new CoPilot(false, "Budipa Piroska", 3));
    a.hozzaad(new RandomPC(false));
    a.kiir();
}
