package com.benawad.tests;

import com.benawad.GoogleBooks;
import org.json.JSONArray;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by benawad on 8/11/15.
 */
public class GoogleBooksTest {
    @Test
    public void shouldMakeStringArrayList(){
        ArrayList<String> groceries = new ArrayList<>();
        groceries.add("bacon");
        groceries.add("yogurt");

        JSONArray jsonArray = new JSONArray(groceries.toArray());

        ArrayList<String> groceriesActual = GoogleBooks.toStringArrayList(jsonArray);

        org.junit.Assert.assertArrayEquals(groceries.toArray(), groceriesActual.toArray());
    }

}
