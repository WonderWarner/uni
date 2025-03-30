/**
 * \file beolvasosablon.cpp
 * beolvasosablon.cpp beolv_ell_str függvényének megvalósítására
 */

#include "memtrace.h"
#include "beolvasosablon.hpp"

/// Stringre mûködõ ellenõrzõ beolvasás
/// A megadható határ a string hossza
/// @param maxmeret - beolvasott string maximális hossza
std::string beolv_ell_str(const size_t& maxmeret) {
    bool hibas=true;
    std::string kapott;
    do {
        //Hibakezelés: amíg nem ad megfelelõ bemenetet, addig nem fogadja el a program
        if(std::cin>>kapott&&kapott.length()<=maxmeret) hibas=false;
        else std::cout<<"Tul hosszu nev, kerem probalja ujra.\n";
    } while(hibas);
    return kapott;
}
