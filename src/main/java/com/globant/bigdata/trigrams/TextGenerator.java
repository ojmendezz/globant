package com.globant.bigdata.trigrams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

/**
 * Analyzes an input text file in order to extract word trigrams. Those trigrams
 * are used to generate new text automatically. The output file has the same
 * name of the input file plus a "_output" suffix
 *
 * @author oscar.mendez
 */
public class TextGenerator {

    private static final Logger logger = Logger.getLogger(TextGenerator.class.getName());
    
    private static final int WORDS_TO_WRITE = 200;

    /**
     * Entry point
     *
     * @param args the command line arguments. Only the first one is used for
     * providing the input file path.
     * @throws java.io.FileNotFoundException If the input file cannot be found
     * @throws java.io.IOException If there is a problem reading or writing
     * files
     * @throws java.lang.InterruptedException If a thread interruption occurs
     * when extracting trigrams
     * @throws java.util.concurrent.ExecutionException If there is a problem
     * when executing parallel trigram extraction
     */
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, ExecutionException, IOException, ConfigurationException {
        //TODO Create properties file and extract application parameters
        //TODO Implement sentences and paragraphs generation (Capitalize first letter is pending)
        logger.info("-------Text Generation starting right now...-------");
        
        //Validate input
        if (args.length <= 0) {
            System.out.println("Usage:\nTextGenerator <input_file_path>");
            if (logger.isDebugEnabled()) {
                logger.error("Not input file provided by the user");
            }
            return;
        }
        
        WordExtractor we = new WordExtractor(args[0]);
        if (!we.isValidFilePath()) {
            System.out.println("File: \"" + args[0] + "\" is not a valid file or cannot be read.");
            if (logger.isDebugEnabled()) {
                logger.error("Invalid input file: " + args[0]);
            }
            return;
        }

        System.out.println("File: " + args[0] + " is ready to be processed.");
        
        logger.info("Input file validations OK");

        //Extract strings from input file and split in parts for
        //concurrent processing
        long tick = System.currentTimeMillis();
        long startTime = tick;
        
        List<String> chunks = we.extractWords();
        
        long tock = System.currentTimeMillis();
        logger.info("Words extraction from file finished");
        logger.info("Elapsed time during this task: " + (tock-tick) + " ms");

        System.out.println("Input file has been splitted in " + chunks.size() + " chunks");

        //Parallel generation of the trigram map
        tick = System.currentTimeMillis();
        
        Map<String, List<String>> trigrams = we.generateTrigramsMap(chunks);
        
        tock = System.currentTimeMillis();
        logger.info("Trigrams map generation completed");
        logger.info("Elapsed time during this task: " + (tock-tick) + " ms");
             
        //Automatic text generation using the trigram map
        tick = System.currentTimeMillis();
        final String outputFileName = args[0] + "_output";
        
        System.out.println("Extracted trigrams are going to be used for automatic text generation.");
        WordProducer wp = new WordProducer(outputFileName, trigrams);
        wp.writeText(WORDS_TO_WRITE);
        
        tock = System.currentTimeMillis();
        logger.info("Automatic text writting finished");
        logger.info("Elapsed time during this task: " + (tock-tick) + " ms");

        
        System.out.println("Text generation completed. " + WORDS_TO_WRITE +
                " words written.");
        System.out.println("Take a look to the generated file at: " + outputFileName);
        System.out.println("Elapsed time for this execution: " + (tock - startTime) + "ms");
    }

}
