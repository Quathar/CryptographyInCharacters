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
    void basicEncodeTest() {
        assertEquals("moqik",   Kata.encode("abcde", "abcde"));
        assertEquals("KMOQS",   Kata.encode("ABCDE", "ABCDE"));
        assertEquals("GIFUKTG", Kata.encode("DEARGOD", "FGH"));
        assertEquals("ikhwmvi", Kata.encode("deargod", "fgh"));
        assertEquals("IQlOirL", Kata.encode("ALaBamA", "hoME"));
    }
    
    @Test
    void basicDecodeTest() {
        assertEquals("abcde",   Kata.decode("moqik", "abcde"));
        assertEquals("ABCDE",   Kata.decode("KMOQS", "ABCDE"));
        assertEquals("DEARGOD", Kata.decode("GIFUKTG", "FGH"));
        assertEquals("deargod", Kata.decode("ikhwmvi", "fgh"));
        assertEquals("ALaBamA", Kata.decode("IQlOirL", "hoME"));
    }

    @Test
    void specialCasesTest() {
        assertEquals("", Kata.encode("", ""), "ERROR: Check encode method");
        assertEquals("", Kata.decode("", ""), "ERROR: Check decode method");
        assertEquals("", Kata.encode(null, null), "ERROR: Check encode method");
        assertEquals("", Kata.decode(null, null), "ERROR: Check decode method");
        assertEquals("", Kata.encode("", null), "ERROR: Check encode method");
        assertEquals("", Kata.decode("", null), "ERROR: Check decode method");
        assertEquals("", Kata.encode(null, ""), "ERROR: Check encode method");
        assertEquals("", Kata.decode(null, ""), "ERROR: Check decode method");
        
        // There is no key to encrypt
        assertEquals("TernaryLove",  Kata.encode("TernaryLove", ""), "ERROR: Check encode method");
        assertEquals("TernaryLove",  Kata.decode("TernaryLove", ""), "ERROR: Check decode method");
        assertEquals("IntelliJCrew", Kata.encode("IntelliJCrew", null), "ERROR: Check encode method");
        assertEquals("IntelliJCrew", Kata.decode("IntelliJCrew", null), "ERROR: Check decode method");
        
        // There is no phrase to encrypt
        assertEquals("", Kata.encode("", "abcde"), "ERROR: Check encode method");
        assertEquals("", Kata.decode("", "abcde"), "ERROR: Check decode method");
        assertEquals("", Kata.encode(null, "IlikeBreakInLoops"), "ERROR: Check encode method");
        assertEquals("", Kata.decode(null, "IlikeBreakInLoops"), "ERROR: Check decode method");
    }

    @Test
    void simpleOverflowEncodeTest() {
        assertEquals("CEEpB",      Kata.encode("turbo", "TUXON"));
        assertEquals("skstzCvzwj", Kata.encode("helloworld", "afgh"));
        assertEquals("IKM",        Kata.encode("xyz", "ijklmn"));
        assertEquals("qUlWuZXovQ", Kata.encode("gOdMoRNinG", "uSA"));
        assertEquals("BOywIJdrzafbLMKuCnVqNt", Kata.encode("wElovETerNaRyExpsaNdIj", "dAvID"));
    }

    @Test
    void simpleOverflowDecodeTest() {
        assertEquals("turbo",      Kata.decode("CEEpB", "TUXON"));
        assertEquals("helloworld", Kata.decode("skstzCvzwj", "afgh"));
        assertEquals("xyz",        Kata.decode("IKM", "ijklmn"));
        assertEquals("gOdMoRNinG", Kata.decode("qUlWuZXovQ", "uSA"));
        assertEquals("wElovETerNaRyExpsaNdIj", Kata.decode("BOywIJdrzafbLMKuCnVqNt", "dAvID"));
    }

    @Test
    void intermediateOverflowTest() {
        assertEquals("rhCRDjxXEVyfu", Kata.encode("hOmEsWeEtHoMe", "nOtSoSwEeT"), "ERROR: Check encode method");
        assertEquals("hOmEsWeEtHoMe", Kata.decode("rhCRDjxXEVyfu", "nOtSoSwEeT"), "ERROR: Check decode method");
        assertEquals("cyvNMxgiVt",    Kata.encode("WonDErWaLl", "oASiS"), "ERROR: Check encode method");
        assertEquals("WonDErWaLl",    Kata.decode("cyvNMxgiVt", "oASiS"), "ERROR: Check decode method");
        assertEquals("lyynmpA",       Kata.encode("Zumbido", "wow"), "ERROR: Check encode method");
        assertEquals("Zumbido",       Kata.decode("lyynmpA", "wow"), "ERROR: Check decode method");
    }

    private Random random = new Random();

    String generateString(int lowerBound, int upperBound) {
        return IntStream.rangeClosed(random.nextInt(lowerBound), random.nextInt(upperBound))
                .mapToObj(i -> random.nextInt(2) == 0 ? random.nextInt('A', 'Z' + 1) : random.nextInt('a', 'z' + 1))
                .collect(StringBuilder::new, (sb, code) -> sb.append((char) code.intValue()), StringBuilder::append)
                .toString();
    }

    @RepeatedTest(500)
    void randomEasyModeTest() {
        String phrase = generateString(1, 100);
        String key    = generateString(1, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key), "ERROR: Check encode method");
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key), "ERROR: Check decode method");
    }

    @RepeatedTest(250)
    void randomHardModeTest() {
        String phrase = generateString(1000, 15000);
        String key    = generateString(10, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key), "ERROR: Check encode method");
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key), "ERROR: Check decode method");
    }

    @RepeatedTest(100)
    void randomGodModeTest() {
        String phrase = generateString(89000, 90000);
        String key    = generateString(15, 17);
        assertEquals(Solution.encode(phrase, key), Kata.encode(phrase, key), "ERROR: Check encode method");
        assertEquals(Solution.decode(phrase, key), Kata.decode(phrase, key), "ERROR: Check decode method");
    }
    
    private static class Solution {

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

}
