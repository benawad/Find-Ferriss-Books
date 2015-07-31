package com.benawad.tests;

import com.benawad.Ferriss;
import com.benawad.FileManager;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by benawad on 7/31/15.
 */
public class FileManagerTest {
    @Test
    public void fileShouldBeMade(){
        FileManager fileManager = new FileManager();
        File file = new File("FileManagerTest.txt");
        fileManager.saveFile("Chocolate icecream", file);
        assertTrue(file.exists());
        //clean up the test file
        file.delete();
    }
    @Test
    public void fileShouldReadPopcorn(){
        FileManager fileManager = new FileManager();
        File file = new File("FileManagerTest2.txt");
        String popcorn = "Popcorn";
        fileManager.saveFile(popcorn, file);
        String wordFromFile = fileManager.readWord(file);
        try {
            assertEquals(popcorn, wordFromFile);
        } finally {
            //clean up the test file
            file.delete();
        }
    }
    @Test
    public void listShouldBeMade(){
        FileManager fileManager = new FileManager();
        List<String> favoriteFoods = new ArrayList<>();
        favoriteFoods.add("chocolate icecream" + Ferriss.SEPARATOR + "milk");
        favoriteFoods.add("popcorn" + Ferriss.SEPARATOR + "caramel");
        File file = new File("FileManagerTest3.txt");
        fileManager.saveList(favoriteFoods, file);
        assertTrue(file.exists());
        //clean up the test file
        file.delete();
    }
    @Test
    public void listShouldHaveFavoriteFoods(){
        FileManager fileManager = new FileManager();
        File file = new File("FileManagerTest4.txt");
        List<String> sFavoriteFoods = new ArrayList<>();
        sFavoriteFoods.add("pretzels" + Ferriss.SEPARATOR + "chips");
        sFavoriteFoods.add("yogurt" + Ferriss.SEPARATOR + "mustard");
        fileManager.saveList(sFavoriteFoods, file);
        List<String[]> favoriteFoods = fileManager.readList(file);
        try {
            assertEquals("pretzels", favoriteFoods.get(0)[0]);
            assertEquals("chips", favoriteFoods.get(0)[1]);
            assertEquals("yogurt", favoriteFoods.get(1)[0]);
            assertEquals("mustard", favoriteFoods.get(1)[1]);
        } finally {
            //clean up the test file
            file.delete();
        }
    }
}
