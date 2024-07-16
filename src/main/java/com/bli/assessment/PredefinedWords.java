package com.bli.assessment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PredefinedWords {
    public static final int MAX_WORD_LENGTH = 256;
    private final Set<String> words;

    public PredefinedWords(Path predefinedWordsFile) throws IOException {
        this.words = Files.lines(predefinedWordsFile)
                .map(String::toLowerCase)
                .filter(word -> word.length() <= MAX_WORD_LENGTH)
                .collect(Collectors.toCollection(ConcurrentHashMap::newKeySet));
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public Set<String> getWords() {
        return words;
    }
}
