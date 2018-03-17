package game;

import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;

public class Wordguess {

    private SpellChecker spellChecker;

    public Wordguess(SpellChecker theSpellChecker) {

        spellChecker = theSpellChecker;

    }

    public String getRandomlyChosenWord(List<String> wordList, int seed) {

        Random random = new Random(seed);

        int index = random.nextInt(wordList.size());

        return wordList.get(index);

    }

    public String scramble(String word, int seed) {

        List<String> letters = Stream.of(word.split("")).collect(toList());
        Collections.shuffle(letters, new Random(seed));
        return String.join("", letters);

    }

    public int getScore(String scrambledWord, String userGuess) {

        if (!spellChecker.isSpellingCorrect(userGuess)) {
            return 0;
        }

        String guessLowerCase = userGuess.toLowerCase();

        Map<String, Long> frequencyOfLettersInGuess = Stream.of(guessLowerCase.split(""))
                .collect(groupingBy(letter -> letter, counting()));

        Map<String, Long> frequencyOfLettersWord = Stream.of(scrambledWord.split(""))
                .collect(groupingBy(letter -> letter, counting()));

        if (frequencyOfLettersInGuess.keySet().stream()
                .filter(letter -> frequencyOfLettersInGuess.get(letter) > frequencyOfLettersWord.computeIfAbsent(letter, key -> 0L))
                .count() > 0) return 0;

        List<String> VOWELS = List.of("a", "e", "i", "o", "u");

        return Stream.of(guessLowerCase.split(""))
                .mapToInt(letter -> VOWELS.contains(letter) ? 1 : 2)
                .sum();

    }


}