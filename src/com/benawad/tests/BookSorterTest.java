package com.benawad.tests;

import com.benawad.BookSorter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benawad on 8/11/15.
 */
public class BookSorterTest {
    @Test
    public void shouldContainWord(){
        String word = "peppermint";

        List<String> flavors = new ArrayList<>();
        flavors.add("peach");
        flavors.add(word);
        flavors.add("oreo");

        Assert.assertTrue(BookSorter.contains(word, flavors));

        flavors.remove(word);

        Assert.assertFalse(BookSorter.contains(word, flavors));

        flavors.clear();

        Assert.assertFalse(BookSorter.contains(word, flavors));
    }
}
