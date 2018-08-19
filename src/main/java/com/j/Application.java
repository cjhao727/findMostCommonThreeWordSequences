package com.j;

import com.j.service.FileService;

public class Application {
    public static void main(String[] args) {
        FileService fileService = new FileService();

        if (args.length != 0) {
            fileService.setArgs(args);
        } else {
            fileService.setArgs();
        }

        fileService.showFirstHundredMostCommonThreeWordSequences();
    }
}