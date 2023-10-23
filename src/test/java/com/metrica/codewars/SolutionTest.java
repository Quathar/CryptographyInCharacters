package com.metrica.codewars;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    
    @Test
    void basicTest() {
        assertEquals("moqik",   Kata.encode("abcde", "abcde"));
        assertEquals("abcde",   Kata.decode("moqik", "abcde"));
        assertEquals("KMOQS",   Kata.encode("ABCDE", "ABCDE"));
        assertEquals("ABCDE",   Kata.decode("KMOQS", "ABCDE"));
        assertEquals("GIFUKTG", Kata.encode("DEARGOD", "FGH"));
        assertEquals("DEARGOD", Kata.decode("GIFUKTG", "FGH"));
        assertEquals("ikhwmvi", Kata.encode("deargod", "fgh"));
        assertEquals("deargod", Kata.decode("ikhwmvi", "fgh"));
        assertEquals("IQlOirL", Kata.encode("ALaBamA", "hoME"));
        assertEquals("ALaBamA", Kata.decode("IQlOirL", "hoME"));
    }

    @Test
    void specialCasesTest() {
        assertEquals("", Kata.encode("", ""));
        assertEquals("", Kata.encode(null, null));
        assertEquals("", Kata.encode("", null));
        assertEquals("", Kata.encode(null, ""));
        
        // There is no key to encrypt
        assertEquals("TernaryLove",  Kata.encode("TernaryLove", ""));
        assertEquals("IntelliJCrew", Kata.encode("IntelliJCrew", null));
        
        // There is no phrase to encrypt
        assertEquals("", Kata.encode("", "abcde"));
        assertEquals("", Kata.encode(null, "IlikeBreakInLoops"));
    }

    @Test
    void simpleOverflowTest() {
        assertEquals("CEEpB",      Kata.encode("turbo", "TUXON"));
        assertEquals("turbo",      Kata.decode("CEEpB", "TUXON"));
        assertEquals("skstzCvzwj", Kata.encode("helloworld", "afgh"));
        assertEquals("helloworld", Kata.decode("skstzCvzwj", "afgh"));
        assertEquals("IKM",        Kata.encode("xyz", "ijklmn"));
        assertEquals("xyz",        Kata.decode("IKM", "ijklmn"));
        assertEquals("qUlWuZXovQ", Kata.encode("gOdMoRNinG", "uSA"));
        assertEquals("gOdMoRNinG", Kata.decode("qUlWuZXovQ", "uSA"));

        assertEquals("BOywIJdrzafbLMKuCnVqNt", Kata.encode("wElovETerNaRyExpsaNdIj", "dAvID"));
        assertEquals("wElovETerNaRyExpsaNdIj", Kata.decode("BOywIJdrzafbLMKuCnVqNt", "dAvID"));
    }

    @Test
    void intermediateOverflowTest() {
        assertEquals("rhCRDjxXEVyfu", Kata.encode("hOmEsWeEtHoMe", "nOtSoSwEeT"));
        assertEquals("hOmEsWeEtHoMe", Kata.decode("rhCRDjxXEVyfu", "nOtSoSwEeT"));
        assertEquals("cyvNMxgiVt",    Kata.encode("WonDErWaLl", "oASiS"));
        assertEquals("WonDErWaLl",    Kata.decode("cyvNMxgiVt", "oASiS"));
        assertEquals("lyynmpA",       Kata.encode("Zumbido", "wow"));
        assertEquals("Zumbido",       Kata.decode("lyynmpA", "wow"));
    }

    private Random random = new Random();

    String generateString(int lowerBound, int upperBound) {
        return IntStream.rangeClosed(random.nextInt(lowerBound + 1), random.nextInt(upperBound + 1))
                .mapToObj(i -> random.nextInt(2) == 0 ? random.nextInt('A', 'Z' + 1) : random.nextInt('a', 'z' + 1))
                .collect(StringBuilder::new, (sb, code) -> sb.append((char) code.intValue()), StringBuilder::append)
                .toString();
    }

    @RepeatedTest(15000)
    void randomEasyModeTest() {
        String phrase = generateString(0, 100);
        String key    = generateString(0, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key));
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key));
    }

    @RepeatedTest(1000)
    void randomHardModeTest() {
        String phrase = generateString(1000, 15000);
        String key    = generateString(10, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key));
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key));
    }

    @RepeatedTest(100)
    void randomGodModeTest() {
        String phrase = generateString(89000, 90000);
        String key    = generateString(15, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key));
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key));
    }

}

class Solution {

    // <<-CONSTANT->>
    private static final int INTERMEDIATE_OFFSET = 'a' - 'Z' - 1;
    
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