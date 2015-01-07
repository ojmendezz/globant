package com.globant.bigdata.trigrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Parallel extraction of trigrams.
 *
 * @author oscar.mendez
 */
public class WordExtractor {

    private static final Logger logger = Logger.getLogger(WordExtractor.class.getName());

    private String filePath;

    /**
     * Maximum number of threads able to run simultaneously to generate
     * trigrams. Can be configured in the <strong>config.properties</strong>
     * file.
     */
    private int poolSize;

    public WordExtractor(String filePath) throws ConfigurationException {
        this.filePath = filePath;

        PropertiesConfiguration pc = new PropertiesConfiguration("config.properties");
        poolSize = pc.getInt("concurrent.threads");
    }

    /**
     * Validates a file path
     *
     * @return <code>true</code> if the path is valid
     */
    public boolean isValidFilePath() {
        File f = new File(filePath);
        return f.canRead() && f.isFile();
    }

    /**
     * Reads an input file and extracts the words in the content. Splits the
     * whole list of strings into chunks and writes each group in a separate
     * file, so that it can be processed by a different thread in parallel.
     *
     * @return List of paths where the resulting chunks can be found
     * @throws IOException if there is a problem reading or writing files
     * @throws FileNotFoundException If the input file does not exist
     */
    public List<String> extractWords() throws IOException, FileNotFoundException, ConfigurationException {
        FileSplitter fs = new FileSplitter(filePath);
        fs.split();
        List<String> chunks = fs.getChunks();
        return chunks;
    }

    public Map<String, List<String>> generateTrigramsMap(List<String> chunks) throws InterruptedException, ExecutionException, FileNotFoundException {
        //Initialize trigrams map
        Map<String, List<String>> trigrams = new ConcurrentHashMap<String, List<String>>();

        //Initialize processing tasks
        List<TrigramFinder> finders = new ArrayList<TrigramFinder>();

        for (int i = 0; i < chunks.size(); i++) {
            finders.add(new TrigramFinder(chunks.get(i), "Chunk_" + (i + 1), trigrams));
        }

        if (logger.isDebugEnabled()) {
            logger.debug(finders.size() + " chunks available to be processed.");
            logger.debug("Up to " + poolSize + " chunks may be processed in parallel.");
        }

        //Launch tasks
        //TODO Evaluate a better adaptative pool size
        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        List<Future<String>> results = executorService.invokeAll(finders);

        //Print tasks results when done
        for (Future<String> result : results) {
            System.out.println(result.get());
        }

        executorService.shutdown();

        if (logger.isInfoEnabled()) {
            logger.info(trigrams.size() + " trigrams found.");
        }

        return trigrams;
    }
}
