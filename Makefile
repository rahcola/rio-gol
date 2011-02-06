SRC=src/gol
CP=classes
JAVAC=javac -d ${CP}
JAVA=java -cp ${CP}

compile:
	${JAVAC} ${SRC}/*.java

main: compile
	${JAVA} gol/Main

clean:
	rm -rf ${CP}/*
