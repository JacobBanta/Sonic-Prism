javac Main.java
javac ground.java
javac player.java
javac Window.java
jar -cvmf MANIFEST.MF sonic.jar Main.class Main$1.class player.class ground.class Window.class Window$1.class UnzipFiles.class level.csv assets
java -jar sonic.jar