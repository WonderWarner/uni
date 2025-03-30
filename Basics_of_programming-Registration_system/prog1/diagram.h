#ifndef DIAGRAM_H_INCLUDED
#define DIAGRAM_H_INCLUDED

#include <SDL.h>
#include <SDL2_gfxPrimitives.h>
#include <math.h>
#include <stdlib.h>
#include "alapok.h"

//ablak letrehozasa - forras: InfoC
void sdl_init(char const *felirat, int szeles, int magas, SDL_Window **pwindow, SDL_Renderer **prenderer);

//Diagram adatainak kiszamolasa, oszlopok rajzolasa adatoknak megfeleloen
void diagram(Hallgato *elso);

#endif // DIAGRAM_H_INCLUDED
