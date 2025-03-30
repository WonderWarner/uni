/**
 * \file dintomb.hpp
 * DinTomb sablon tetsz�leges mennyis�g�, tetsz�leges t�pus dinamikus t�rol�s�hoz
 * Sablon specializ�ci�ja: dinamikusan foglalt j�t�kosok pointereinek t�rol�s�ra
 */

#ifndef DINTOMB_HPP_INCLUDED
#define DINTOMB_HPP_INCLUDED

#include "memtrace.h"

///DinTomb oszt�ly sablon dinamikus t�rol�shoz
/// @param T adatt�pus
template <class T>
class DinTomb {
    size_t n;   ///<t�rol� aktu�lis m�rete
    T* t;       ///<adathalmaz els� elem�re mutat� pointer
    DinTomb& operator=(const DinTomb& dt); //k�v�lr�l nem hozz�f�rhet�
public:

    ///Default konstruktor
    DinTomb(): n(0) {
        t=NULL;
    }

    /// Indexel�s
    /// @param i - index
    /// @return - referencia az adott index� elemre
    /// @return - hib�s index�rt�k eset�n kiv�telt dob
    const T& operator[](size_t i) const {
        if(i<n&&i>=0) return t[i];
        else throw(std::out_of_range("Indexhiba!"));
    }

    /// Indexel�s konstans f�ggv�nye
    /// @param i - index
    /// @return - referencia az adott index� elemre
    /// @return - hib�s index�rt�k eset�n kiv�telt dob
    T& operator[](size_t i) {
        if(i<n&&i>=0) return t[i];
        else throw(std::out_of_range("Indexhiba!"));
    }

    /// �j adat hozz�a�sa a dinamikus t�mbh�z
    void hozzaad(T);

    /// M�sol� konstruktor
    /// Haszn�lata a programban csak int-re
    /// @param dt - lem�soland� t�rol�
    DinTomb(const DinTomb& dt): n(0) {
        t=NULL;
        ///�j t�rol� l�trehoz�sa �s adatok egyenk�nti �tad�sa
        for(size_t i=0; i<dt.n; i++) {
            hozzaad(dt[i]);
        }
    }

    /// M�ret lek�r�se
    /// @return - t�rol� m�rete
    size_t size() const { return n; }

    ///Adatt�rol� minden adat�nak felsorol�sa
    void kiir() const;

    ///Destruktor
    ~DinTomb();
};


#endif // DINTOMB_HPP_INCLUDED
