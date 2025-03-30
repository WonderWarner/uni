defmodule Nhf1 do
    @moduledoc """
    Számtekercs
    @author "Tömöri Péter András tomoripeter@edu.bme.hu"
    @date   "2024-10-13"
    """

    @type size()  :: integer() # tábla mérete (0 < n)
    @type cycle() :: integer() # ciklus hossza (0 < m <= n)
    @type value() :: integer() # mező értéke (0 < v <= m)

    @type row()   :: integer()       # sor száma (1-től n-ig)
    @type col()   :: integer()       # oszlop száma (1-től n-ig)
    @type field() :: {row(), col()}  # mező koordinátái
    @type index() :: integer()       # kiterített lista elemének sorszáma (1-től (n*n)-ig)

    @type field_value() :: {field(), value()} # mező és értéke
    @type puzzle_desc() :: {size(), cycle(), [field_value()]} # feladvány
    @type matrix_map()  :: %{{row(), col()} => retval()} # készülő eredménymátrix Map struktúrában tárolva
    @type row_map()     :: %{row() => [retval()]} # segédstruktúra sorbeli egyediség gyorsabb ellenőrzéséhez
    @type col_map()     :: %{col() => [retval()]} # segédstruktúra oszlopbeli egyediség gyorsabb ellenőrzéséhez
    @type row_zero()    :: %{row() => integer()}  # segédstruktúra sorbeli kitöltöttség ellenőrzéséhez (0 <= value <= m)
    @type col_zero()    :: %{col() => integer()}  # segédstruktúra oszlopbeli kitöltöttség ellenőrzéséhez (0 <= value <= m)

    @type retval()    :: integer()    # eredménymező értéke (0 <= rv <= m)
    @type solution()  :: [[retval()]] # egy megoldás
    @type solutions() :: [solution()] # összes megoldás

    # ss az sd feladványleíróval megadott feladvány összes megoldásának listája
    @spec helix(sd::puzzle_desc()) :: ss::solutions()
    # A megoldásgeneráló függvénynek átadjuk, hogy hány számsorozatot kell tartalmaznia a listának, és hány 0-nak kell így lennie
    # A kényszereket előre beletesszük a map-ekbe
    # next_fields átadásával annak az első elemét levéve mindig tudjuk merre kell továbbhaladni
    # A kitöltöttség ellenőrzéséhez átadjuk, hogy hány 0 van már a sorokban és oszlopokban, és hogy mennyilehet max
    def helix({n, m, fvs}) do
        {sol, rm, cm} = init_maps(fvs, {%{}, %{}, %{}})
        maps = gen_sols(sol, rm, cm, m, n, n*(n-m), 1, Enum.reverse(next_fields(:R, n-2, n-2, [{1,1}])), n-m, %{}, %{})
        for map <- List.flatten(maps) do for r <- 1..n do for c <- 1..n do
            Map.get(map, {r, c}, 0)
        end end end
    end

    # Kényszerek inicializálása a Map-ekbe
    @spec init_maps(fvs::[field_value()], {sol::matrix_map(), rm::row_map(), cm::col_map()}) :: {sol::matrix_map(), rm::row_map(), cm::col_map()}
    defp init_maps([{field={row, col}, value}|s], {sol, rm, cm}), do:
      init_maps(
        s,
        {Map.put(sol, field, value),
        Map.put(rm, row, [value | Map.get(rm, row, [])]),
        Map.put(cm, col, [value | Map.get(cm, col, [])])
        }
      )
    defp init_maps([], {sol, rm, cm}), do: {sol, rm, cm}

    # fields a bejárásnak megfelelő sorrendben tartalmazza a fieldeket
    # direction, hogy melyik irányba haladunk
    # steps_remaining az adott irányba tett hátralevő lépések száma, ha ez 0 akkor irányt kell váltani
    # start_step_count az adott irányba teendő lépések száma a legutóbbi irányváltoztatás után
    # fields akkumulátorba adjuk hozzá a következő mezőket, amíg végig nem értünk a teljes bejáráson
    # A bejárás során minden négyzetes részmátrixot úgy kezdünk, hogy az első fieldje már adott
    @spec next_fields(direction::atom(), steps_remaining::integer(), start_step_count::integer(), fields::[field()]) :: fields::[field()]
    defp next_fields(:R, 0, cnt, fields=[{row, col}|_]), do: next_fields(:D, cnt, cnt, [{row, col+1}|fields])
    defp next_fields(:R, rem, cnt, fields=[{row, col}|_]), do: next_fields(:R, rem-1, cnt, [{row, col+1}|fields])
    defp next_fields(:D, 0, cnt, fields=[{row, col}|_]), do: next_fields(:L, cnt, cnt, [{row+1, col}|fields])
    defp next_fields(:D, rem, cnt, fields=[{row, col}|_]), do: next_fields(:D, rem-1, cnt, [{row+1, col}|fields])
    defp next_fields(:L, 0, cnt, fields=[{row, col}|_]), do: next_fields(:U, cnt-1, cnt-1, [{row, col-1}|fields]) # felfele csak az utolsó előttiig megyünk, ezért cnt-1
    defp next_fields(:L, rem, cnt, fields=[{row, col}|_]), do: next_fields(:L, rem-1, cnt, [{row, col-1}|fields])
    # Páratlan mátrix esetén felfele a végén nem csak az aktuális field-et kell hozzáadni, hanem a középsőt is
    defp next_fields(:U, 0, 0, fields=[{row, col}|_]), do: [{row-1, col+1}|[{row-1, col}|fields]]
    defp next_fields(:U, 0, cnt, fields=[{row, col}|_]), do: next_fields(:R, cnt-1, cnt-1, [{row-1, col+1}|[{row-1, col}|fields]])
    # Páros mátrix esetén még meghívjuk felfele, de -1-gyel. A lista elkészült, vissza lehet térni vele
    defp next_fields(:U, -1, _, fields), do: fields
    defp next_fields(:U, rem, cnt, fields=[{row, col}|_]), do: next_fields(:U, rem-1, cnt, [{row-1, col}|fields])

    # Az összes megoldás létrehozása: új value felvétele a számtekercs szerinti bejárás alapján, minden kényszer vizsgálatával
    # sol az aktuális mátrix, ennek részei találhatók rm és cm segédstruktúrákban
    # m a számsorozatok legnagyobb száma
    # rem a hátralévő számsorozatok száma (0 <= rem <= n)
    # zero a még hozzáadható 0-k száma (0 <= zero <= len-n*m)
    # a következő value amit hozzáadhatunk a permutációhoz vagy nextval vagy 0
    # next_fields a hátralevő mezőket tartalmazza a bejárás sorrendjében
    # max_zero a megengedett maximális 0-k száma adott sorban vagy oszlopban, értéke n-m
    # rzm és czm a sorok és oszlopok kitöltöttségét tartalmazó segéd Map-ek: adott sor/oszlophoz tárolja, hogy hány 0 van már benne
    # sols az összes megoldás
    @spec gen_sols(sol::matrix_map(), rm::row_map(), cm::col_map(), m::cycle(), rem::integer(), zero::integer(), nextval::value(), next_fields::[field()], max_zero::integer(), rzm::row_zero(), czm::col_zero()) :: sols::[matrix_map()]
    # Ha már minden mezőt kitöltöttünk, akkor az aktuális megoldást hozzáadjuk az eredményekhez
    defp gen_sols(sol, _, _, _, 0, 0, _, _, _, _, _), do: [sol]
    # Ha még nem vagyunk kész, akkor a következő value lehetséges értékeire ellenőrizzük a feltételek helyességét
    defp gen_sols(sol, rm, cm, m, rem, zero, nextval, [field|next_fields], max_zero, rzm, czm) do
        [check_and_create_sol(sol, rm, cm, m, rem, zero, 0, nextval, [field|next_fields], max_zero, rzm, czm)|
        check_and_create_sol(sol, rm, cm, m, rem, zero, nextval, nextval, [field|next_fields], max_zero, rzm, czm)]
    end

    # Az új mező hozzáadása a megoldáshoz: ellenőrzi, hogy az új érték rendelkezésre áll-e még, és ha igen, a feladvány minden kényszerének teljesülését ellenőrzi
    # value az új mező értéke, minden más paraméter a gen_sols-ból
    @spec check_and_create_sol(sol::matrix_map(), rm::row_map(), cm::col_map(), m::cycle(), rem::integer(), zero::integer(), value::retval(), nextval::value(), next_fields::[field()], max_zero::integer(), rzm::row_zero(), czm::col_zero()) :: sols::[matrix_map()]
    # Ha az új érték 0 és még van hozzáadható 0, akkor ellenőrizzük rá a feltételeket és siker esetén folytatjuk a generálást
    defp check_and_create_sol(sol, rm, cm, m, rem, zero, 0, nextval, [field|next_fields], max_zero, rzm, czm) when zero>0 do
      {map_passed, {new_sol, row_map, col_map, new_rzm, new_czm}} = update_and_check_maps(sol, rm, cm, field, 0, max_zero, rzm, czm)
      if(map_passed) do
        gen_sols(new_sol, row_map, col_map, m, rem, zero-1, nextval, next_fields, max_zero, new_rzm, new_czm)
      else []
      end
    end
    # Ha az új érték nem 0 és már nem csak 0-kat kell hozzáadni a megoldáshoz,
    # akkor ellenőrizzük rá a feltételeket és siker esetén folytatjuk a generálást
    defp check_and_create_sol(sol, rm, cm, m, rem, zero, value, _, [field|next_fields], max_zero, rzm, czm) when rem>0 do
      {map_passed, {new_sol, row_map, col_map, new_rzm, new_czm}} = update_and_check_maps(sol, rm, cm, field, value, max_zero, rzm, czm)
      if(map_passed) do
        if(value == m) do # Ha végigértünk egy számsorozaton, akkor eggyel kevesebbet kell használni a kitöltés során, és újra az 1-es érték jön
          gen_sols(new_sol, row_map, col_map, m, rem-1, zero, 1, next_fields, max_zero, new_rzm, new_czm)
        else gen_sols(new_sol, row_map, col_map, m, rem, zero, value+1, next_fields, max_zero, new_rzm, new_czm)
        end
      else []
      end
    end
    # Ha a hozzáadandó értékből már nem áll rendelkezésre, akkor ez a megoldás sikertelen
    defp check_and_create_sol(_, _, _, _, _, _, _, _, _, _, _, _), do: []

    # Leellenőrzi, hogy az új mező az adott sorban és oszlopban egyedi-e
    # Leellenőrzi az előre megadott field általi kényszerek teljesülését és szükség esetén hozzáadja az új mezőt
    # Leellenőrzi a sorok és oszlopok kitöltöttségét
    # Ha teljesít mindent, akkor frissíti a Map-eket
    @spec update_and_check_maps(sol::matrix_map(), rm::row_map(), cm::col_map(), field::field(), value::retval(), max_zero::integer(), rzm::row_zero(), czm::col_zero()) :: {result::boolean(), {new_sol::matrix_map(), new_rm::row_map(), new_cm::col_map(), new_rzm::row_zero(), new_czm::col_zero()}}
    # Ha a value 0 akkor nem kell update-elni az értékeket tároló Map-eket és ellenőrizni a sor/oszlop egyediséget
    # de a constraint és kitöltöttség ellenőrzése szükséges
    defp update_and_check_maps(sol, rm, cm, field={row, col}, 0, max_zero, rzm, czm) do
      {constraint_passed, _} = satisfy_constraints(0, Map.get(sol, field))
      if(constraint_passed) do
        zero_accepted = Map.get(rzm, row, 0) < max_zero and Map.get(czm, col, 0) < max_zero
        if(zero_accepted) do
            {true, {sol, rm, cm, Map.put(rzm, row, Map.get(rzm, row, 0)+1), Map.put(czm, col, Map.get(czm, col, 0)+1)}}
        else {false, {sol, rm, cm, rzm, czm}} end
      else {false, {sol, rm, cm, rzm, czm}} end
    end
    # Ha a value nem 0 akkor a constraint és sor/oszlop egyediség ellenőrzése után
    # a Map-ek frissítésére lehet szükség
    # A kitöltöttség ellenőrzése ebben az esetben nem szükséges, rzm és czm nem módosulnak
    defp update_and_check_maps(sol, rm, cm, field={row, col}, value, _, rzm, czm) do
      {constraint_passed, should_update_map} = satisfy_constraints(value, Map.get(sol, field))
      if(constraint_passed) do
        if(should_update_map) do
          rvs = Map.get(rm, row, [])
          row_unique = Enum.all?(rvs, fn v -> v != value end)
          if(row_unique) do
            cvs = Map.get(cm, col, [])
            col_unique = Enum.all?(cvs, fn v -> v != value end)
            if(col_unique) do
              {true, {Map.put(sol, field, value), Map.put(rm, row, [value | rvs]), Map.put(cm, col, [value | cvs]), rzm, czm}}
            else {false, {sol, rm, cm, rzm, czm}} end
          else {false, {sol, rm, cm, rzm, czm}} end
        else {true, {sol, rm, cm, rzm, czm}} end
      else {false, {sol, rm, cm, rzm, czm}} end
    end

    # result az eredmény, hogy az adott mezőre vonatkozó kezdeti kényszerek teljesülnek-e, és should_update tárolja, hogy az új értéket fel kell-e venni a Map-ekben
    # Ha a map-ben szerepel érték a fieldnél akkor map_value tárolja azt, ha nem akkor nil az értéke
    @spec satisfy_constraints(value::retval(), map_value::integer()) :: {result::boolean(), should_update::boolean()}
    # Ha nincs kényszer és az érték 0, akkor megfelel, de nem kell frissíteni
    defp satisfy_constraints(0, nil), do: {true, false}
    # Ha nincs kényszer és az érték nem 0, akkor megfelel és frissíteni kell
    defp satisfy_constraints(_, nil), do: {true, true}
    # Ha van kényszer, és a két érték megegyezik, akkor megfelel, de nem kell frissíteni
    defp satisfy_constraints(value, value), do: {true, false}
    # Egyébként nem felel meg
    defp satisfy_constraints(_, _), do: {false, false}

  end


Nhf1.helix({2, 2, [{{2,1}, 2}, {{1,1},1}]}) |> IO.inspect()


defmodule Nhf1Testcases do

  testcases = # %{key => {size, cycle, constraints, solutions}}
    %{
      0 => {3, 2, [], [[[0, 1, 2], [1, 2, 0], [2, 0, 1]], [[0, 1, 2], [2, 0, 1], [1, 2, 0]], [[1, 2, 0], [2, 0, 1], [0, 1, 2]]]},
      1 => {4, 2, [{{1, 1}, 1}, {{1, 4}, 2}], [[[1, 0, 0, 2], [0, 1, 2, 0], [0, 2, 1, 0], [2, 0, 0, 1]], [[1, 0, 0, 2], [2, 0, 0, 1], [0, 2, 1, 0], [0, 1, 2, 0]], [[1, 0, 0, 2], [2, 0, 1, 0], [0, 2, 0, 1], [0, 1, 2, 0]]]},
      2 => {4, 1, [{{1, 1}, 1}], [[[1, 0, 0, 0], [0, 0, 0, 1], [0, 0, 1, 0], [0, 1, 0, 0]], [[1, 0, 0, 0], [0, 0, 0, 1], [0, 1, 0, 0], [0, 0, 1, 0]], [[1, 0, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1], [0, 1, 0, 0]], [[1, 0, 0, 0], [0, 0, 1, 0], [0, 1, 0, 0], [0, 0, 0, 1]], [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 0, 1], [0, 0, 1, 0]], [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]]},
      3 => {4, 3, [], []},
      4 => {5, 3, [{{1, 3}, 1}, {{2, 2}, 2}], [[[0, 0, 1, 2, 3], [0, 2, 0, 3, 1], [1, 3, 0, 0, 2], [3, 0, 2, 1, 0], [2, 1, 3, 0, 0]], [[0, 0, 1, 2, 3], [0, 2, 3, 0, 1], [1, 3, 0, 0, 2], [3, 0, 2, 1, 0], [2, 1, 0, 3, 0]]]},
      5 => {6, 3, [{{1, 5}, 2}, {{2, 2}, 1}, {{4, 6}, 1}], [[[1, 0, 0, 0, 2, 3], [0, 1, 2, 3, 0, 0], [0, 3, 1, 2, 0, 0], [0, 2, 3, 0, 0, 1], [3, 0, 0, 0, 1, 2], [2, 0, 0, 1, 3, 0]]]},
      6 => {6, 3, [{{1, 5}, 2}, {{2, 2}, 1}, {{4, 6}, 1}], [[[1, 0, 0, 0, 2, 3], [0, 1, 2, 3, 0, 0], [0, 3, 1, 2, 0, 0], [0, 2, 3, 0, 0, 1], [3, 0, 0, 0, 1, 2], [2, 0, 0, 1, 3, 0]]]},
      7 => {6, 3, [{{2, 4}, 3}, {{3, 3}, 1}, {{3, 6}, 2}, {{6, 1}, 3}], [[[0, 1, 2, 0, 3, 0], [2, 0, 0, 3, 0, 1], [0, 3, 1, 0, 0, 2], [0, 0, 3, 2, 1, 0], [1, 0, 0, 0, 2, 3], [3, 2, 0, 1, 0, 0]]]},
      8 => {7, 3, [{{1, 1}, 1}, {{2, 4}, 3}, {{3, 4}, 1}, {{4, 3}, 3}, {{6, 6}, 2}, {{7, 7}, 3}], [[[1, 0, 0, 2, 0, 3, 0], [0, 1, 2, 3, 0, 0, 0], [0, 3, 0, 1, 2, 0, 0], [0, 2, 3, 0, 0, 0, 1], [3, 0, 0, 0, 0, 1, 2], [0, 0, 1, 0, 3, 2, 0], [2, 0, 0, 0, 1, 0, 3]]]},
      9 => {8, 3, [{{1, 4}, 1}, {{1, 7}, 3}, {{2, 3}, 2}, {{2, 4}, 3}, {{3, 2}, 1}, {{4, 7}, 1}, {{7, 7}, 2}], [[[0, 0, 0, 1, 0, 2, 3, 0], [0, 0, 2, 3, 0, 0, 0, 1], [0, 1, 0, 0, 2, 3, 0, 0], [0, 3, 0, 0, 0, 0, 1, 2], [1, 2, 3, 0, 0, 0, 0, 0], [3, 0, 0, 2, 0, 1, 0, 0], [0, 0, 1, 0, 3, 0, 2, 0], [2, 0, 0, 0, 1, 0, 0, 3]], [[0, 0, 0, 1, 0, 2, 3, 0], [0, 0, 2, 3, 0, 0, 0, 1], [0, 1, 0, 0, 2, 3, 0, 0], [0, 3, 0, 0, 0, 0, 1, 2], [1, 2, 3, 0, 0, 0, 0, 0], [3, 0, 0, 2, 1, 0, 0, 0], [0, 0, 1, 0, 3, 0, 2, 0], [2, 0, 0, 0, 0, 1, 0, 3]], [[0, 0, 0, 1, 0, 2, 3, 0], [0, 0, 2, 3, 0, 0, 0, 1], [0, 1, 0, 2, 0, 3, 0, 0], [0, 3, 0, 0, 0, 0, 1, 2], [1, 2, 3, 0, 0, 0, 0, 0], [3, 0, 0, 0, 2, 1, 0, 0], [0, 0, 1, 0, 3, 0, 2, 0], [2, 0, 0, 0, 1, 0, 0, 3]], [[0, 0, 0, 1, 0, 2, 3, 0], [0, 0, 2, 3, 0, 0, 0, 1], [0, 1, 0, 2, 3, 0, 0, 0], [0, 3, 0, 0, 0, 0, 1, 2], [1, 2, 3, 0, 0, 0, 0, 0], [3, 0, 0, 0, 2, 1, 0, 0], [0, 0, 0, 0, 1, 3, 2, 0], [2, 0, 1, 0, 0, 0, 0, 3]], [[0, 0, 0, 1, 0, 2, 3, 0], [0, 0, 2, 3, 0, 0, 0, 1], [0, 1, 0, 2, 3, 0, 0, 0], [0, 3, 0, 0, 0, 0, 1, 2], [1, 2, 3, 0, 0, 0, 0, 0], [3, 0, 0, 0, 2, 1, 0, 0], [0, 0, 1, 0, 0, 3, 2, 0], [2, 0, 0, 0, 1, 0, 0, 3]]]},
      10 => {8, 4, [{{2, 3}, 4}, {{3, 3}, 2}, {{6, 1}, 1}, {{7, 6}, 3}], [[[0, 0, 1, 2, 3, 4, 0, 0], [0, 0, 4, 0, 1, 2, 3, 0], [0, 0, 2, 3, 0, 0, 4, 1], [3, 1, 0, 4, 0, 0, 0, 2], [2, 4, 0, 0, 0, 0, 1, 3], [1, 3, 0, 0, 0, 0, 2, 4], [0, 2, 0, 1, 4, 3, 0, 0], [4, 0, 3, 0, 2, 1, 0, 0]], [[0, 0, 1, 2, 3, 4, 0, 0], [0, 0, 4, 0, 1, 2, 3, 0], [3, 0, 2, 0, 0, 0, 4, 1], [0, 1, 3, 4, 0, 0, 0, 2], [2, 4, 0, 0, 0, 0, 1, 3], [1, 3, 0, 0, 0, 0, 2, 4], [0, 2, 0, 1, 4, 3, 0, 0], [4, 0, 0, 3, 2, 1, 0, 0]]]},
      11 => {9, 3, [{{1, 7}, 3}, {{3, 1}, 1}, {{6, 1}, 3}, {{6, 2}, 2}, {{6, 6}, 1}, {{8, 4}, 3}, {{9, 2}, 1}], [[[0, 0, 0, 0, 1, 2, 3, 0, 0], [0, 0, 2, 0, 0, 0, 0, 3, 1], [1, 3, 0, 0, 0, 0, 0, 0, 2], [0, 0, 0, 1, 2, 3, 0, 0, 0], [0, 0, 0, 2, 3, 0, 1, 0, 0], [3, 2, 0, 0, 0, 1, 0, 0, 0], [0, 0, 3, 0, 0, 0, 2, 1, 0], [0, 0, 1, 3, 0, 0, 0, 2, 0], [2, 1, 0, 0, 0, 0, 0, 0, 3]]]}
    }
  for i <- 0..map_size(testcases)-1
    do
    {size, cycle, constrains, solutions} = testcases[i]
    {"Test case #{i}",
     Nhf1.helix({size, cycle, constrains}) |> Enum.sort() === solutions
    }
    |> IO.inspect
  end

end
