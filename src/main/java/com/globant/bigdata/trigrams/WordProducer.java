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
        Random r = new Random();

        List<String> keys = new ArrayList<String>(trigrams.keySet());
        String randomKey = keys.get(r.nextInt(keys.size()));
        List<String> values = trigrams.get(randomKey);
        String value = values.get(r.nextInt(values.size()));
        pw.print(randomKey + " " + value);
        String[] key = randomKey.split(" ");
        key[0] = key[1];
        key[1] = value;
        String currentTrigram;
        for (int i = 0; i < numWords - 3; i++) {
            String k = key[0] + " " + key[1];
            values = trigrams.get(k);
            value = values.get(r.nextInt(values.size()));
            currentTrigram = k + " " + value;
            // If an already used trigram is found
            if (usedTrigrams.contains(k)) {
                //Try to get a different value with the same key
                String newValue = values.get(r.nextInt(values.size()));
                if (!value.equals(newValue)) {
                    value = newValue;
                } else {
                    //if it is not posible to find a different value, start again
                    //with a new trigram
                    String newRandomKey;
                    //Verify that we are not getting the same key
                    do {
                        newRandomKey = keys.get(r.nextInt(keys.size()));
                    } while (randomKey.equals(newRandomKey));
                    
                    values = trigrams.get(randomKey);
                    value = values.get(r.nextInt(values.size()));
                    //Print the new trigram
                    pw.println(".");
                    pw.print(randomKey + " ");
                    key = randomKey.split(" ");
                }
                currentTrigram = randomKey + " " + value;
            }
            pw.print(value + " ");
            usedTrigrams.add(currentTrigram);

            key[0] = key[1];
            key[1] = value;
        }
        pw.flush();
        pw.close();
    }

}
