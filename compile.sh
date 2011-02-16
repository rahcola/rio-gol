#!/bin/sh
CP=classes
SRC=src

if [ ! -d "$CP" ]; then
	echo mkdir "$CP"
	mkdir "$CP"
fi

echo javac -d $CP -sourcepath $SRC $SRC/gol/Main.java
javac -d $CP -sourcepath $SRC $SRC/gol/Main.java
