SRC=src/gol
CP=classes
JAVAC=javac -Xlint:unchecked -d ${CP}
JAVA=AWT_TOOLKIT=MToolkit java -cp ${CP}

compile:
	${JAVAC} ${SRC}/*.java ${SRC}/oscillators/*.java

main: compile
	${JAVA} gol/Main

clean:
	rm -rf ${CP}/*
