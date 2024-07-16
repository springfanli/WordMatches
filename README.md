Steps to run the program
1. Ensure JDK Installation: Make sure you have JDK 21 or higher installed to use virtual threads and try-with-resources for ExecutorService. This feature is introduced in 19 as a preview feature, if you have to use JDK 19, make sure to turn on the preview feature. You can check your Java version by running:
java -version

2. Ensure Maven Installation
mvn -version

3. Get the code
To clone the project, create a folder and use the git clone command.
$ cd $HOME
$ mkdir code
$ cd code
$ git clone https://github.com/springfanli/WordMatches.git
$ cd WordMatches

4. Compile the project
mvn clean package

5. Run the program
- To run the program, put your input file and predefined words in the WordMatches dierctory or specify the absolute paths of those files.

       java -jar target/WordMatches-1.0-SNAPSHOT.jar input.txt predefined_words.txt

- To test it on a large input file up 20MB and 10k predefined words. You can use the python script I provided to generate the test files following the steps below. Make sure you have python 3.5+ installed.

       python pyscripts/generate_testdata.py
       java -jar target/WordMatches-1.0-SNAPSHOT.jar large_input.txt predefined_10k_words.txt > result_output.txt

6. What did I test?
- Normal cases
- Empty files
- Words separated by punctuations
- Words up to 256 characters and words more than 256 characters
- Large input file up to 20MB and predefined words file up to 10k entries
