package com.example.recommendationservice.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class FileReader {
    public String readFileAsString(String filename) {
        InputStream is = getResourceFileAsInputStream(filename);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    private InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = FileReader.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
