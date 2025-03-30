/**
 * \file dintomb.hpp
 * DinTomb sablon tetszõleges mennyiségû, tetszõleges típus dinamikus tárolásához
 * Sablon specializációja: dinamikusan foglalt játékosok pointereinek tárolására
 */

#ifndef DINTOMB_HPP_INCLUDED
#define DINTOMB_HPP_INCLUDED

#include "memtrace.h"

///DinTomb osztály sablon dinamikus tároláshoz
/// @param T adattípus
template <class T>
class DinTomb {
    size_t n;   ///<tároló aktuális mérete
    T* t;       ///<adathalmaz elsõ elemére mutató pointer
    DinTomb& operator=(const DinTomb& dt); //kívülrõl nem hozzáférhetõ
public:

    ///Default konstruktor
    DinTomb(): n(0) {
        t=NULL;
    }

    /// Indexelés
    /// @param i - index
    /// @return - referencia az adott indexû elemre
    /// @return - hibás indexérték esetén kivételt dob
    const T& operator[](size_t i) const {
        if(i<n&&i>=0) return t[i];
        else throw(std::out_of_range("Indexhiba!"));
    }

    /// Indexelés konstans függvénye
    /// @param i - index
    /// @return - referencia az adott indexû elemre
    /// @return - hibás indexérték esetén kivételt dob
    T& operator[](size_t i) {
        if(i<n&&i>=0) return t[i];
        else throw(std::out_of_range("Indexhiba!"));
    }

    /// Új adat hozzáaása a dinamikus tömbhöz
    void hozzaad(T);

    /// Másoló konstruktor
    /// Használata a programban csak int-re
    /// @param dt - lemásolandó tároló
    DinTomb(const DinTomb& dt): n(0) {
        t=NULL;
        ///Új tároló létrehozása és adatok egyenkénti átadása
        for(size_t i=0; i<dt.n; i++) {
            hozzaad(dt[i]);
        }
    }

    /// Méret lekérése
    /// @return - tároló mérete
    size_t size() const { return n; }

    ///Adattároló minden adatának felsorolása
    void kiir() const;

    ///Destruktor
    ~DinTomb();
};


#endif // DINTOMB_HPP_INCLUDED
