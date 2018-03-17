package game.ui;

import game.SpellCheckService;
import game.SpellChecker;
import game.Wordguess;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

public class WordguessGameUI {

    static SpellChecker spellChecker = new SpellCheckService();

    static Wordguess wordguess = new Wordguess(spellChecker);


    public static void main(String[] args) {

        System.out.println("************Guess the word from the hint given below************");
        Scanner scanner = new Scanner(System.in);

        try {
            List<String> wordsList = readInputFiles(Paths.get(".").toAbsolutePath().normalize().toString() + "/Wordlist.txt");
            Random random = new Random();
            int seed = random.nextInt(wordsList.size());
            String randomlyChosenWord = wordguess.getRandomlyChosenWord(wordsList, seed);
            String scrambledWord = wordguess.scramble(randomlyChosenWord, seed);

            while (true) {

                System.out.println("Hint: " + scrambledWord);
                System.out.println("Guess the word:");
			    
                 String userInput = scanner.nextLine();
			    

                if (randomlyChosenWord.equals(userInput)) {
                    System.out.println("Score:  " + WordguessGameUI.wordguess.getScore(scrambledWord, userInput) + "\nCongratulations! You won");
                    break;
                }

                System.out.println("Score: " + WordguessGameUI.wordguess.getScore(scrambledWord, userInput));
                System.out.println("Guess again!\n");

            }
        } catch (IOException ex) {

            System.out.println("Wordlist  not found!");

        } catch (RuntimeException ex) {

            System.out.println("Network Failure,try again!");
        }

    }

    private static List<String> readInputFiles(String pathToFile) throws IOException {

        return Files.lines(Paths.get(pathToFile)).collect(toList());

    }

}
