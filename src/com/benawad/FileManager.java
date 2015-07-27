package com.benawad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by benawad on 7/26/15.
 */
public class FileManager {
    public void saveList(List<String> list, String name){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            for(String str:list){
                writer.write(str + "\n");
            }
            writer.close();
        } catch ( IOException ex){
            ex.printStackTrace();
        }
    }
    public void saveWord(String word, File name){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            writer.write(word);
            writer.flush();
            writer.close();
        } catch ( IOException ex){
            ex.printStackTrace();
        }
    }
}
