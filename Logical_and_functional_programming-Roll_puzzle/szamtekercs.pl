% Author: Tömöri Péter András
% Neptun: I4RZ0O 
% Date: 2024.11.29.

:- use_module(library(lists)).
:- use_module(library(between)).
:- use_module(library(sets)).

% :- type feladvany_leiro ---> szt(meret, ciklus, list(adott_elem)).
% :- type meret             == integer (1<=meret).
% :- type ciklus            == integer (1<=ciklus<=meret).
% :- type adott_elem      ---> i(sorszam, oszlopszam, elem).
% :- type sorszam           == integer (1<=sorszam<=meret).
% :- type oszlopszam        == integer (1<=oszlopszam<=meret).
% :- type hatar             == integer (1<=hatar<=meret).
% :- type elem              == integer (1<=elem<=ciklus).

% :- type megoldas          == list(sor).
% :- type sor               == list(ertek).
% :- type ertek             == integer (0<=ertek<=ciklus).

% :- type t_matrix          == list(t_sor).
% :- type t_tekercs         == list(t_ertek).
% :- type t_sor             == list(t_ertek).
% :- type t_ertek           == list(ertek); ertek.

% :- type szukites        ---> sor(sorszam,ertek) ; oszl(oszlopszam,ertek) ; nem.


% Előállítja a számtekercset reprezentáló mátrixot a kezdő tartományokkal.
% Ezután elvégzi a lehetséges kezdeti szűkítéseket, majd az ertek_meghatarozasa predikátumra bízza a számtekercs sorrendjének megfelelő értékbeállításokat.
% Az utolsó érték meghatározása után a helyes megoldásokat egyesével visszaadja.
% :- pred tekercs(feladvany_leiro::in, megoldas::out).
tekercs(szt(Meret, Ciklus, AdottElemek), Megoldas) :-
    kezdotabla(szt(Meret, Ciklus, AdottElemek), Mx0),
    szukites(Meret, Ciklus, Mx0, Mx1),
    ertek_meghatarozasa(Meret, Ciklus, 1, 1, Meret, 1, Mx1, Megoldas).

% A kapott helyre beállítja vagy a meghatározott értéket vagy a 0-t, valamint ellenőrzi, hogy a bejárás véget ért-e.
% :- pred ertek_meghatarozasa(meret::in, ciklus::in, sorszam::in, oszlopszam::in, hatar::in, elem::in, t_matrix::in, megoldas::out).
ertek_meghatarozasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Mx0, Mx) :-
    Mx0 \== [],
    Hatar \== -1,
    nth1(Sorszam, Mx0, Sor),
    nth1(Oszlopszam, Sor, Elem),
    (ertek_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Elem, Mx0, Mx);
    nulla_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Elem, Mx0, Mx)).
% Ha a Határ -1, akkor végigértünk a mátrixon, visszatérünk a kapott mátrixszal.
ertek_meghatarozasa(_, _, _, _, -1, 1, Mx, Mx) :- Mx \== [].

% A kapott helyre beállítja a megadott értéket amennyiben lehetséges/szükséges,
% majd ismételten leellenőrzi a lehetséges szűkítéseket, és továbblép a következő mezőre.
% :- pred ertek_beallitasa(meret::in, ciklus::in, sorszam::in, oszlopszam::in, hatar::in, elem::in, t_ertek::in, t_matrix::in, megoldas::out).
% Ha tartomány van a megfelelő helyen, ami tartalmazza a 0-t: beállítjuk [0]-ra, szűkítünk és lépünk a következő mezőre
ertek_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Elem, Mx0, Mx) :-
    is_list(Elem),
    member(Ertek, Elem),
    elem_modositasa(Sorszam, Oszlopszam, Ertek, Mx0, Mx1),
    sor_tart_frissites(Meret, Ciklus, Sorszam, 1, Ertek, Mx1, Mx2),
    oszlop_tart_frissites(Meret, Ciklus, 1, Oszlopszam, Ertek, Mx2, Mx3),
    szukites(Meret, Ciklus, Mx3, Mx4),
    kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, UjSorszam, UjOszlopszam, UjHatar),
    kovetkezo_ertek(Ciklus, Ertek, UjErtek),
    ertek_meghatarozasa(Meret, Ciklus, UjSorszam, UjOszlopszam, UjHatar, UjErtek, Mx4, Mx).
% Ha már van ott érték, és az van ott ami kell nekünk, akkor csak továbblépünk (ha ez teljesül akkor a nulla_beallitasa nem fog, de nem kell ott is egy másik ág, mert akkor 2-szer mennénk tovább ugyanazzal)
ertek_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Ertek, Mx0, Mx) :-
    kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, UjSorszam, UjOszlopszam, UjHatar),
    kovetkezo_ertek(Ciklus, Ertek, UjErtek),
    ertek_meghatarozasa(Meret, Ciklus, UjSorszam, UjOszlopszam, UjHatar, UjErtek, Mx0, Mx).

% A kapott helyre beállítja a 0-t amennyiben lehetséges/szükséges,
% majd ismételten leellenőrzi a lehetséges szűkítéseket, és továbblép a következő mezőre.
% :- pred nulla_beallitasa(meret::in, ciklus::in, sorszam::in, oszlopszam::in, hatar::in, elem::in, t_ertek::in, t_matrix::in, megoldas::out).
% Ha már 0 van a megfelelő helyen, akkor a következő mezőre lépünk.
nulla_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, 0, Mx0, Mx) :-
    kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, UjSorszam, UjOszlopszam, UjHatar),
    ertek_meghatarozasa(Meret, Ciklus, UjSorszam, UjOszlopszam, UjHatar, Ertek, Mx0, Mx).
% Ha tartomány van a megfelelő helyen, ami tartalmazza a 0-t: beállítjuk [0]-ra, szűkítünk és lépünk a következő mezőre
nulla_beallitasa(Meret, Ciklus, Sorszam, Oszlopszam, Hatar, Ertek, Elem, Mx0, Mx) :-
    is_list(Elem),
    member(0, Elem),
    elem_modositasa(Sorszam, Oszlopszam, 0, Mx0, Mx1),
    sor0_frissites(Meret, Ciklus, Sorszam, 1, Mx1, Mx2),
    oszlop0_frissites(Meret, Ciklus, 1, Oszlopszam, Mx2, Mx3),
    szukites(Meret, Ciklus, Mx3, Mx4),
    kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, UjSorszam, UjOszlopszam, UjHatar),
    ertek_meghatarozasa(Meret, Ciklus, UjSorszam, UjOszlopszam, UjHatar, Ertek, Mx4, Mx).

% Meghatározza a következő hozzáadandó értéket: ha elértük a Ciklus méretét, akkor 1-t ad vissza, egyébként 1+Értéket.
% :- pred kovetkezo_ertek(ciklus::in, elem::in, elem::out).
kovetkezo_ertek(Ciklus, Ciklus, 1) :- !.
kovetkezo_ertek(_, Ertek, UjErtek) :- UjErtek is Ertek+1.


% KHF5 második fele beleolvasztva a KHF6 felhasználását. Szűkítés kényszerek alapján:

% Leszűkíti a mátrixelemek tartományát az egyelemű tartományok alapján amíg tudja, majd
% végigmegy a sorokon kizárásos szűkítést keresve, majd a kizaras_ellenorzese_sorban predikátumra bízza ennek eredményét,
% esetleges további szűkítés keresését az oszlopokban.
% A végső eredményt a szukites_ellenorzese predikátumra bízza.
% :- pred szukites(meret::in, ciklus::in, t_matrix::in, t_matrix::out).
szukites(Meret, Ciklus, Mx0, Mx) :-
   ismert_szukites_iteracio(Meret, Ciklus, Mx0, Mx1),
   vonalmenti_bejaras(1, Meret, Mx1, 0, Ciklus, Mx2, Szam, Ertek),
   kizaras_ellenorzese_sorban(Meret, Ciklus, Mx2, Szam, Ertek, Mx3, _),
   tekercs_szukites(Meret, Ciklus, Mx3, Mx4),
   szukites_ellenorzese(Meret, Ciklus, Mx4, Mx).

% :- pred szukites_ellenorzese(meret::in, ciklus::in, t_matrix::in, szukites::in, t_matrix::out).
% Ha nem találtunk szűkítési lehetőséget visszatérünk a kapott mátrixszal (ez igaz az üres mátrixra is).
szukites_ellenorzese(_, _, Mx, Mx) :- !.
% Ha találtunk szűkítési lehetőséget, akkor rekurzívan meghívjuk a szűkítést.
szukites_ellenorzese(Meret, Ciklus, Mx0, Mx) :-
    szukites(Meret, Ciklus, Mx0, Mx).

% ismert_szukites_iteracio a szukites_vegrehajtas-t meghívva egyszer végigmegy a mátrixon.
% amennyiben a mátrix módosul, újra meghívja magát ezáltal elölről kezdve a mátrix bejárását,
% ha nem módosult, akkor visszatér a kapott Mátrix-szal.
% :- pred ismert_szukites_iteracio(meret::in, ciklus::in, t_matrix::in, t_matrix::out).
ismert_szukites_iteracio(_, _, [], []) :- !. % ha nem megoldható a mátrix, akkor visszatér az üres tömbbel.
ismert_szukites_iteracio(Meret, Ciklus, Mx0, Mx) :-
    szukites_vegrehajtas(1, 1, Meret, Ciklus, Mx0, Szukitett),
    (Mx0 \== Szukitett -> ismert_szukites_iteracio(Meret, Ciklus, Szukitett, Mx), !;
     Mx = Szukitett).

% szukites_vegrehajtas végigmegy a mátrixon és az egyelemű tartományok alapján szűkíti a tartományokat.
% Minden szűkítés után visszalép és újrakezdi a bejárást
% :- pred szukites_vegrehajtas(sorszam::in, oszlopszam::in, meret::in, ciklus::in, t_matrix::in, t_matrix::out).
szukites_vegrehajtas(_, _, _, _, [], []) :- !. % ha nem megoldható a mátrix, akkor visszatér az üres tömbbel.
szukites_vegrehajtas(Sorszam, Oszlopszam, Meret, Ciklus, Aktualis, Szukitett) :-
    nth1(Sorszam, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    egyelemu_ellenorzes(Meret, Ciklus, Sorszam, Oszlopszam, Elem, Aktualis, Szukitett).

% egyelemu_ellenorzes ellenőrzi, hogy az adott elem tartománya egyelemű-e.
% Ha igen, akkor módosítja az elemet a mátrixban,
% elvégzi a szükséges módosításokat a sorban és oszlopban,
% majd visszatér a módosított mátrixszal.
% Ha nem egyelemű tartomány van, akkor továbblép a mátrixban a következő elemre.
% :- pred egyelemu_ellenorzes(meret::in, ciklus::in, sorszam::in, oszlopszam::in, t_ertek::in, t_matrix::in, t_matrix::out).
egyelemu_ellenorzes(Meret, Ciklus, Sorszam, Oszlopszam, [Ertek], Aktualis, Szukitett) :- % ha egyelemű a tartomány, akkor a tartományt az értékkel helyettesíti.
    elem_modositasa(Sorszam, Oszlopszam, Ertek, Aktualis, ElemFrissitve),
    sor_es_oszlop_frissites(Meret, Ciklus, Sorszam, Oszlopszam, Ertek, ElemFrissitve, Szukitett),
    !.
egyelemu_ellenorzes(_, _, _, _, [], _, []) :- !. % ha egy elem sem szerepel a tartományban, a feladvány nem megoldható, ezért visszatér az üres mátrixszal.
egyelemu_ellenorzes(Meret, Ciklus, Sorszam, Oszlopszam, _, Aktualis, Modositott) :- % ha nem többelemű a tartomány, akkor megy a következő elemre.
    Oszlopszam \== Meret,  % ha még nem ért a sor végére, akkor a következő oszlopra lép.
    UjOszlopszam is Oszlopszam+1,
    szukites_vegrehajtas(Sorszam, UjOszlopszam, Meret, Ciklus, Aktualis, Modositott).
egyelemu_ellenorzes(Meret, Ciklus, Sorszam, Meret, _, Aktualis, Modositott) :- % ha végigért a soron, de még nem az utolsó az, akkor a következő sorra lép.
    Sorszam \== Meret,
    UjSorszam is Sorszam+1,
    szukites_vegrehajtas(UjSorszam, 1, Meret, Ciklus, Aktualis, Modositott).
egyelemu_ellenorzes(Meret, _, Meret, Meret, _, Aktualis, Aktualis). % ha végigért a mátrixon, akkor visszatér a kapott mátrixszal.

% Az mátrix adott sorában kicseréli a listában kapott indexek értékét a kapott értékre
% :- pred elemek_modositasa(list(integer)::in, sorszam::in, t_ertek::in, t_matrix::in, t_matrix::out).
elemek_modositasa([], _, _, Mx, Mx).    % Ha már nincs több cserélendő elem visszatér a mátrix-szal
elemek_modositasa([Index|Maradek], Vonalszam, Ertek, Mx0, Mx) :-
    elem_modositasa(Vonalszam, Index, Ertek, Mx0, Mx1),     % Ha van még elem kicseréljük
    elemek_modositasa(Maradek, Vonalszam, Ertek, Mx1, Mx).  % És megyünk tovább az elemeken

% elem_modositasa módosítja az adott elemet a mátrixban a megadott értékre.
% :- pred elem_modositasa(sorszam::in, oszlopszam::in, integer::in, t_matrix::in, t_matrix::out).
elem_modositasa(Sorszam, Oszlopszam, Ertek, Aktualis, Modositott) :-
    nth1(Sorszam, Aktualis, Sor),                       % sor kiválasztása
    elem_csere(Oszlopszam, Ertek, Sor, UjSor),          % elem cseréje a sorban
    elem_csere(Sorszam, UjSor, Aktualis, Modositott).   % sor cseréje a mátrixban

% elem_csere a megadott lista megadott indexén az elemet lecseréli a megadott értékre és visszatér a módosított listával.
% :- pred elem_csere(sorszam::in, integer::in, t_sor::in, t_sor::out).
elem_csere(Index, Ertek, Lista, UjLista) :-
    nth1(Index, Lista, _, Maradek),
    nth1(Index, UjLista, Ertek, Maradek).

% sor_es_oszlop_frissites frissíti a megadott elem sorát és oszlopát az adott elem értékétől függően.
% Ha az érték 0, akkor a 0-k frissítése történik meg sor0_frissites és oszlop0_frissites segítségével.
% Ha az érték nem 0, akkor a tartományok frissítése történik meg sor_tart_frissites és oszlop_tart_frissites segítségével.
% :- pred sor_es_oszlop_frissites(meret::in, ciklus::in, sorszam::in, oszlopszam::in, integer::in, t_matrix::in, t_matrix::out).
sor_es_oszlop_frissites(_, _, _, _, _, [], []) :- !.
sor_es_oszlop_frissites(Meret, Ciklus, Sorszam, Oszlopszam, Ertek, Aktualis, Szukitett) :-
    Ertek \== 0,
    sor_tart_frissites(Meret, Ciklus, Sorszam, 1, Ertek, Aktualis, SorFrissitve),
    oszlop_tart_frissites(Meret, Ciklus, 1, Oszlopszam, Ertek, SorFrissitve, Szukitett).
sor_es_oszlop_frissites(Meret, Ciklus, Sorszam, Oszlopszam, 0, Aktualis, Szukitett) :-
    sor0_frissites(Meret, Ciklus, Sorszam, 1, Aktualis, SorFrissitve),
    oszlop0_frissites(Meret, Ciklus, 1, Oszlopszam, SorFrissitve, Szukitett).

% sor_tart_frissites és oszlop_tart_frissites frissítik a sorban/oszlopban a tartományokat az adott elem tartományból kivételével.
% A sorok és oszlopok frissítése során ha egy elem már nem tömb, akkor azt kihagyjuk.
% A két predikátum a soron/oszlopon elemenként megy végig 1-től Meret-ig
% :- pred sor_tart_frissites(meret::in, ciklus::in, sorszam::in, oszlopszam::in, integer::in, t_matrix::in, t_matrix::out).
sor_tart_frissites(_, _, _, _, _, [], []) :- !.
sor_tart_frissites(Meret, Ciklus, Sorszam, AktOszlopszam, Ertek, Aktualis, Szukitett) :-
    AktOszlopszam \== Meret,
    nth1(Sorszam, Aktualis, Sor),
    nth1(AktOszlopszam, Sor, Elem),
    elem_kivetele(Ertek, Elem, UjElem),
    elem_modositasa(Sorszam, AktOszlopszam, UjElem, Aktualis, Modositott),
    UjOszlopszam is AktOszlopszam+1,
    sor_tart_frissites(Meret, Ciklus, Sorszam, UjOszlopszam, Ertek, Modositott, Szukitett).
sor_tart_frissites(Meret, _, Sorszam, Meret, Ertek, Aktualis, Szukitett) :-
    nth1(Sorszam, Aktualis, Sor),
    nth1(Meret, Sor, Elem),
    elem_kivetele(Ertek, Elem, UjElem),
    elem_modositasa(Sorszam, Meret, UjElem, Aktualis, Szukitett).

% :- pred oszlop_tart_frissites(meret::in, ciklus::in, sorszam::in, oszlopszam::in, integer::in, t_matrix::in, t_matrix::out).
oszlop_tart_frissites(_, _, _, _, _, [], []) :- !.
oszlop_tart_frissites(Meret, Ciklus, AktSorszam, Oszlopszam, Ertek, Aktualis, Szukitett) :-
    AktSorszam \== Meret,
    nth1(AktSorszam, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    elem_kivetele(Ertek, Elem, UjElem),
    elem_modositasa(AktSorszam, Oszlopszam, UjElem, Aktualis, Modositott),
    UjSorszam is AktSorszam+1,
    oszlop_tart_frissites(Meret, Ciklus, UjSorszam, Oszlopszam, Ertek, Modositott, Szukitett).
oszlop_tart_frissites(Meret, _, Meret, Oszlopszam, Ertek, Aktualis, Szukitett) :-
    nth1(Meret, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    elem_kivetele(Ertek, Elem, UjElem),
    elem_modositasa(Meret, Oszlopszam, UjElem, Aktualis, Szukitett).

% sor0_frissites és oszlop0_frissites frissítik a sorban/oszlopban az összes [0]-t 0-ra, majd
% megszámolja a 0-kat és ha a szám megegyezik a Meret-Ciklus értékével, akkor
% a sorban/oszlopban a tartományokból kiveszi a 0-t, ha pedig nagyobb, mint Meret-Ciklus,
% akkor a mátrixot kicseréli üres tömbre.
% A két predikátum a soron/oszlopon elemenként megy végig 1-től Meret-ig
% :- pred sor0_frissites(meret::in, ciklus::in, sorszam::in, oszlopszam::in, t_matrix::in, t_matrix::out).
sor0_frissites(_, _, _, _, [], []) :- !.
sor0_frissites(Meret, Ciklus, Sorszam, AktOszlopszam, Aktualis, Szukitett) :-
    AktOszlopszam \== Meret,
    nth1(Sorszam, Aktualis, Sor),
    nth1(AktOszlopszam, Sor, Elem),
    egyelemu_0(Elem, UjElem),
    elem_modositasa(Sorszam, AktOszlopszam, UjElem, Aktualis, Modositott),
    UjOszlopszam is AktOszlopszam+1,
    sor0_frissites(Meret, Ciklus, Sorszam, UjOszlopszam, Modositott, Szukitett).
sor0_frissites(Meret, Ciklus, Sorszam, Meret, Aktualis, Szukitett) :-
    nth1(Sorszam, Aktualis, Sor),
    nth1(Meret, Sor, Elem),
    egyelemu_0(Elem, UjElem),
    elem_modositasa(Sorszam, Meret, UjElem, Aktualis, Modositott),
    % mivel végigértünk meg kell számolni hány db 0 van a sorban
    nth1(Sorszam, Modositott, UjSor),
    include(=(0), UjSor, Nullak),
    length(Nullak, NullakSzama),
    nullak_szama_sorban(NullakSzama, Meret, Ciklus, Sorszam, Modositott, Szukitett).

% :- pred oszlop0_frissites(meret::in, ciklus::in, sorszam::in, oszlopszam::in, t_matrix::in, t_matrix::out).
oszlop0_frissites(_, _, _, _, [], []) :- !.
oszlop0_frissites(Meret, Ciklus, AktSorszam, Oszlopszam, Aktualis, Szukitett) :-
    AktSorszam \== Meret,
    nth1(AktSorszam, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    egyelemu_0(Elem, UjElem),
    elem_modositasa(AktSorszam, Oszlopszam, UjElem, Aktualis, Modositott),
    UjSorszam is AktSorszam+1,
    oszlop0_frissites(Meret, Ciklus, UjSorszam, Oszlopszam, Modositott, Szukitett).
oszlop0_frissites(Meret, Ciklus, Meret, Oszlopszam, Aktualis, Szukitett) :-
    nth1(Meret, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    egyelemu_0(Elem, UjElem),
    elem_modositasa(Meret, Oszlopszam, UjElem, Aktualis, Modositott),
    % mivel végigértünk meg kell számolni hány db 0 van az oszlopban
    nullak_szamolasa_oszlopban(Meret, 1, Oszlopszam, Modositott, 0, NullakSzama),
    nullak_szama_oszlopban(NullakSzama, Meret, Ciklus, Oszlopszam, Modositott, Szukitett).

% elem_kivetele kiveszi az adott elemet a tartományból.
% Ha nem tartomány az elem, hanem érték, akkor nem csinál semmit.
% :- pred elem_kivetele(integer::in, t_ertek::in, t_ertek::out).
elem_kivetele(Ertek, Lista, UjLista) :-
    is_list(Lista),
    delete(Lista, Ertek, UjLista),
    !.
elem_kivetele(_, Elem, Elem).

% egyelemu_0 lecseréli a [0]-t 0-ra, egyébként nem módosítja az elemet.
% :- pred egyelemu_0(t_ertek::in, t_ertek::out).
egyelemu_0([0], 0) :- !.
egyelemu_0(Elem, Elem).

% Ha az adott sorban n-m nulla van, akkor az adott sorban a tartományokból kiveszi a 0-t,
% ha több, akkor a mátrixot kicseréli üres tömbre,
% ha kevesebb, akkor nem csinál semmit.
% :- pred nullak_szama_sorban(integer::in, meret::in, ciklus::in, sorszam::in, t_matrix::in, t_matrix::out).
nullak_szama_sorban(NullakSzama, Meret, Ciklus, _, Aktualis, Aktualis) :-
    NullakSzama < Meret-Ciklus.
nullak_szama_sorban(NullakSzama, Meret, Ciklus, _, _, []) :-
    NullakSzama > Meret-Ciklus.
nullak_szama_sorban(NullakSzama, Meret, Ciklus, Sorszam, Aktualis, Szukitett) :-
    NullakSzama is Meret-Ciklus,
    sor_tart_frissites(Meret, Ciklus, Sorszam, 1, 0, Aktualis, Szukitett).

% Ha az adott oszlopban n-m nulla van, akkor az adott oszlopban a tartományokból kiveszi a 0-t,
% ha több, akkor a mátrixot kicseréli üres tömbre,
% ha kevesebb, akkor nem csinál semmit.
% :- pred nullak_szama_oszlopban(integer::in, meret::in, ciklus::in, oszlopszam::in, t_matrix::in, t_matrix::out).
nullak_szama_oszlopban(NullakSzama, Meret, Ciklus, _, Aktualis, Aktualis) :-
    NullakSzama < Meret-Ciklus.
nullak_szama_oszlopban(NullakSzama, Meret, Ciklus, _, _, []) :-
    NullakSzama > Meret-Ciklus.
nullak_szama_oszlopban(NullakSzama, Meret, Ciklus, Oszlopszam, Aktualis, Szukitett) :-
    NullakSzama is Meret-Ciklus,
    oszlop_tart_frissites(Meret, Ciklus, 1, Oszlopszam, 0, Aktualis, Szukitett).

% Megszámolja az adott oszlopban levő 0-k számát.
% Végigmegy az oszlopon 1-től Meret-ig és akkumulátorban tárolja az eddigi 0-k számát.
% :- pred nullak_szamolasa_oszlopban(meret::in, sorszam::in, oszlopszam::in, t_matrix::in, integer::in, integer::out).
nullak_szamolasa_oszlopban(Meret, Sorszam, Oszlopszam, Aktualis, EddigiNullak, NullakSzama) :-
    Sorszam \== Meret,
    nth1(Sorszam, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    elem_0_ellenorzes(Elem, Nulla),
    UjNullak is EddigiNullak+Nulla,
    UjSorszam is Sorszam+1,
    nullak_szamolasa_oszlopban(Meret, UjSorszam, Oszlopszam, Aktualis, UjNullak, NullakSzama).
nullak_szamolasa_oszlopban(Meret, Meret, Oszlopszam, Aktualis, EddigiNullak, NullakSzama) :-
    nth1(Meret, Aktualis, Sor),
    nth1(Oszlopszam, Sor, Elem),
    elem_0_ellenorzes(Elem, Nulla),
    NullakSzama is EddigiNullak+Nulla.

% elem_0_ellenorzes ellenőrzi, hogy az adott elem 0-e, ha igen akkor 1-t ad vissza, ha nem akkor 0-t.
% :- pred elem_0_ellenorzes(t_ertek::in, integer::out).
elem_0_ellenorzes(0, 1) :- !.
elem_0_ellenorzes(_, 0).


% KHF6 kizárásos szűkítés részei:

% A sor alapú vonalmenti bejárás eredményének kiértékelése.
% :- pred kizaras_ellenorzese_sorban(meret::in, ciklus::in, t_matrix::in, sorszam::in, ertek::in, t_matrix::out, szukites::out).
kizaras_ellenorzese_sorban(_, _, [], _, _, [], nem) :- !. % Ha a feladványnak nincs megoldása, akkor visszatérünk az üres tömbbel.
% Ha az érték -1, vagyis a sormenti szűkítés során nem jutottunk semmire, akkor az oszlopmenti szűkítés lehetőségét ellenőrizzük és értékeljük ki.
kizaras_ellenorzese_sorban(Meret, Ciklus, Mx0, -1, _, Mx, Szukites) :-
    transpose(Mx0, TMx0),
    vonalmenti_bejaras(1, Meret, TMx0, 0, Ciklus, TMx1, Szam, Ertek),
    transpose(TMx1, Mx1),
    kizaras_ellenorzese_oszlopban(Meret, Ciklus, Mx1, Szam, Ertek, Mx, Szukites),
    !.
% Ha az érték nem -1, akkor a megfelelő sorban talált szűkítést adjuk vissza a módosított mátrixszal.
kizaras_ellenorzese_sorban(_, _, Mx, Szam, Ertek, Mx, sor(Szam, Ertek)) :- Szam \== -1.

% Az oszlop alapú vonalmenti bejárás eredményének kiértékelése.
% :- pred kizaras_ellenorzese_oszlopban(meret::in, ciklus::in, t_matrix::in, oszlopszam::in, ertek::in, t_matrix::out, szukites::out).
% Ha az érték nem -1, akkor a megfelelő oszlopban talált szűkítést adjuk vissza a módosított és már visszatranszponált mátrixszal.
kizaras_ellenorzese_oszlopban(_, _, Mx, Szam, Ertek, Mx, oszl(Szam, Ertek)) :- Szam \== -1.
% Ha az érték -1, akkor nem találtunk semmilyen szűkítési lehetőséget (ez akkor is igaz, ha a mátrix üres, tehát nincs megoldás).
kizaras_ellenorzese_oszlopban(_, _, Mx, -1, _, Mx, nem).

% Sorok/oszlopok bejárása minden lehetséges értékkel a szűkítéshez.
% :- pred vonalmenti_bejaras(sorszam::in, meret::in, t_matrix::in, ertek::in, ciklus::in, t_matrix::out, sorszam::out, ertek::out).
vonalmenti_bejaras(Vonalszam, Meret, Mx0, Ertek, Ciklus, Mx, ModositottVonal, SzukitettErtek) :-
    nth1(Vonalszam, Mx0, Vonal),
    ertek_elofordulasai(Vonal, Ertek, 1, Elofordulasok, ElofordulasokListaban),
    vonal_szukites(Vonalszam, Meret, Mx0, Ertek, Ciklus, Elofordulasok, ElofordulasokListaban, Mx, ModositottVonal, SzukitettErtek).

% ertek_elofordulasai kigyűjti egy listába a kapott listának minden olyan indexét (1-től indexelve),
% aminek értéke vagy a kapott érték, vagy pedig egy olyan lista, amiben szerepel a kapott érték
% Továbbá egy másik listába kigyűjti csak azokat, amik tartományban tárolják az értéket
% :- pred ertek_elofordulasai(t_sor::in, ertek::in, integer::in, list(integer)::out, list(integer)::out).
ertek_elofordulasai([], _, _, [], []).  % ha üres a lista, akkor nem lehet benne érték
ertek_elofordulasai([Tartomany|Maradek], Ertek, Index, [Index|Indexek], [Index|TartomanyIndexek]) :- %  hozzáadjuk az indexet a megoldáshoz,
    is_list(Tartomany),          % ha a lista első eleme egy olyan tartomány,
    member(Ertek, Tartomany),    % amiben az érték benne van.
    UjIndex is Index+1,
    ertek_elofordulasai(Maradek, Ertek, UjIndex, Indexek, TartomanyIndexek),
    !.
ertek_elofordulasai([Ertek|Maradek], Ertek, Index, [Index|Indexek], TartomanyIndexek) :- % ha a lista első eleme az érték,
    UjIndex is Index+1,                                               % akkor ennek az indexét hozzáadjuk a megoldáshoz
    ertek_elofordulasai(Maradek, Ertek, UjIndex, Indexek, TartomanyIndexek),
    !.
ertek_elofordulasai([_|Maradek], Ertek, Index, Indexek, TartomanyIndexek) :-  % ha az előbbiek nem teljesülnek,
    UjIndex is Index+1,                                                       % akkor nem adjuk hozzá az indexet.
    ertek_elofordulasai(Maradek, Ertek, UjIndex, Indexek, TartomanyIndexek).    % és haladunk tovább a vonalon

% Adott sor/oszlop szűkítésének ellenőrzése adott értékre.
% :- pred vonal_szukites(sorszam::in, meret::in, t_matrix::in, ertek::in, ciklus::in, list(integer)::in, list(integer)::in, t_matrix::out, sorszam::out, ertek::out).
% Ha az érték 0 és Elofordulasok mérete kisebb mint Meret-Ciklus, akkor a feladatnak nincs megoldása
vonal_szukites(_, Meret, _, 0, Ciklus, Elofordulasok, _, [], -1, -1) :-
    length(Elofordulasok, ElofordulasokSzama),
    ElofordulasokSzama < Meret-Ciklus,
    !.
% Ha az érték 0, Elofordulasok mérete egyenlő Meret-Ciklussal,
% és ElofordulasokListaban mérete nagyobb, mint 0,
% akkor minden listát, ami tartalmazza a 0-t kicseréljük [0]-ra.
% Ezt a frissítést a mátrixban is megtéve visszatérünk vele és a szűkítés értékeivel
vonal_szukites(Vonalszam, Meret, Mx0, 0, Ciklus, Elofordulasok, ElofordulasokListaban, Mx, Vonalszam, 0) :-
    length(Elofordulasok, ElofordulasokSzama),
    ElofordulasokSzama is Meret-Ciklus,
    length(ElofordulasokListaban, ElofordulasokSzamaListaban),
    ElofordulasokSzamaListaban > 0,
    elemek_modositasa(ElofordulasokListaban, Vonalszam, [0], Mx0, Mx),
    !.
% Ha az érték nem 0 és Elofordulasok mérete 0, akkor a feladatnak nincs megoldása
vonal_szukites(_, _, _, Ertek, _, [], _, [], -1, -1) :-
    Ertek \== 0,
    !.
% Ha az érték nem 0, Elofordulasok mérete 1, és ElofordulasokListaban mérete is 1,
% akkor átírjuk [Ertek]-re, frissítjük a mátrixot és visszatérünk vele.
vonal_szukites(Vonalszam, _, Mx0, Ertek, _, [Index], [Index], Mx, Vonalszam, Ertek) :-
    Ertek \== 0,
    elem_modositasa(Vonalszam, Index, [Ertek], Mx0, Mx),
    !.
% Egyébként pedig haladunk tovább a vonalmenti bejárásban a következők szerint:
% Ha még nem érte el a vonal utolsó értékét, akkor az adott vonalat nézzük eggyel nagyobb értékkel
vonal_szukites(Vonalszam, Meret, Mx0, Ertek, Ciklus, _, _, Mx, ModositottVonal, SzukitettErtek) :-
    Ertek \== Ciklus,
    UjErtek is Ertek+1,
    vonalmenti_bejaras(Vonalszam, Meret, Mx0, UjErtek, Ciklus, Mx, ModositottVonal, SzukitettErtek),
    !.
% Ha elérte az utolsó értéket, de még nem az utolsó vonalon van,
% akkor a következő vonalat nézzük 0 értékkel.
vonal_szukites(Vonalszam, Meret, Mx0, Ciklus, Ciklus, _, _, Mx, ModositottVonal, SzukitettErtek) :-
    Vonalszam \== Meret,
    UjVonalszam is Vonalszam+1,
    vonalmenti_bejaras(UjVonalszam, Meret, Mx0, 0, Ciklus, Mx, ModositottVonal, SzukitettErtek),
    !.
% Ha elérte az utolsó vonalat az utolsó értékkel úgy, hogy nem talált szűkítést, visszatér a Mátrixszal
vonal_szukites(Meret, Meret, Mx, Ciklus, Ciklus, _, _, Mx, -1, -1).


% KHF5 első fele, kezdotabla predikátum:

% kezdotabla az szt feladvány-leíró alapján előállítja az annak megfelelő legáltalánosabb Mx tartomány-mátrixot
% :- pred kezdotabla(feladvany_leiro::in, t_matrix::out).
kezdotabla(szt(Meret, Ciklus, Adottak), Mx) :-
    tartomany(Meret, Ciklus, Tartomany),
    kezdo_matrix(Meret, Ciklus, Adottak, Tartomany, Mx).

% kezdotabla az szt feladvány-leíró alapján előállítja az annak megfelelő legáltalánosabb Mx tartomány-mátrixot
% :- pred kezdotabla(feladvany_leiro::in, t_matrix::out).
tartomany(Meret, Meret, Tartomany) :-
    numlist(1, Meret, Tartomany).
tartomany(Meret, Ciklus, Tartomany) :-
    Meret \== Ciklus,
    numlist(0, Ciklus, Tartomany).

% kezdo_matrix létrehozza a kiindulási tartománymátrixot az adott értékekre szűkítéssel
% :- pred kezdo_matrix(meret::in, ciklus::in list(adott_elem)::in, list(integer)::in, t_matrix::out).
kezdo_matrix(Meret, Ciklus, Adottak, Tartomany, Mx) :-
    length(Sor, Meret),
    maplist(=(Tartomany), Sor),
    length(Mx0, Meret),
    maplist(=(Sor), Mx0),
    adottak_szukitese(Meret, Ciklus, Adottak, Mx0, Mx).

% Előre megadott értékek beállítása a mátrixban és kivétele a szükséges tartományokból
% Kezeli azt is ha 0-t adnak meg előre!
% :- pred adottak_szukitese(meret::in, ciklus::in, list(adott_elem)::in, t_matrix::in, t_matrix::out).
adottak_szukitese(_, _, [], Mx, Mx).
adottak_szukitese(Meret, Ciklus, [i(Sorszam, Oszlopszam, Ertek)|Maradek], Mx0, Mx) :-
    elem_modositasa(Sorszam, Oszlopszam, Ertek, Mx0, Mx1),
    sor_es_oszlop_frissites(Meret, Ciklus, Sorszam, Oszlopszam, Ertek, Mx1, Mx2),
    adottak_szukitese(Meret, Ciklus, Maradek, Mx2, Mx).

% A tekercsbeli jelenlegi helyzetünk alapján meghatározza a számtekercs bejárása szerinti következő mezőt.
% :- pred kovetkezo_lepes(meret::in, sorszam::in, oszlopszam::in, hatar::in, sorszam::out, oszlopszam::out, hatar::out).
kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, Sorszam, UjOszlopszam, Hatar) :-
    Sorszam is Meret - Hatar + 1,
    Oszlopszam < Hatar,
    UjOszlopszam is Oszlopszam + 1,
    !. % Ha az aktuális réteg felső sorában vagyunk és még nem értünk az utolsó oszlophoz, akkor jobbra haladunk.
kovetkezo_lepes(_, Sorszam, Oszlopszam, Hatar, UjSorszam, Oszlopszam, Hatar) :-
    Oszlopszam is Hatar,
    Sorszam < Hatar,
    UjSorszam is Sorszam + 1,
    !. % Ha az aktuális réteg jobb szélén vagyunk és még nem értünk az utolsó sorhoz, akkor lefelé haladunk.
kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, Sorszam, UjOszlopszam, Hatar) :-
    Sorszam is Hatar,
    Oszlopszam > Meret - Hatar + 1,
    UjOszlopszam is Oszlopszam - 1,
    !. % Ha az aktuális réteg alsó sorában vagyunk és még nem értünk az utolsó oszlophoz, akkor balra haladunk.
kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, UjSorszam, Oszlopszam, Hatar) :-
    Sorszam > Meret - Hatar + 2,
    Oszlopszam is Meret - Hatar + 1,
    UjSorszam is Sorszam - 1,
    !. % Ha az aktuális réteg bal szélén vagyunk és még nem értünk a második sorhoz, akkor felfelé haladunk.
kovetkezo_lepes(Meret, Sorszam, Oszlopszam, Hatar, Sorszam, UjOszlopszam, UjHatar) :-
    Sorszam is Meret - Hatar + 2,
    Oszlopszam is Meret - Hatar + 1,
    UjOszlopszam is Oszlopszam + 1,
    Hatar \== UjOszlopszam, % Feltéve, hogy nem páros mátrix utolsó mezőjéhez értünk.
    UjHatar is Hatar - 1,
    !. % Ha az aktuális réteg bal szélén elértük az utolsó elemet is, akkor a következő rétegre lépünk jobbra, a határt csökkentjük.
kovetkezo_lepes(Meret, _, _, Hatar, -1, -1, -1) :-
    Hatar is Meret - Hatar + 1,
    !. % Páratlan mátrix esetén, ha az utolsó mezőhöz értünk.
kovetkezo_lepes(_, _, _, _, -1, -1, -1). % Egyébként páros mátrix utolsó mezőjéhez értünk.


% KHF7 és kiegészítése

% Tartományok szűkítése a tekeredő vonal mentén mindkét irányba
% :- pred tekercs_szukites(meret::in, ciklus::in, t_matrix::in, t_matrix::out).
tekercs_szukites(Meret, Ciklus, Mx0, Mx) :-
    matrix_tekercs(Mx0, Tekercs),
    utolso_pozitiv(Ciklus, Tekercs, [Ciklus], EgyIranybaSzukitett),
    reverse(EgyIranybaSzukitett, EgyIranybaSzukitettForditott),
    utolso_pozitiv_forditott(Ciklus, EgyIranybaSzukitettForditott, [1], KetIranybaSzukitettForditott),
    reverse(KetIranybaSzukitettForditott, KetIranybaSzukitett),
    matrix_inicializalas(Meret, Mx),
    matrix_tekercs(Mx, KetIranybaSzukitett).

% Adott elemre a tekercsben előző értékek általi szűrés
% :- pred utolso_pozitiv(ciklus::in, t_tekercs::in, t_elem::in, t_tekercs::out).
utolso_pozitiv(_, [], _, []).
% Ha az aktuális elem lista, akkor nem kell aggódni a metszet miatt, annak az eredménye is lista lesz, helyesen
utolso_pozitiv(Ciklus, [Elem|Maradek], UtolsoPozitiv, [PrefixMegenged|Szukitett]) :-
    is_list(Elem),
    ciklikus_rakovetkezok(Ciklus, UtolsoPozitiv, CiklikusRakovetkezok), % Ciklikus rákövetkezők meghatározása az előzőek alapján
    BovitettCiklikusRakovetkezok = [0 | CiklikusRakovetkezok],          % Kiegészítés 0-val
    intersection(BovitettCiklikusRakovetkezok, Elem, PrefixMegenged),   % A lehetséges és engedett értékek metszete lesz a megfelelő
    PrefixMegenged \== [],                                              % Ha nincs megfelelő, nincs megoldás
    kovetkezo_utolso_pozitiv(UtolsoPozitiv, PrefixMegenged, KovetkezoUtolsoPozitiv), % Következő elem értékeinek meghatározása
    utolso_pozitiv(Ciklus, Maradek, KovetkezoUtolsoPozitiv, Szukitett), % Következő elem ellenőrzése
    !.
% Ha az aktuális elem nem lista, akkor a metszet művelet eredménye az Érték ha benne van, üres lista ha nincs, így elég kikötni, hogy legyen benne és utána az értéket használjuk
utolso_pozitiv(Ciklus, [Elem|Maradek], UtolsoPozitiv, [Elem|Szukitett]) :-
    ciklikus_rakovetkezok(Ciklus, UtolsoPozitiv, CiklikusRakovetkezok),
    BovitettCiklikusRakovetkezok = [0 | CiklikusRakovetkezok],
    member(Elem, BovitettCiklikusRakovetkezok),
    kovetkezo_utolso_pozitiv(UtolsoPozitiv, Elem, KovetkezoUtolsoPozitiv),
    utolso_pozitiv(Ciklus, Maradek, KovetkezoUtolsoPozitiv, Szukitett).

% Minden elemre meghatározzuk az utána várt nem 0 értéket
% :- pred ciklikus_rakovetkezok(ciklus::in, t_elem::in, t_elem::out).
ciklikus_rakovetkezok(_, [], []).
ciklikus_rakovetkezok(Ciklus, [Elem|Maradek], [Kovetkezo|CiklikusRakovetkezok]) :-
    kovetkezo_ertek(Ciklus, Elem, Kovetkezo),
    ciklikus_rakovetkezok(Ciklus, Maradek, CiklikusRakovetkezok).

% Következő elem megengedett értékeinek meghatározása
% Ha az aktuális mező értéke nem lehet 0,
    % akkor csak az aktuális mező megengedett értékei határozzák meg a következő elem megengedett értékeit
% Ha az aktuális mező értékei közt van a 0 is, akkor a jelenlegi lehetséges értékek (a 0-t kivéve),
    % és az előző megengedett lehetséges értékei is meghatározhatják a következő mező lehetséges értékeit
% :- pred kovetkezo_utolso_pozitiv(t_elem::in, t_elem::in, t_elem::out).
kovetkezo_utolso_pozitiv(UtolsoPozitiv, [0|PrefixMegenged], KovetkezoUtolsoPozitiv) :-
    union(UtolsoPozitiv, PrefixMegenged, KovetkezoUtolsoPozitiv),
    !.
kovetkezo_utolso_pozitiv(UtolsoPozitiv, 0, UtolsoPozitiv) :- !.
kovetkezo_utolso_pozitiv(_, PrefixMegenged, PrefixMegenged) :-
    is_list(PrefixMegenged),
    !.
kovetkezo_utolso_pozitiv(_, PrefixMegenged, [PrefixMegenged]).

% Adott elemre a tekercsben előző értékek általi szűrés, de fordított irányban, tehát mindig kisebbnek kell következnie
% :- pred utolso_pozitiv_forditott(ciklus::in, t_tekercs::in, t_elem::in, t_tekercs::out).
utolso_pozitiv_forditott(_, [], _, []).
% Ha az aktuális elem lista, akkor nem kell aggódni a metszet miatt, annak az eredménye is lista lesz, helyesen
utolso_pozitiv_forditott(Ciklus, [Elem|Maradek], UtolsoPozitiv, [PrefixMegenged|Szukitett]) :-
    is_list(Elem),
    ciklikus_rakovetkezok_forditott(Ciklus, UtolsoPozitiv, CiklikusRakovetkezok), % Ciklikus rákövetkezők meghatározása az előzőek alapján
    BovitettCiklikusRakovetkezok = [0 | CiklikusRakovetkezok],          % Kiegészítés 0-val
    intersection(BovitettCiklikusRakovetkezok, Elem, PrefixMegenged),   % A lehetséges és engedett értékek metszete lesz a megfelelő
    PrefixMegenged \== [],                                              % Ha nincs megfelelő, nincs megoldás
    kovetkezo_utolso_pozitiv(UtolsoPozitiv, PrefixMegenged, KovetkezoUtolsoPozitiv), % Következő elem értékeinek meghatározása
    utolso_pozitiv_forditott(Ciklus, Maradek, KovetkezoUtolsoPozitiv, Szukitett), % Következő elem ellenőrzése
    !.
% Ha az aktuális elem nem lista, akkor a metszet művelet eredménye az Érték ha benne van, üres lista ha nincs, így elég kikötni, hogy legyen benne és utána az értéket használjuk
utolso_pozitiv_forditott(Ciklus, [Elem|Maradek], UtolsoPozitiv, [Elem|Szukitett]) :-
    ciklikus_rakovetkezok_forditott(Ciklus, UtolsoPozitiv, CiklikusRakovetkezok),
    BovitettCiklikusRakovetkezok = [0 | CiklikusRakovetkezok],
    member(Elem, BovitettCiklikusRakovetkezok),
    kovetkezo_utolso_pozitiv(UtolsoPozitiv, Elem, KovetkezoUtolsoPozitiv),
    utolso_pozitiv_forditott(Ciklus, Maradek, KovetkezoUtolsoPozitiv, Szukitett).

% Minden elemre meghatározzuk az utána várt nem 0 értéket, a fordított sorrend szerint!
% :- pred ciklikus_rakovetkezok(ciklus::in, t_elem::in, t_elem::out).
ciklikus_rakovetkezok_forditott(_, [], []).
ciklikus_rakovetkezok_forditott(Ciklus, [Elem|Maradek], [Kovetkezo|CiklikusRakovetkezok]) :-
    elozo_ertek(Ciklus, Elem, Kovetkezo),
    ciklikus_rakovetkezok_forditott(Ciklus, Maradek, CiklikusRakovetkezok).

% Adott nem nulla szám előtti legelső nem nulla érték a tekercs szerinti bejárásban
% :- pred elozo_ertek(ciklus::in, ertek::in, ertek::out).
elozo_ertek(Ciklus, 1, Ciklus) :- !.
elozo_ertek(_, Ertek, UjErtek) :-
    UjErtek is Ertek - 1.


% KHF4 Mátrix tekerccsé alakítása

% A mátrix transzponálása és a két mátrix segítségével tekercs létrehozása
% :- pred matrix_tekercs(t_matrix::in, tekercs::out).
matrix_tekercs(Mx0, Tekercs) :-
    transpose(Mx0, Mx0T),
    matrix_kiterites(Mx0, Mx0T, Tekercs).

% matrix_kiterites: Mátrix kiterítése listába a spirális bejárás mentén.
% Az első és utolsó sort a mátrix segítségével adjuk hozzá,
% az első és utolsó oszlopot pedig a mátrix transzponáltját használva.
% A külső réteg levétele után rekurzív hívással határozzuk meg a tekercs hátralevő részét.
% :- pred matrix_kiterites(matrix::in, matrix::in, list(integer)::out)
matrix_kiterites([], [], []).           % Páros mátrix esetén üres listával ér véget a bejárás.
matrix_kiterites([[X]], [[X]], [X]).    % Páratlan mátrix esetén az utolsó elemet hozzáadjuk a megoldáshoz.
% Általános esetben rétegenként haladunk a mátrixban.
matrix_kiterites([ElsoSor|Mtx], [[_11|ElsoOszlop]|MtxT], Spiral) :-   % Mátrix első sorának és oszlopának meghatározása, az első oszlopból kivéve az első elemet
    append(AlacsonyMtxT, [[_1n|UtolsoOszlop]], MtxT),                 % Mátrix utolsó oszlopának meghatározása és első elemének kivétele
    append(AlacsonyMtx, [[_n1|UtolsoSor]], Mtx),                      % Mátrix utolsó sorának meghatározása és első elemének kivétele
    reverse(UtolsoSor, [_nn|UtolsoSorIranyitott]),                    % Mátrix utolsó sorának megfordítása a megfelelő irányhoz az eredetileg utolsó elem kivételével
    reverse(ElsoOszlop, ElsoOszlopIranyitott),                        % Mátrix első oszlopának megfordítása a megfelelő irányhoz
    append(ElsoSor, UtolsoOszlop, FelReteg),                          %\   
    append(FelReteg, UtolsoSorIranyitott, ElsoOszlopNelkul),          % |-- Külső réteg összeállítása darabokból
    append(ElsoOszlopNelkul, ElsoOszlopIranyitott, KulsoReteg),       %/
    matrix_karcsusitas(AlacsonyMtx, UjMtx),                           % Eredeti mátrix soraiból az első és utolsó elemek kivétele
    matrix_karcsusitas(AlacsonyMtxT, UjMtxT),                         % Transzponált mátrix soraiból az első és utolsó elemek kivétele
    matrix_kiterites(UjMtx, UjMtxT, BelsoRetegek),                    % A mátrix belsőbb rétegeinek feldolgozása
    append(KulsoReteg, BelsoRetegek, Spiral).                         % A belső rétegek és a külső réteg összeillesztése az eredménnyé
    
% matrix_karcsusitas: Mátrix frissítése: minden sor első és utolsó elemének kivétele.
% :- pred matrix_karcsusitas(matrix::in, matrix::out)
matrix_karcsusitas([], []).
matrix_karcsusitas([Sor|Mtx], [UjSor|Karcsu]) :-
    lista_levagas(Sor, UjSor),          % Aktuális sor módosítása
    matrix_karcsusitas(Mtx, Karcsu).    % A többi sor módosításarekurzívan

% lista_levagas: Lista első és utolsó elemének eltávolítása
% :- pred lista_levagas(list(integer)::in, list(integer)::out)
lista_levagas([_|ElsoNelkul], Levagott) :-
    append(Levagott, [_], ElsoNelkul).

% N*N-es mátrix létrehozása változókkal a visszatekeréshez
% :- pred matrix_inicializalas(meret::in, matrix::out)
matrix_inicializalas(Meret, Mx) :-
    length(Mx, Meret),                      % A mátrixnak Meret sora legyen
    maplist(sor_inicializalas(Meret), Mx).  % Minden sor Meret hosszú lista változókkal

% Meret hosszú lista létrehozása változókkal
% :- pred sor_inicializalas(meret::in, t_sor::out)
sor_inicializalas(Meret, Sor) :-
    length(Sor, Meret).