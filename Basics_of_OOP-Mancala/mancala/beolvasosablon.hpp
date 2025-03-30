/**
 * \file beolvasosablon.hpp
 * Függvénysablon tetszõleges típusú adatok beolvasására
 * Cél: ne legyen sok ismétlés a kódban (ezért függvény)
 * és hogy ne csak egy valamire mûködjön (sablon, bár most sok mindenre nem fogjuk használni)
 */

#ifndef BEOLVASOSABLON_HPP_INCLUDED
#define BEOLVASOSABLON_HPP_INCLUDED

#include "memtrace.h"
#include <iostream>
#include <limits>

/// Sablon egy felhasználó által megadott adat beolvasásához (char és int most)
/// Addig kér be adatot, amíg nem ad egy megfelelõt a felhasználó
/// @param kezd - legalább ennyi legyen az értéke
/// @param veg - maximum ennyi legyen az értéke
/// @return - a felhasználó által megadott megfelelõ adat
template<typename T>
T beolv_ell(const T& kezd, const T& veg) {
    bool hibas=true;
    T kapott;
    do {
        if(std::cin>>kapott&&kapott>=kezd&&kapott<=veg) hibas=false;
        else {
            //Szöveg esetén sem kerül végtelen ciklusba, hanem új inputot vár
            std::cout<<"Hibas bemenet, kerem probalja ujra.\n";
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    } while(hibas);
    return kapott;
}

/// Stringre mûködõ ellenõrzõ beolvasás
std::string beolv_ell_str(const size_t& maxmeret);

#endif // BEOLVASOSABLON_HPP_INCLUDED
