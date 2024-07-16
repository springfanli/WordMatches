package com.bli.assessment;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordMatchesCounter {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java -jar target/WordMatches-1.0-SNAPSHOT.jar <input_file> <predefined_words_file>");
            System.exit(1);
        }

        Path inputFile = Path.of(args[0]);
        Path predefinedWordsFile = Path.of(args[1]);

        try {
            PredefinedWords predefinedWords = loadPredefinedWords(predefinedWordsFile);
            MatchesCounter matchesCounter = new MatchesCounter(predefinedWords);

            // use virtual threads and try-with-resources for ExecutorService
            // this feature is introduced in 19 as a preview feature or later
            try (ExecutorService executor = createExecutorService()) {
                countMatches(inputFile, matchesCounter, executor);
            }

            ResultWriter rw = new ResultWriter(matchesCounter);
            rw.writeResults();

        } catch(NoSuchFileException ne){
            System.err.println(ne.getFile()+" doesn't exist.");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static PredefinedWords loadPredefinedWords(Path predefinedWordsFile) throws IOException {
        return new PredefinedWords(predefinedWordsFile);
    }

    public static void countMatches(Path inputFile, MatchesCounter matchCounter, ExecutorService executor) throws IOException {
        matchCounter.countMatches(inputFile, executor);
    }

    public static ExecutorService createExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
