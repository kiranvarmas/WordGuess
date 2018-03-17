package game;

import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WordguessTest {

    private Wordguess wordguess;

    @Test
    void canary() {
        assertTrue(true);
    }

    @BeforeEach
    void createStub() {
        SpellChecker checker = mock(SpellChecker.class);
        when(checker.isSpellingCorrect(anyString())).thenReturn(true);

        wordguess = new Wordguess(checker);
    }

    @Test
    void ScoreWhenUserGuessOneVowelCorrect() {
        assertEquals(1, wordguess.getScore("oekmny", "o"));
    }

    @Test
    void ScoreWhenUserGuessOneConsonantCorrect() {
        assertEquals(2, wordguess.getScore("oekmny", "k"));
    }

    @Test
    void ScoreWhenUserGuessIsValidWord() {
        assertEquals(4, wordguess.getScore("oekmny", "my"));
    }

    @Test
    void ScoreWhenUserGuessIsValidWordButInvalidLetter() {
        assertEquals(0, wordguess.getScore("oekmny", "mop"));
    }

    @Test
    void ScoreWhenUserGuessMatch() {
        assertEquals(10, wordguess.getScore("oekmny", "monkey"));
    }

    @Test
    void scoreWhenANonDuplicateLetterInGuessIsDuplicated() {
        assertEquals(0, wordguess.getScore("oekmny", "moon"));
    }

    @Test
    void scoreWhenUserGuessIsSpeltWrong() {
        SpellChecker negativeWordChecker = mock(SpellChecker.class);
        when(negativeWordChecker.isSpellingCorrect("mezo")).thenReturn(false);

        wordguess = new Wordguess(negativeWordChecker);

        assertEquals(0, wordguess.getScore("oekmny", "mezo"));
    }

    @Test
    void scoreWhenTheNetworkFails() {
        SpellChecker networkChecker = mock(SpellChecker.class);
        when(networkChecker.isSpellingCorrect("mkeyu")).thenThrow(new RuntimeException("Network failed"));

        wordguess = new Wordguess(networkChecker);

        assertThrows(RuntimeException.class, () -> wordguess.getScore("oekmny", "mkeyu"), "Network failed");
    }


    @Test
    void isRandomWordPartOfWordList() {
        List<String> wordsList = Arrays.asList("monkey", "cosmopolitan", "fruit", "banana", "apple");

        assertTrue(wordsList.contains(wordguess.getRandomlyChosenWord(wordsList, 10)));
    }

    @Test
    void isRandomWordChosenSameForSameSeed() {
        List<String> wordsList = Arrays.asList("monkey", "cosmopolitan", "fruit", "banana", "apple");

        assertTrue((wordguess.getRandomlyChosenWord(wordsList, 10).equals(wordguess.getRandomlyChosenWord(wordsList, 10))));
    }

    @Test
    void isRandomWordChosenForDifferentSeedsAreDifferent() {
        List<String> wordsList = Arrays.asList("monkey", "cosmopolitan", "fruit", "banana", "apple");

        assertEquals(false, (wordguess.getRandomlyChosenWord(wordsList, 10).equals(wordguess.getRandomlyChosenWord(wordsList, 30))));
    }

    @Test
    void isWordTrulyScrambled() {
        String word = "banana";

        assertNotEquals(word, wordguess.scramble(word, 10));
    }

    @Test
    void isScrambledWordSameForTheSameSeed() {
        assertTrue(wordguess.scramble("banana", 10).equals(wordguess.scramble("banana", 10)));
    }

    @Test
    void isScrambledWordDifferentForTheDifferentSeed() {
        assertEquals(false, (wordguess.scramble("banana", 10).equals(wordguess.scramble("banana", 20))));
    }

    @Test
    void isLengthOfScrambledWordEqualsChosenWord() {
        String word = "apple";

        assertEquals(word.length(), wordguess.scramble(word, 100).length());
    }

    @Test
    void isScrambledWordEqualsOriginalWord() {
        String chosenWord = "apple";
        String scrambleWord = wordguess.scramble(chosenWord, 1);

        chosenWord = chosenWord.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .sorted()
                .collect(Collectors.joining());

        scrambleWord = scrambleWord.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .sorted()
                .collect(Collectors.joining());

        assertEquals(chosenWord, scrambleWord);
    }

}