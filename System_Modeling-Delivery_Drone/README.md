# Homework Assignment

Using GitHub actions, every diagram is rendered on a different branch, `renders`, enabling easy documentation creation: by using the fully qualified name of the diagram (e.g., `Model.Drone.Package_Overview`), the render can be included in Markdown files:

![diagram](../../raw/renders/Model.Drone.Package_Overview.svg)

Use this method to create your documentation throughout the semester. Create a different Markdown file for each assignment in the `docs` folder. **The documentation should be sufficient for someone to understand your work, without opening any of the model files**.

We include an example model in `model.qeax` for ease of use. Treat this as a placeholder. Feel free to delete the model elements (or replace the file outright), but **keep the filename the same**.

The `main` branch is protected, and Pull Requests need to be opened for any updates. 

# Important links

* Tool usage Q&A: [https://q2a.inf.mit.bme.hu/](https://q2a.inf.mit.bme.hu/)
* Modeling tutorial: [https://ftsrg-rete.github.io/remo-lecture-notes/](https://ftsrg-rete.github.io/remo-lecture-notes/)
* Tool installation tutorial: [https://ftsrg-rete.github.io/remo-lecture-notes/rete-install-basics-en/](https://ftsrg-rete.github.io/remo-lecture-notes/rete-install-basics-en/)
* Markdown tutorial (for documentation): [https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)

# Házi feladat ellenőrző lista

Kötelező elemek:

- [x] [Rendszerkontextus](docs/assignment-3.md#system-context-bdd) (3p)
  - [x] Kontextuselemek és tulajdonságaik azonosítása (BDD)
  - [x] Kapcsolatok, adatáramlás és interfészek azonosítása (IBD)
- [x] [Követelmények](docs/assignment-3.md#stakeholders-uc) (5p)
  - [x] Érintettek összegyűjtése (UC)
  - [x] Funkcionális és extrafunkcionális követelmények összegyűjtése, kidolgozása, modellezése (REQ)
  - [x] Követelmények dekomponálása, később származtatása
- [x] [Használati esetek](docs/assignment-3.md#use-cases-uc) (6p)
  - [x] Használati esetek összegyűjtése, modellezése (UC)
  - [x] Használati esetek kidolgozása (elő- és utófeltételek, forgatókönyvek)
- [x] [Funkcionális modell/logikai architektúra](docs/assignment-3.md#logicalfunctional-system-bdd) (6p)
  - [x] Fő funkciók összegyűjtése, dekomponálása (BDD)
  - [x] Kapcsolatok, adatáramlás és interfészek azonosítása (IBD)
- [x] [Platform modell/fizikai architektúra](docs/assignment-3.md#the-physical-model-bdd) (6p)
  - [x] Fő platform komponensek összegyűjtése, komponálása, tulajdonságaik megadása (BDD)
  - [x] Kapcsolatok, adatáramlás és interfészek azonosítása (IBD)
- [x] [Rendszerarchitektúra](docs/assignment-3.md#system-architecture-bdd) (6p)
  - [x] Funkciók platformelemekre allokálása
  - [x] Platformspecifikus funkciók modellezése (BDD)
  - [x] Konfigurált platform elemek modellezése (BDD)
  - [x] Komponens-funkció nézet (IBD)

- [x] [Hibatűrés](docs/assignment-3.md#fault-tolerance-considerations): megfelelő hibatűrési minták az architektúra modellben
- [x] [Nyomonkövethetőség](docs/assignment-3.md#traceability): A modellelemek között modellezni kell a nyomonkövethetőségi relációkat

Legalább egy viselkedésmodell:

- [x] [Interakció modellezése](docs/assignment-3.md#sequence-diagrams) (9p): Komponensek interakcóinak ábrázolása szekvenciadiagramon
- [x] [Folyamatmodellezés](docs/assignment-3.md#task-validation-and-execution-activity) (9p): Összetett folyamatok modellezése Activity Diagrammal
- [x] [Állapotgépek](docs/assignment-3.md#task-execution-systems-state-machine) (9p): Állapotgépek modellezése State Machine Diagramon

Legalább egy analízis módszer:

- [x] [Szolgáltatásbiztonsági analízis](docs/assignment-3.md#fault-tree-analysis) (10p): Hibafa, eseményfa, vagy megbízhatósági blokkdiagram szolgáltatásbiztonság paramétereinek vizsgálatához
- [x] [Hibamód és -hatás analízis](docs/assignment-3.md#failure-mode-and-effects-analysis-fmea) (10p): részletes táblázatos FMEA a tervezés különböző fázisaiban a hibalehetőségek és hatásuk elemzésére
- [x] [Teljesítményanalízis](docs/assignment-3.md#performance-analysis) (10p): Teljesítménymodellezés és analízis egy teljesítmény követelmény teljesülésének bizonyításához
- [x] [Tesztelés és szimuláció](docs/assignment-3.md#simulation) (10p): Egy vagy több funkcionális követelmény vizsgálata a funkcionális modellekhez készített tesztesetekkel és szimulációval
