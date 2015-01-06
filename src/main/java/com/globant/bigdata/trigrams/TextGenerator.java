package com.globant.bigdata.trigrams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Level;
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
    public static void main(String[] args) throws FileNotFoundException, InterruptedException, ExecutionException, IOException {
        //TODO Create properties file and extract application parameters
        //TODO Implement sentences and paragraphs generation (Capitalize first letter is pending)

        //Validate input
        if (args.length <= 0) {
            System.out.println("Usage:\nTextGenerator <input_file_path>");
            if (logger.isDebugEnabled()) {
                logger.log(Level.ERROR, "Not input file provided by the user");
            }
            return;
        }

        WordExtractor we = new WordExtractor(args[0]);
        if (!we.isValidFilePath()) {
            System.out.println(args[0] + " is not a valid file or cannot be read.");
            if (logger.isDebugEnabled()) {
                logger.log(Level.ERROR, "Invalid input file");
            }
            return;
        }

        logger.log(Level.INFO, "Input file validations OK");

        //Extract strings from input file and split in parts for
        //concurrent processing
        List<String> chunks = we.extractWords();

        logger.log(Level.INFO, "Input file has been splitted in " + chunks.size() + "chunks");

        //Parallel generation of the trigram map
        Map<String, List<String>> trigrams = we.generateTrigramsMap(chunks);
        
        logger.log(Level.INFO, "Trigrams extracted: " + trigrams.size());
            
        //Automatic text generation using the trigram map
        WordProducer wp = new WordProducer(args[0] + "_output", trigrams);
        wp.writeText(200);
        
            logger.log(Level.INFO, "Text generation completed.");

        //TODO Erase temp chunks
    }

}
