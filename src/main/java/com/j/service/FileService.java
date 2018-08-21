package com.j.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

public class FileService {

    private String[] args;

    private static final String regexOfPunctuationAndSpace = "[\\p{Punct}\\s]+";
    private static final String regexOfSingleWord = "\\w+";
    private static final int sizeOfListOfMostCommonThreeWordSequences = 100;

    public void setArgs(String[] args) {
        this.args = args;
    }

    public void setArgs() {
        System.out.print("Enter Parameter : ");
        Scanner scanner = new Scanner(System.in);
        String t = scanner.nextLine().trim();
        scanner.close();
        this.args = t.split("\\s+");
    }

    public void showFirstHundredMostCommonThreeWordSequences() {
        Arrays.stream(args).forEach(this::getFirstNmostCommonThreeWordSequences);
    }

    private void getFirstNmostCommonThreeWordSequences(String arg) {
        try {
            long timeOfStart = Instant.now().toEpochMilli();

            //Path path = Paths.get(getClass().getClassLoader().getResource(arg).toURI()); this is for dev test
            Path pathOfFile = Paths.get(arg);

            List<String> tokenList = getSingleWordTokenList(pathOfFile);
            List<String> threeWordSequence = createThreeWordSequences(tokenList);
            ConcurrentHashMap<String, LongAdder> threeWordSequencesMap = buildThreeWordSequencesMap(threeWordSequence);

            threeWordSequencesMap.keySet().stream()
                    .sorted((previousSequence, nextSequence) -> compare(threeWordSequencesMap.get(nextSequence).intValue(), threeWordSequencesMap.get(previousSequence).intValue()))
                    .map(key -> String.format("%d%s", threeWordSequencesMap.get(key).intValue(), " - " + key))
                    .limit(sizeOfListOfMostCommonThreeWordSequences)
                    .forEach(threeWordSequences -> System.out.println("\t" + threeWordSequences));

            long timeOfEnd = Instant.now().toEpochMilli();
            System.out.println(String.format("\nCompleted in %d milliseconds", (timeOfEnd - timeOfStart)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConcurrentHashMap<String, LongAdder> buildThreeWordSequencesMap(List<String> threeWordSequence) {
        ConcurrentHashMap<String, LongAdder> threeWordSequenceMap = new ConcurrentHashMap<>();
        threeWordSequence.parallelStream().forEach(token -> {
            if (!threeWordSequenceMap.containsKey(token)) {
                threeWordSequenceMap.put(token, new LongAdder());
            }
            threeWordSequenceMap.get(token).increment();
        });
        return threeWordSequenceMap;
    }

    private List<String> createThreeWordSequences(List<String> tokenList) {
        List<String> threeWordSequence = new ArrayList<>();
        for (int i = 0; i < tokenList.size() - 3; i++) {
            threeWordSequence.add(tokenList.get(i) + " " + tokenList.get(i + 1) + " " + tokenList.get(i + 2));
        }
        return threeWordSequence;
    }

    private List<String> getSingleWordTokenList(Path pathOfFile) throws IOException {
        return Files.readAllLines(pathOfFile)
                .parallelStream()
                .map(line -> line.split(regexOfPunctuationAndSpace))
                .flatMap(Arrays::stream)
                .parallel()
                .filter(word -> word.matches(regexOfSingleWord))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
