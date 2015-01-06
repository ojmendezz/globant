package com.globant.bigdata.trigrams;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Splits an input file into smaller files, filtering the content to allow only
 * words.
 *
 * @author oscar.mendez
 */
public class FileSplitter {

    private static final Logger logger = Logger.getLogger(FileSplitter.class.getName());
    
    private final String filePath;
    private StreamTokenizer st;
    private List<String> chunks;
    //TODO Extract as a property
    //TODO Try to calculate the split size
    public static final int SPLIT_SIZE = 10000;
    PrintWriter filePart;

    public FileSplitter(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        chunks = new ArrayList<String>();
        FileInputStream fis = new FileInputStream(filePath);
        st = new StreamTokenizer(new BufferedReader(new InputStreamReader(fis)));
        st.lowerCaseMode(true);
    }

    /**
     * Reads words from the input file and create new files of
     * <code>SPLIT_SIZE</code> words in each of them. The output files contain
     * one word per line.
     *
     * @throws IOException If there is a problem reading or writing files
     */
    public void split() throws IOException {
        String word;
        int count = 0;
        int globalCount = 0;
        newOutputFile();
        while ((word = getNext()) != null) {
            filePart.println(word);
            if (count >= SPLIT_SIZE) {
                globalCount += count;
                count = 0;
                newOutputFile();
            }
            count++;
        }
        filePart.flush();
        filePart.close();
        globalCount += count;
        if(logger.isDebugEnabled()){
            logger.log(Level.DEBUG, "Total words: " + globalCount);
        }

    }

    /**
     * Filters the input file stream to extract only words.
     *
     * @return The next word found in the input stream or null if the end of the
     * file is reached.
     * @throws IOException If there is a problem reading the input file
     */
    private String getNext() throws IOException {
        do {
            st.nextToken();
        } while (st.ttype != StreamTokenizer.TT_EOF && st.ttype != StreamTokenizer.TT_WORD);

        if (st.ttype == StreamTokenizer.TT_WORD) {
            return st.sval;
        }

        return null;
    }

    /**
     * Creates a new file for the output stream.
     *
     * @throws FileNotFoundException
     */
    private void newOutputFile() throws FileNotFoundException {
        if (filePart != null) {
            filePart.flush();
            filePart.close();
        }
        String newFilePartName = filePath + "_" + chunks.size();
        filePart = new PrintWriter(newFilePartName);
        chunks.add(newFilePartName);
        if(logger.isDebugEnabled()){
            logger.log(Level.DEBUG, "New split created: " + newFilePartName);
        }
    }

    /**
     * Returns the parts obtained from the original file.
     *
     * @return The list of paths corresponding to the parts.
     */
    public List<String> getChunks() {
        return chunks;
    }

}
