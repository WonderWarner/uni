/**
 * \file beolvasosablon.hpp
 * F�ggv�nysablon tetsz�leges t�pus� adatok beolvas�s�ra
 * C�l: ne legyen sok ism�tl�s a k�dban (ez�rt f�ggv�ny)
 * �s hogy ne csak egy valamire m�k�dj�n (sablon, b�r most sok mindenre nem fogjuk haszn�lni)
 */

#ifndef BEOLVASOSABLON_HPP_INCLUDED
#define BEOLVASOSABLON_HPP_INCLUDED

#include "memtrace.h"
#include <iostream>
#include <limits>

/// Sablon egy felhaszn�l� �ltal megadott adat beolvas�s�hoz (char �s int most)
/// Addig k�r be adatot, am�g nem ad egy megfelel�t a felhaszn�l�
/// @param kezd - legal�bb ennyi legyen az �rt�ke
/// @param veg - maximum ennyi legyen az �rt�ke
/// @return - a felhaszn�l� �ltal megadott megfelel� adat
template<typename T>
T beolv_ell(const T& kezd, const T& veg) {
    bool hibas=true;
    T kapott;
    do {
        if(std::cin>>kapott&&kapott>=kezd&&kapott<=veg) hibas=false;
        else {
            //Sz�veg eset�n sem ker�l v�gtelen ciklusba, hanem �j inputot v�r
            std::cout<<"Hibas bemenet, kerem probalja ujra.\n";
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    } while(hibas);
    return kapott;
}

/// Stringre m�k�d� ellen�rz� beolvas�s
std::string beolv_ell_str(const size_t& maxmeret);

#endif // BEOLVASOSABLON_HPP_INCLUDED
