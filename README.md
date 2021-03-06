### Trigram Based Automatic Text Generator
This program extracts **trigrams** (see [trigram](http://en.wikipedia.org/wiki/N-gram)) from an input text file and use them to generate automatic text. It is intended to be executed using a command line console providing the input file path as argument. After execution an output file containing automatically generated text should be created by the program in the same directory than the input file.

### Project contents:

- src/main: Java source code
- src/test: Java Unit tests

### How to run:

This software project uses Maven as the project management tool. Only the source code and tests are provided. In order to generate an executable it is necessary to open a command prompt, go to the project directory and execute:

`mvn install`

(requires maven installed and configured)

A jar file named *TextGenerator-1.1-SNAPSHOT-jar-with-dependencies.jar* in the directory named target is created (sorry for the long name).

The program can be executed as follows:

`java -jar target/TextGenerator-1.1-SNAPSHOT-jar-with-dependencies.jar <input_file_path>`

(requires java installed and configured)

Some sample input files are provided in the folder:

src/main/resources/input

Executions using sample input files should be similar to the following:

`java -jar target/TextGenerator-1.1-SNAPSHOT-jar-with-dependencies.jar src/main/resources/input/small.txt`

`java -jar target/TextGenerator-1.1-SNAPSHOT-jar-with-dependencies.jar src/main/resources/input/medium.txt`

`java -jar target/TextGenerator-1.1-SNAPSHOT-jar-with-dependencies.jar src/main/resources/input/large.txt`

Unit tests can be run using:

`mvn test`

And coverage analysis can be carried out by executing:

`mvn cobertura:cobertura`

The resulting coverage report will be found at:

target/site/cobertura/index.html