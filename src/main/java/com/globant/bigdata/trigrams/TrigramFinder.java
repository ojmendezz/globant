package com.globant.bigdata.trigrams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Task for finding trigrams in a text fragment.
 *
 * @author oscar.mendez
 */
public class TrigramFinder implements Callable<String> {

    private String filePath;
    private String jobId;
    private BufferedReader br;
    private LinkedList<String> buffer;
    private Map<String, List<String>> mapRef;

    public TrigramFinder(String filePath, String jobId, Map<String, List<String>> map) throws FileNotFoundException {
        this.filePath = filePath;
        this.jobId = jobId;
        this.mapRef = map;
        FileInputStream fis = new FileInputStream(filePath);
        br = new BufferedReader(new InputStreamReader(fis));
        buffer = new LinkedList<String>();
    }

    /**
     * Reads a word stream and creates trigrams in a map where the first two
     * words are the key and the third one is the value.
     *
     * @return Message indicating succesful execution
     * @throws IOException If there is a problem reading the input stream
     */
    @Override
    public String call() throws IOException {
        buffer.add(br.readLine());
        buffer.add(br.readLine());
        String word;
        while ((word = br.readLine()) != null) {
            buffer.add(word);
            final String key = buffer.get(0) + " " + buffer.get(1);
            List<String> words = mapRef.get(key);
            if (words == null) {
                words = new ArrayList<String>();
                mapRef.put(key, words);
            }
            synchronized (words) {
                words.add(buffer.get(2));
            }
            buffer.removeFirst();
        }

        br.close();

        return "Process " + jobId + " finished.";
    }

}
