package com.globant.bigdata.trigrams;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Analyzes an input text file in order to extract word trigrams. Those trigrams
 * are used to generate new text automatically. The output file has the same
 * name of the input file plus a "_output" suffix
 *
 * @author oscar.mendez
 */
public class TextGenerator {

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
        //TODO change project to maven or gradle
        //TODO Create properties file and extract application parameters
        //TODO Create unit tests with several input sizes, several input files
        //at the same time and considering extreme situations: empty file,
        //empty map, one-key map, etc.
        //TODO Change sysout to logger
        //TODO Review a better way to package the software product
        //TODO Implement sentences and paragraphs generation
        
        //Validate input
        if (args.length <= 0) {
            System.out.println("Usage:\nTextGenerator <input_file_path>");
            System.exit(1);
            return;
        }

        WordExtractor we = new WordExtractor(args[0]);
        if (!we.isValidFilePath()) {
            System.out.println(args[0] + " is not a valid file or cannot be read.");
            System.exit(1);
            return;
        }

        //Extract strings from input file and split in parts for
        //concurrent processing
        List<String> chunks = we.extractWords();

        //Parallel generation of the trigram map
        Map<String, List<String>> trigrams = we.generateTrigramsMap(chunks);

        //Automatic text generation using the trigram map
        WordProducer wp = new WordProducer(args[0] + "_output", trigrams);
        wp.writeText(200);
        
        //TODO Erase temp chunks
        
        
        System.exit(0);
    }

}
