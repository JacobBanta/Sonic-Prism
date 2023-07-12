default: Main.class Window.class player.class Enemy.class ImageTools.class Level.class Masher_Gabuccho.class Ground.class UnzipFiles.class
	jar -cmf MANIFEST.MF Sonic-Prism.jar ./*.class ./*.csv ./assets/** ./levels/*
	cp -f Sonic-Prism.jar ..
	java -jar ./Sonic-Prism.jar 1
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
	java -jar ./Sonic-Prism.jar
build: Main.class Window.class player.class Enemy.class ImageTools.class Level.class Masher_Gabuccho.class Ground.class UnzipFiles.class
	jar -cmf MANIFEST.MF Sonic-Prism.jar ./*.class ./*.csv ./assets/** ./levels/*
	cp -f Sonic-Prism.jar ..
rebuild:
	javac Main.java
	javac Window.java
	javac player.java
	javac Enemy.java
	javac ImageTools.java
	javac Level.java
	javac Masher_Gabuccho.java
	javac Ground.java
	javac UnzipFiles.java
	jar -cmf MANIFEST.MF Sonic-Prism.jar ./*.class ./*.csv ./assets/** ./levels/*
	cp -f Sonic-Prism.jar ..
