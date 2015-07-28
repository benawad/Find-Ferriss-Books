package com.benawad;

import java.io.*;
import java.util.ArrayList;
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
    public List<String[]> readList(String sFile){
        List<String[]> books = new ArrayList<>();
        try{
            File file = new File(sFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;
            while((line=br.readLine()) != null){
                books.add(line.split(Ferriss.SEPARATOR));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
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
    public String readWord(File name){
        String line = "";
        try{
            FileReader fileReader = new FileReader(name);
            BufferedReader reader = new BufferedReader(fileReader);
            line = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
