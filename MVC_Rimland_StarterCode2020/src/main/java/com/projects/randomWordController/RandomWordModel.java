package com.projects.randomWordController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomWordModel {
    ArrayList<String> listOfWords;
    Random r;
    public RandomWordModel()
    {
        listOfWords = new ArrayList<String>(
                // If you want to initialize the list with some values, uncomment these lines
//                Arrays.asList(
//                "Hello",
//                "World",
//                "Bye",
//                "Random",
//                "Words")
        );

        r = new Random();
    }
    public String getWord()
    {
        if(listOfWords.size() == 0){
            return "no words found.";
        }
        return listOfWords.get(r.nextInt(listOfWords.size()));
    }

    public void putWord(String word){
        listOfWords.add(word);
    }
}
