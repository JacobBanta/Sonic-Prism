#!/bin/bash
cd src
javac Main.java
javac player.java
javac Enemy.java
javac ImageTools.java
javac Level.java
javac Masher_Gabuccho.java
javac Ground.java
javac UnzipFiles.java
jar -cmf MANIFEST.MF levelbuilder.jar ./**
cp -f levelbuilder.jar ..
rm levelbuilder.jar
cd ..
java -jar levelbuilder.jar
