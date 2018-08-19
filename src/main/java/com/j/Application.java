package com.j;

import com.j.service.FileService;

import java.util.Arrays;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        FileService fileService = new FileService();
        if (args.length != 0) {
            Arrays.stream(args).forEach(fileService::getMostCommonThreeWordSequences);
        } else {
            System.out.print("Enter Parameter : ");
            Scanner scanner = new Scanner(System.in);
            String t = scanner.next().trim();

            String[] argList = t.split("\\s+");

            Arrays.stream(argList).forEach(fileService::getMostCommonThreeWordSequences);
//            fileService.getMostCommonThreeWordSequences(t);
        }
    }
}