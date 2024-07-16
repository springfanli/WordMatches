package com.bli.assessment;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordMatchesCounterTest {
    private static Path inputFilePath;
    private static Path predefinedWordsFilePath;
    private static Path emptyFilePath;
    private static Path predefinedLongWordsFilePath;
    private static Path longWordsFilePath;

    @BeforeAll
    public static void setup() throws IOException {
        inputFilePath = Paths.get("test_input.txt");
        predefinedWordsFilePath = Paths.get("test_predefined_words.txt");
        emptyFilePath = Paths.get("empty_test_input.txt");
        longWordsFilePath = Paths.get("test_long.txt");
        predefinedLongWordsFilePath = Paths.get("test_predefined_long.txt");

        Files.write(inputFilePath, List.of(
                "Detecting first names is tricky to do even with AI.",
                "How do you say a street name is not a first name?",
                "People use AI to predict the future, but it's hard to make predictions about the future.",
                "AI is changing the world.",
                "Companies like to adopt AI-first strategy these days."
        ));

        // Create temporary input file
        Files.createFile(emptyFilePath);

        Files.write(predefinedWordsFilePath, List.of(
                "name",
                "first",
                "ai",
                "people",
                "street"
        ));
    }

    @AfterAll
    public static void tearDown() throws IOException {
        // Clean up temporary files
        Files.deleteIfExists(inputFilePath);
        Files.deleteIfExists(predefinedWordsFilePath);
        Files.deleteIfExists(emptyFilePath);
        Files.deleteIfExists(longWordsFilePath);
        Files.deleteIfExists(predefinedLongWordsFilePath);
    }

    @Test
    public void testWordMatchCounter() throws IOException {
        PredefinedWords predefinedWords = WordMatchesCounter.loadPredefinedWords(predefinedWordsFilePath);
        MatchesCounter matchCounter = new MatchesCounter(predefinedWords);

        try (ExecutorService executor = WordMatchesCounter.createExecutorService()) {
            WordMatchesCounter.countMatches(inputFilePath, matchCounter, executor);
        }

        // Expected results
        var wordCounts = matchCounter.getWordCounts();
        assertEquals(2, wordCounts.get("name").sum());
        assertEquals(3, wordCounts.get("first").sum());
        assertEquals(4, wordCounts.get("ai").sum());
        assertEquals(1, wordCounts.get("people").sum());
        assertEquals(1, wordCounts.get("street").sum());
    }

    @Test
    public void testEmptyInputFile() throws IOException {
        PredefinedWords predefinedWords = WordMatchesCounter.loadPredefinedWords(predefinedWordsFilePath);
        MatchesCounter matchCounter = new MatchesCounter(predefinedWords);

        try (ExecutorService executor = WordMatchesCounter.createExecutorService()) {
            WordMatchesCounter.countMatches(emptyFilePath, matchCounter, executor);
        }

        // Expected results
        var wordCounts = matchCounter.getWordCounts();
        assertEquals(0, wordCounts.get("name").sum());
        assertEquals(0, wordCounts.get("first").sum());
        assertEquals(0, wordCounts.get("ai").sum());
        assertEquals(0, wordCounts.get("people").sum());
        assertEquals(0, wordCounts.get("street").sum());
    }

    @Test
    public void testLongWord() throws IOException {
        String longWord = "c".repeat(256);
        String tooLongWord = "d".repeat(257);

        Files.writeString(predefinedLongWordsFilePath, longWord + "\n" + tooLongWord + "\n");
        Files.writeString(longWordsFilePath, longWord + " " + tooLongWord);

        PredefinedWords predefinedWords = WordMatchesCounter.loadPredefinedWords(predefinedLongWordsFilePath);
        MatchesCounter matchCounter = new MatchesCounter(predefinedWords);

        try (ExecutorService executor = WordMatchesCounter.createExecutorService()) {
            WordMatchesCounter.countMatches(longWordsFilePath, matchCounter, executor);
        }

        // Expected results
        var wordCounts = matchCounter.getWordCounts();

        assertEquals(1, wordCounts.get(longWord.toLowerCase()).sum());
        assertTrue(wordCounts.get(tooLongWord.toLowerCase()) == null || wordCounts.get(tooLongWord.toLowerCase()).sum() == 0);
    }
}