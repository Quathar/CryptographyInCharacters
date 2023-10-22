package com.metrica.codewars;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kata {

    // <<-CONSTANT->>
    private static final int INTERMEDIATE_OFFSET = 'a' - 'Z' + 1;
    
    // <<-METHODS->>
    private static int getLastDigit(final int digit) {
        return digit % 10;
    }

    private static List<Integer> getEncryptationOffsets(String key) {
        return key.chars().map(n -> getLastDigit(n) + key.length()).boxed().toList();
    }

    private static List<Integer> getPhraseChars(String phrase) {
        return phrase.chars().boxed().collect(Collectors.toList());
    }

    private static String parseChars(List<Integer> traduction) {
        return traduction.stream()
                .map(n -> (char) n.intValue())
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());
    }

    public static String encode(String phrase, String key) {
        if (phrase == null || phrase.isEmpty()) return "";
        if (key    == null || key   .isEmpty()) return phrase;

        List<Integer> offsets     = getEncryptationOffsets(key);
        List<Integer> phraseChars = getPhraseChars(phrase);
        List<Integer> traduction  = IntStream.range(0, phrase.length())
                .mapToObj(i -> {
                    int keyIndex = i % offsets.size();
                    int current  = phraseChars.get(i);
                    int result   = current + offsets.get(keyIndex);
                    
                    if (current <= 'Z' && result >= 'a') {
                        int offset = result - 'Z';
                        result = 'a' + offset - 1;
                    }

                    if (result > 'Z' && result < 'a') {
                        result += INTERMEDIATE_OFFSET;
                    } else if (result > 'z') {
                        result = result - 'z' + 'A' - 1;
                    }
                    
                    return result;
                }).collect(Collectors.toList());

        return parseChars(traduction);
    }

    public static String decode(String phrase, String key) {
        if (phrase == null || phrase.isEmpty()) return "";
        if (key    == null || key   .isEmpty()) return phrase;
        
        List<Integer> offsets     = getEncryptationOffsets(key);
        List<Integer> phraseChars = getPhraseChars(phrase);
        List<Integer> traduction  = IntStream.range(0, phrase.length())
                .mapToObj(i -> {
                    int keyIndex = i % offsets.size();
                    int current  = phraseChars.get(i);
                    int result   = current - offsets.get(keyIndex);
                    
                    if (current >= 'a' && result <= 'Z') {
                        int offset = 'a' - result;
                        result = 'Z' - offset + 1;
                    }

                    if (result < 'a' && result > 'Z') {
                        result -= INTERMEDIATE_OFFSET;
                    } else if (result < 'A') {
                        int offset = 'A' - result;
                        result = 'z' - offset + 1;
                    }
                    
                    return result;
                }).collect(Collectors.toList());

        return parseChars(traduction);
    }

}
