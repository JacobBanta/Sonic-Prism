default: Main.class Window.class player.class Enemy.class ImageTools.class Level.class Masher_Gabuccho.class Ground.class UnzipFiles.class
	jar -cmf MANIFEST.MF levelbuilder.jar ./*.class ./*.ser ./assets/*
	cp -f levelbuilder.jar ..
	java -jar ./levelbuilder.jar 1
Main.class: Main.java
	javac Main.java
Window.class: Window.java
	javac Window.java
player.class: player.java
	javac player.java
Enemy.class: Enemy.java
	javac Enemy.java
ImageTools.class: ImageTools.java
	javac ImageTools.java
Level.class: Level.java
	javac Level.java
Masher_Gabuccho.class: Masher_Gabuccho.java
	javac Masher_Gabuccho.java
Ground.class: Ground.java
	javac Ground.java
UnzipFiles.class: UnzipFiles.java
	javac UnzipFiles.java
run:
	java -jar ./levelbuilder.jar
build: Main.class Window.class player.class Enemy.class ImageTools.class Level.class Masher_Gabuccho.class Ground.class UnzipFiles.class
	jar -cmf MANIFEST.MF levelbuilder.jar ./*.class ./*.ser ./assets/*
	cp -f levelbuilder.jar ..
rebuild:
	javac ./*.java
	jar -cmf MANIFEST.MF levelbuilder.jar ./*.class ./*.ser ./assets/*
	cp -f levelbuilder.jar ..
