package com.globant.bigdata.trigrams;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Generates automatic text using a trigram map.
 *
 * @author oscar.mendez
 */
public class WordProducer {

    private Set<String> usedTrigrams;
    private Map<String, List<String>> trigrams;
    private String filePath;
    private Random randomGen = new Random();

    public WordProducer(String file, Map<String, List<String>> trigrams) {
        this.trigrams = trigrams;
        this.filePath = file;
        usedTrigrams = new HashSet<String>();
    }

    /**
     * Writes automatic text taking the trigrams map as source.
     * @param numWords Number of words to write in the output file.
     * @throws FileNotFoundException If there is a problem creating the 
     * output file.
     */
    public void writeText(int numWords) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);

        String randomKey = getRandomKey();
        String value = getRandomValue(randomKey);
        pw.print(randomKey + " " + value);
        String[] key = randomKey.split(" ");
        shiftKey(key, value);
        
        String currentTrigram;
        String currentKey;
        for (int i = 0; i < numWords - 3; i++) {
            currentKey = key[0] + " " + key[1];
            value = getRandomValue(currentKey);
            //If the key does not exist or there is no value
            while(value == null || value.isEmpty()){
                //What happens when the map is empty?
                currentKey = getRandomKey();
                value = getRandomValue(currentKey);
            }
            
            currentTrigram = currentKey + " " + value;
            // If an already used trigram is found
            if (usedTrigrams.contains(currentTrigram)) {
                //Try to get a different value with the same key
                String newValue = getRandomValue(currentKey);
                //What if randomly the same value is selected but there is yet
                //different values?
                if (!value.equals(newValue)) {
                    value = newValue;
                } else {
                    //if it is not posible to find a different value, start again
                    //with a new trigram
                    String newRandomKey;
                    //Verify that we are not getting the same key
                    do {
                        newRandomKey = getRandomKey();
                        // What if there is just one key on the map?
                    } while (currentKey.equals(newRandomKey));
                    
                    currentKey = newRandomKey;
                            
                    value = getRandomValue(currentKey);
                    //Print the new trigram
                    pw.println(".");
                    pw.print(newRandomKey);
                    key = newRandomKey.split(" ");
                }
                currentTrigram = currentKey + " " + value;
            }
            pw.print(" " + value);
            usedTrigrams.add(currentTrigram);

            shiftKey(key, value);
        }
        pw.flush();
        pw.close();
    }

    /**
     * Returns a random value of the list of words given an entry key for the
     * trigrams map.
     * @param key key for searching on the trigrams map
     * @return a random trigram word for the key. <code>Null</code> if the list 
     * of words for the key is empty or null.
     */
    private String getRandomValue(String key) {
        List<String> values = trigrams.get(key);
        if(values == null || values.isEmpty()){
            return null;
        }
        String value = values.get(randomGen.nextInt(values.size()));
        return value;
    }
    
    /**
     * Returns a new random key from the trigrams map
     * @return a new random key
     */
    private String getRandomKey(){
        List<String> keys = new ArrayList<String>(trigrams.keySet());
        return keys.get(randomGen.nextInt(keys.size()));
    }
    
    /**
     * Shifts an array containing the words of a trigram, deleting the word in 
     * the first position and inserting a new value in the last one.
     * @param words Array of three words to be shifted
     * @param value New value to insert in the array
     */
    private void shiftKey(String[] words, String value){
        if(words == null || words.length != 2){
            return;
        }
        
        words[0] = words[1];
        words[1] = value;
    }

}
