SRC=src
CP=classes
JAVAC=javac -Xlint:unchecked -d ${CP} -sourcepath ${SRC}
JAVA=AWT_TOOLKIT=MToolkit java -cp ${CP}

compile:
	${JAVAC} ${SRC}/gol/Main.java

main: compile
	${JAVA} gol/Main

clean:
	rm -rf ${CP}/*
