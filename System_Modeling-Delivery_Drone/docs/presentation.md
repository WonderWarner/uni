# Presentation

## Speech

A tervezés során különös figyelmet fordítottunk a nyomonkövethetőségre, a megbízhatóságra és a biztonságra.

1️⃣ Követelmények és Use Case-ek
A projekt első fázisában a specifikáció alapján feltártuk a stakeholdereket, az alapvető use-caseket és a funkcionális és nem-funkcionális követelményeket. Például:

F01 – Autonomous Flight: A drónnak önállóan kell teljesítenie a repülési tervet, adatkapcsolat hiányában is.

R02 – Emergency Landing: Hiba esetén biztonságos kényszerleszállást kell végrehajtania.

P06 - Diagnostic Data Flow Rate: A rendszernek legalább 1 Hz-es frekvenciával kell küldenie diagnosztikai adatot.

A követelményeket a FURPS elvek alapján modelleztük, majd a [Traceability] diagram segítségével összekapcsoltuk az érintett use case-ekkel, pl. „carry out Emergency Landing”.

![](a3_screenshots/useCases.png)
![](a3_screenshots/Sequence%20Take%20over%20control.png)

2️⃣ Tervezői döntések: Architektúra és komponensek

Egy fontos döntésünk az volt, hogy a drón moduláris rendszerként épül fel, külön komponensekkel a navigáció, kommunikáció, diagnosztika és energiaellátás számára.

(funckionális dekompozíció)

Például:

A Navigation System egy komplex komponens, amely három alrendszert tartalmaz: Route Management, Location System, Sensor Management.

A Task Execution System validálja a bejövő TaskData-t, mielőtt elindítaná a navigációt.

A [System Context IBD] és [Logical Model IBD] diagramokon látható, hogyan kommunikálnak ezek az alrendszerek, és milyen portokon keresztül történik az adatáramlás.

![](a3_screenshots/systemContextIBD.png)
![](a3_screenshots/logicalModelIBD.png)
![](a3_screenshots/navigationSystemIBD.png)
![](a3_screenshots/physicalModelIBD.png)

A modellünket is ennek megfelelően, hierarchikusan építettük fel.

- Pl.: SystemContext -> System -> Drone Logical és Physical
- Data Type-okat és Interface-eket ott hoztuk létre amelyik kontextusban először szükség volt rájuk.
- Activity diagramokat a hozzájuk tartozó komponens alá hoztuk létre, Szekvencia diagramokat pedig a megfelelő use case alá.

3️⃣ Hibatűrés: Redundancia és megbízhatóság
A rendszer hibatűrését több komponensszintű döntéssel biztosítottuk:

Duplikált CPU, két akkumulátor, 3 gyroszkóp + voting mechanizmus.

Ezek a döntések megjelennek a [Physical Model] és a [System Architecture] diagramokon, valamint Fault Tree Analysis formájában modelleztük a potenciális hibákat és azok következményeit.

![](a3_screenshots/6DSensorsFT.png)
![](a3_screenshots/landingFT.png)

Továbbá Hibamód és hibahatást is részletesen készítettünk a különböző komponensek hibáinak feltárására: ok, megelőzés, detektálás, hatások

![](a3_screenshots/FMEA1.png)
![](a3_screenshots/FMEA2.png)
![](a3_screenshots/FMEA3.png)
![](a3_screenshots/FMEADiagram.png)

4️⃣ Verifikáció: viselkedésalapú igazolás
A követelmények teljesülését több szinten igazoltuk:

Activity diagramok: bemutatják a rendszerműködés folyamatát, pl. a [taskValidationActivity] mutatja, hogyan validáljuk a TaskData-t.

Állapotgépek: például a [navigationSystemStateMachine] igazolja, hogy hogyan vált a drón különböző üzemmódok (Idle, Autonomous Flight, Emergency Landing) között.

Teljesítményelemzés: Számításaink alapján a szállítás várható időtartama 50 perc, ami megfelel a P07 – Delivery Time ≤ 1 óra követelménynek.

(Szekvencia)

![](a3_screenshots/taskValidationActivity.png)
![](a3_screenshots/navigationSystemActivity.png)

Ezen verifikációs lépések mind hozzájárulnak a «verify» relációval kapcsolt követelmények igazolásához.

![](a3_screenshots/Verification.png)

Szimulációt is csináltunk, amivel modelleztük a drón fizikáját és meghatároztuk, hogy mik az egyes tényezők, amik befolyásolják a leszállást.

check, detect, log, forward recovery