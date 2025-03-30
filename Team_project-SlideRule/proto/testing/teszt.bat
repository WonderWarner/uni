@ECHO OFF

CHCP 65001
MD .\testing\output

TYPE .\testing\tests.txt
ECHO.
ECHO.

SET N=0

SET C=0
FOR %%X IN (%*) DO SET /A C+=1
IF %C% GEQ 1 (
	SET N=%1
	GOTO TEST
)

SET /P N=Melyik teszteset? (1-28 vagy * az összeshez) 

:TEST
IF %N%==* GOTO ALL
IF %N% LSS 1 GOTO ERROR
IF %N% GTR 28 GOTO ERROR

:SELECT
java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -cp .\out\ Interpreter <.\testing\input\%N%input.txt >.\testing\output\%N%output.txt
ECHO ***** %N%. TESZTESET EREDMÉNY *****
FC .\testing\output\%N%output.txt .\testing\expected\%N%expected.txt
GOTO END

:ALL
ECHO Összes teszteset
FOR /L %%I IN (1,1,28) DO (
	java -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -cp .\out\ Interpreter <.\testing\input\%%Iinput.txt >.\testing\output\%%Ioutput.txt
	ECHO ***** %%I. TESZTESET EREDMÉNY *****
	FC .\testing\output\%%Ioutput.txt .\testing\expected\%%Iexpected.txt
)
GOTO END

:ERROR
ECHO A megadott teszteset nem létezik!

:END