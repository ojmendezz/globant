package com.globant.bigdata.trigrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the file splitter
 *
 * @author oscar.mendez
 */
public class FileSplitterTest {

    private FileSplitter splitter;
    private static final String TEST_FILE_PATH = "src/main/resources/input/medium.txt";

    public FileSplitterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws FileNotFoundException, ConfigurationException {
        splitter = new FileSplitter(TEST_FILE_PATH);
    }

    @After
    public void tearDown() {
        //Delete generated files
        File testOutputFile = new File(TEST_FILE_PATH+ "_output");
        if (testOutputFile.exists()) {
            testOutputFile.delete();
        }
    }

    @Test
    public void splitTest() throws IOException {
        splitter.split();
        int expectedChunks = 17;
        for (int i = 0; i < expectedChunks; i++) {
            assertTrue(new File(TEST_FILE_PATH + "_" + i).exists());
        }

    }
}
