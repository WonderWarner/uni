/**
 * \file dintomb.cpp
 * DinTomb sablon oszt�ly �s specializ�lt f�ggv�nyeinek megval�s�t�sa
 */

#include <iostream>
#include <cstring>
#include "memtrace.h"
#include "dintomb.hpp"
#include "jatekos.h"

/// �j adat hozz�a�sa a dinamikus t�mbh�z
/// @param adat - sablonnak megfelel� t�pus� t�rol�hoz hozz�adand� adat
/// @return - Mem�riafoglal�si hiba eset�n kiv�telt dob
template <class T>
void DinTomb<T>::hozzaad(T adat) {
    T* uj=new T[++n];       // Eggyel t�bb adatnak helyfoglal�s, majd �tm�sol�s az �j adattal egy�tt
    ///Ha nem sikerult foglalni, automatikusan kivetelt dob (std::bad_alloc)
    for(size_t i=0; i<n-1; i++) {
        uj[i]=t[i];         //Kell a T-nek = operator (implicit j�) �s a DinTomb-nek [] operator (megcsin�lva)
    }
    uj[n-1]=adat;
    delete[] t;             // R�giek felszabad�t�sa �s a pointer megfelel� helyre mutat�s�nak be�ll�t�sa
    t=uj;
}

/// Adatt�rol� minden adat�nak felsorol�sa
template <class T>
void DinTomb<T>::kiir() const{
    for(size_t i=0; i<n-1; i++) {
        std::cout<<t[i]<<" ";   //sz�ks�ges, hogy T-nek legyen << operatora (�s index is)
    }
    std::cout<< t[n-1] <<std::endl;
}

/// Ki�r fv specializ�l�sa Jatekos pointereket t�rol�sakor
template <>
void DinTomb<Jatekos*>::kiir() const{
    for(size_t i=0; i<n-1; i++) {
        std::cout<<*t[i]<<" ";  //Kell a Jatekos-nak << operator (�s index is)
    }
    std::cout<< *t[n-1] <<std::endl;
}

/// Destruktor
/// Felszabad�tja a dinamikusan foglalt adatt�mb�t
template<class T>
DinTomb<T>::~DinTomb() {
    delete[] t;
}

/// Destruktor Jatekos* t�rol�sa eset�n
/// Mivel a Jatekosok is dinamikusan vannak foglalva,
/// �s a DinTomb feladata ennek a felszabad�t�sa, ez�rt
/// el�bb v�gigmegy a j�t�kosokon, majd ut�na szabad�tja fel a pointereket t�rol� t�mb�t
template<>
DinTomb<Jatekos*>::~DinTomb() {
    for(size_t i=0; i<n; i++) {
        delete (t[i]);
    }
    delete[] t;
}



/// Teszt a DinTomb f�ggv�nyeinek helyes m�k�d�s�re
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
