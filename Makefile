SRC=src/gol
CP=classes
JAVAC=javac -Xlint:unchecked -d ${CP}
JAVA=java -cp ${CP}

compile:
	${JAVAC} ${SRC}/*.java

main: compile
	${JAVA} gol/Main

clean:
	rm -rf ${CP}/*
