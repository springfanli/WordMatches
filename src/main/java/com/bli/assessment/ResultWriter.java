package com.bli.assessment;

public class ResultWriter {
    private final MatchesCounter matchesCounter;

    public ResultWriter(MatchesCounter matchesCounter) {
        this.matchesCounter = matchesCounter;
    }

    public void writeResults() {
        System.out.printf("%-30s %s%n", "Predefined word", "Match count");
        matchesCounter.getWordCounts()
                .forEach((word, count) -> System.out.printf("%-30s %d%n", word, count.sum()));
    }
}
