package com.j.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;
import static java.lang.Integer.parseInt;

public class FileService {

    public void getMostCommonThreeWordSequences(String arg) {
        try {
            long start = Instant.now().toEpochMilli();

            ConcurrentHashMap<String, LongAdder> wordMap = new ConcurrentHashMap<>();
            Path path = Paths.get(getClass().getClassLoader().getResource(arg).toURI());

            List<String> tokenList = Files.readAllLines(path)
                    .parallelStream()
                    .map(line -> line.split("[\\p{Punct}\\s]+"))
                    .flatMap(Arrays::stream)
                    .parallel()
                    .filter(word -> word.matches("\\w+"))
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

//            System.out.println(tokenList);
            List<String> threeWordSequence = new ArrayList<>();

            for (int i = 0; i < tokenList.size() - 3; i++) {
                threeWordSequence.add(tokenList.get(i) + " " + tokenList.get(i+1) + " " + tokenList.get(i+2));
            }

//            System.out.println(threeWordSequence);

            threeWordSequence.parallelStream().forEach(token -> {
                if(!wordMap.containsKey(token)){
                    wordMap.put(token, new LongAdder());
                }
                wordMap.get(token).increment();
            });

            wordMap.keySet().stream()
                    .sorted((p, n) -> compare(wordMap.get(n).intValue(), wordMap.get(p).intValue()))
                    .map(key -> String.format("%d%s", wordMap.get(key).intValue(), " - " + key))
                    .limit(100)
                    .forEach(w -> System.out.println("\t" + w));
//
//            System.out.println(wordMap);
            long end = Instant.now().toEpochMilli();
            System.out.println(String.format("\tCompleted in %d milliseconds", (end - start)));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
