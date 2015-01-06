package com.globant.bigdata.trigrams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the different scenarios of execution of the TextGenerator project
 * @author oscar.mendez
 */
public class TextGeneratorTest {
    
    private static final Logger logger = Logger.getLogger(TextGeneratorTest.class.getName());
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void badInputFile(){
        try {
            TextGenerator.main(new String[] {"This/file/does/not/exists"});
            assertTrue(outContent.toString().contains("This/file/does/not/exists is not a valid file or cannot be read."));
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void noInputFile(){
        try {
            TextGenerator.main(new String[] {});
            assertTrue(outContent.toString().contains("Usage:\nTextGenerator <input_file_path>"));
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void mediumFile(){
        try {
            TextGenerator.main(new String[] {"src/main/resources/input/medium.txt"});            
            assertTrue(new File("src/main/resources/input/medium.txt_output").exists());
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
