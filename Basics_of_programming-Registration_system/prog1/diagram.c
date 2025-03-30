#include "diagram.h"

//Ablak letrehozasa - forras: InfoC
void sdl_init(char const *felirat, int szeles, int magas, SDL_Window **pwindow, SDL_Renderer **prenderer) {
    if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
        SDL_Log("Nem indithato az SDL: %s", SDL_GetError());
        exit(1);
    }
    SDL_Window *window = SDL_CreateWindow(felirat, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, szeles, magas, 0);
    if (window == NULL) {
        SDL_Log("Nem hozhato letre az ablak: %s", SDL_GetError());
        exit(1);
    }
    SDL_Renderer *renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_SOFTWARE);
    if (renderer == NULL) {
        SDL_Log("Nem hozhato letre a megjelenito: %s", SDL_GetError());
        exit(1);
    }
    SDL_RenderClear(renderer);

    *pwindow = window;
    *prenderer = renderer;
}

//Diagram adatainak kiszamolasa, oszlopok rajzolasa adatoknak megfeleloen
void diagram(Hallgato *elso) {
    //Igy tobbszori futtataskor is mukodik a stringRGBA()
    gfxPrimitivesSetFont(NULL, 0, 0);
    //Ablak letrehozasa
    SDL_Window *window;
    SDL_Renderer *renderer;
    //800x500 keppont meretu ablak
    sdl_init("Jegyek eloszlasa", 1000, 500, &window, &renderer);

    //Szamitasok
    Hallgato *akt=elso;
    //Az indexnek megfelelo osztalyzattal rendelkezo hallgatok (0 is van=meg nincs eredmeny megadva)
    Sint16 db[6]={0};
    Sint16 ossz=0;
    while(akt!=NULL) {
        ossz+=1;
        db[akt->eredmeny.jegy]+=1;
        akt=akt->kov;
    }
    /*Szamitas hattereben:
        Ha minden hallgatonak megegyezne a jegye, akkor az az oszlop y1=50 y2=375 koordinatakkal rendelkezzen.
        Tehat a maximalis oszlopmeret 325 keppont(a 6 oszlop magassaganak osszege is mindig ennyi lesz, kiveve ha mind 0). Az oszlop alja mindig fix, y2=375.
        A valtozo oszlopmagassaghoz az y1=50 koordinatat kell valtoztatni.
        Adott az aktualis darabszam db[i] es az osszes hallgato db szama ossz.
        Ha a ketto hanyadosat nezzuk db/ossz, akkor lesz 1 ha mindenki azzal az eredmennyel rendelkezik.
        Ekkor 50 keppontnak kell lennie, igy az aktualis kezdo keppont y1 koordinataja:
        pix[i]=50. Ha 0 fo rendelkezik a jeggyel pix[i]=375.
        Altalanosan megadhato keplet: pix[i]=375-(db[i]/ossz)*325
        Az elozo kettore helyes eredmenyt kapunk.
        Plusz pelda: 50 hallgatobol 10-en kaptak 4-est. (Ez a hallgatok 20%-a)
        pix[4]=375-(10/50)*325=375-65=310 (A magassaga 375-310=65 lesz ami a 325 max magassagnak tenyleg 20%-a)*/
    Sint16 pix[6];
    char szam[6][100+1];
    char maxi[100+1];
    sprintf(maxi, "%d", ossz);
    for(int i=0; i<6; ++i) {
        sprintf(szam[i], "%d", db[i]);
        pix[i]=(Sint16)((double)375-((double)db[i]/(double)ossz)*(double)325);
    }

    //Oszlopok
    //x1 mindig a hozza tartozo szoveg x koordinataja
    //y1=pix[i] amit ki kellett szamolni a db es ossz fuggvenyeben
    //x2=x1+100
    //y2=375 (az x tengelyt erinti)
    boxRGBA(renderer, 50, pix[1], 150, 375, 117, 0, 0, 255);
    stringRGBA(renderer, 100, pix[1]-10, szam[1], 117, 0, 0, 255);
    boxRGBA(renderer, 200, pix[2], 300, 375, 255, 102, 25, 255);
    stringRGBA(renderer, 250, pix[2]-10, szam[2], 255, 102, 25, 255);
    boxRGBA(renderer, 350, pix[3], 450, 375, 255, 213, 0, 255);
    stringRGBA(renderer, 400, pix[3]-10, szam[3], 255, 213, 0, 255);
    boxRGBA(renderer, 500, pix[4], 600, 375, 170, 255, 0, 255);
    stringRGBA(renderer, 550, pix[4]-10, szam[4], 170, 255, 0, 255);
    boxRGBA(renderer, 650, pix[5], 750, 375, 0, 153, 0, 255);
    stringRGBA(renderer, 700, pix[5]-10, szam[5], 0, 153, 0, 255);
    boxRGBA(renderer, 800, pix[0], 900, 375, 0, 102, 153, 255);
    stringRGBA(renderer, 850, pix[0]-10, szam[0], 0, 102, 153, 255);
    //Tengelyek
    //x tengely vonal, nyil es tengelyfelirat
    lineRGBA(renderer, 10, 375, 980, 375, 255, 255, 255, 255);
    lineRGBA(renderer, 960, 360, 980, 375, 255, 255, 255, 255);
    lineRGBA(renderer, 960, 390, 980, 375, 255, 255, 255, 255);
    stringRGBA(renderer, 920, 405, "Osztalyzat", 255, 255, 255, 255);
    //y tengely vonal, nyil, tengelyfelirat, max ertek bejelolve
    lineRGBA(renderer, 20, 10, 20, 390, 255, 255, 255, 255);
    lineRGBA(renderer, 20, 10, 5, 30, 255, 255, 255, 255);
    lineRGBA(renderer, 20, 10, 35, 30, 255, 255, 255, 255);
    stringRGBA(renderer, 40, 10, "Hallgatok szama (fo)", 255, 255, 255, 255);
    lineRGBA(renderer, 10, 50, 30, 50, 255, 255, 255, 255);
    stringRGBA(renderer, 40, 40, maxi, 255, 255, 255, 255);

    //Szovegek
    stringRGBA(renderer, 50, 390, "1 - Elegtelen", 117, 0, 0, 255);
    stringRGBA(renderer, 200, 390, "2 - Elegseges", 255, 102, 25, 255);
    stringRGBA(renderer, 350, 390, "3 - Kozepes", 255, 213, 0, 255);
    stringRGBA(renderer, 500, 390, "4 - Jo", 170, 255, 0, 255);
    stringRGBA(renderer, 650, 390, "5 - Jeles", 0, 153, 0, 255);
    stringRGBA(renderer, 800, 390, "0 - Nincs adat", 0, 102, 153, 255);
    stringRGBA(renderer, 290, 470, "Kilepeshez: piros x az ablakon vagy kattintas egerrel", 102, 71, 0, 255);

    //Az elvegzett rajzolasok a kepernyore
    SDL_RenderPresent(renderer);
    printf("Statisztika megjelenitese sikeresen megtortent\n");

    //Varunk a kilepesre (X vagy kattintas)
    SDL_Event ev;
    while (SDL_WaitEvent(&ev) && ev.type!=SDL_MOUSEBUTTONDOWN&& ev.type != SDL_QUIT) {
    }

    //Ablak bezarasa
    SDL_Quit();
}
