#!/bin/bash
cd src
javac Main.java
javac Window.java
javac player.java
javac Enemy.java
javac ImageTools.java
javac Level.java
javac Masher_Gabuccho.java
javac Ground.java
javac UnzipFiles.java
jar -cmf MANIFEST.MF Sonic-Prism.jar ./**
cp -f Sonic-Prism.jar ..
rm Sonic-Prism.jar
cd ..
java -jar Sonic-Prism.jar
