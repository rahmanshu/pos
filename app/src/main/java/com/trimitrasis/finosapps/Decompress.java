package com.trimitrasis.finosapps;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by rahman on 28/07/2017.
 */

public class Decompress {

    private String _zipFile;
    private String _location;

    public Decompress(String zipFile, String location) {
        _zipFile = zipFile;
        _location = location;
        _dirChecker("");
    }

    public void unzip() {
        try  {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null){

                if(ze.isDirectory()){
                    _dirChecker(ze.getName());
                }else{
                    FileOutputStream fout = new FileOutputStream(_location + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }
            }

            zin.close();

        } catch(FileNotFoundException e) {
            Log.e("Decompress", "unzip", e);
        } catch (Exception e){
            Log.e("Decompress", "unzip error", e);
        }

    }


    private void _dirChecker(String dir){
        File f = new File(_location + dir);
        if(!f.isDirectory()) {
            f.mkdirs();
        }
    }



}
