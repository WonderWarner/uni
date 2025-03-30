/**
 * \file beolvasosablon.cpp
 * beolvasosablon.cpp beolv_ell_str f�ggv�ny�nek megval�s�t�s�ra
 */

#include "memtrace.h"
#include "beolvasosablon.hpp"

/// Stringre m�k�d� ellen�rz� beolvas�s
/// A megadhat� hat�r a string hossza
/// @param maxmeret - beolvasott string maxim�lis hossza
std::string beolv_ell_str(const size_t& maxmeret) {
    bool hibas=true;
    std::string kapott;
    do {
        //Hibakezel�s: am�g nem ad megfelel� bemenetet, addig nem fogadja el a program
        if(std::cin>>kapott&&kapott.length()<=maxmeret) hibas=false;
        else std::cout<<"Tul hosszu nev, kerem probalja ujra.\n";
    } while(hibas);
    return kapott;
}
