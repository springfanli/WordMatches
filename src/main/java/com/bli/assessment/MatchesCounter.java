package com.bli.assessment;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.LongAdder;

import static com.bli.assessment.PredefinedWords.MAX_WORD_LENGTH;

public class MatchesCounter {

    private final PredefinedWords predefinedWords;
    private final ConcurrentHashMap<String, LongAdder> wordCounts;

    public MatchesCounter(PredefinedWords predefinedWords) {
        this.predefinedWords = predefinedWords;
        this.wordCounts = new ConcurrentHashMap<>();
        predefinedWords.getWords().forEach(word -> wordCounts.put(word, new LongAdder()));
    }

    public void countMatches(Path inputFile, ExecutorService executor) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            reader.lines().forEach(line -> executor.execute(() -> countWordsPerLine(line)));
        }
    }

    private void countWordsPerLine(String line) {
        String[] words = line.toLowerCase().split("\\W+");
        for (String word : words) {
            if (word.length() <= MAX_WORD_LENGTH && predefinedWords.contains(word)) {
                wordCounts.get(word).increment();
            }
        }
    }

    public ConcurrentHashMap<String, LongAdder> getWordCounts() {
        return wordCounts;
    }
}
