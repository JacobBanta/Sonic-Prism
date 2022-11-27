import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFiles {

    public static void call(String zipFilePath, String destDir) {
        unzip(zipFilePath, destDir);
    }
    private static String getExtension(String filename){
      return filename.substring(filename.lastIndexOf("."));
    }
    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
              if(!ze.isDirectory()){
                String fileName = ze.getName();
                if(!(getExtension(fileName).matches(".class") || getExtension(fileName).matches(".MF")) && (!(new File(fileName)).exists())){
                  File newFile = new File(destDir + File.separator + fileName);
                  //System.out.println("Unzipping to "+newFile.getAbsolutePath());
                  //create directories for sub directories in zip
                  new File(newFile.getParent()).mkdirs();
                  FileOutputStream fos = new FileOutputStream(newFile);
                  int len;
                  while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                  }
                  fos.close();
                }
              }else if(!ze.getName().matches("META-INF/")){
                new File(destDir + File.separator + ze.getName()).mkdir();
                //System.out.println("creating dir "+ze.getName());
              }
              //close this ZipEntry
              zis.closeEntry();
              ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
