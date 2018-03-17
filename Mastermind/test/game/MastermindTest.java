package game;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static game.Mastermind.Response.*;
import static game.Mastermind.*;


public class MastermindTest {

    Mastermind mastermind;

    @Test
    void canary() {
        assertTrue(true);
    }

    @BeforeEach
    void init() {
        List<Color> selection = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        mastermind = new Mastermind(selection);
    }

    @Test
    void noColorMatches() {

        List<Color> userGuess = Arrays.asList(Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(0, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(0, (long) response.get(MATCH)),
                () -> assertEquals(6, (long) response.get(NO_MATCH)));
    }


    @Test
    void oneColorMatches() {

        List<Color> userGuess = Arrays.asList(Color.ORANGE, Color.ORANGE, Color.BLUE, Color.ORANGE, Color.ORANGE, Color.ORANGE);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(0, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(1, (long) response.get(MATCH)),
                () -> assertEquals(5, (long) response.get(NO_MATCH)));
    }


    @Test
    void oneColorPositionMatches() {

        List<Color> userGuess = Arrays.asList(Color.ORANGE, Color.ORANGE, Color.GREEN, Color.ORANGE, Color.ORANGE, Color.ORANGE);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(
                () -> assertEquals(1, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(0, (long) response.get(MATCH)),
                () -> assertEquals(5, (long) response.get(NO_MATCH)));
    }


    @Test
    void twoColorMatches() {

        List<Color> userGuess = Arrays.asList(Color.GREEN, Color.BLACK, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(0, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(2, (long) response.get(MATCH)),
                () -> assertEquals(4, (long) response.get(NO_MATCH)));
    }


    @Test
    void allColorPositionMatches() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(6, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(0, (long) response.get(MATCH)),
                () -> assertEquals(0, (long) response.get(NO_MATCH)));

    }


    @Test
    void multiplePositionMisMatch() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE, Color.BLACK, Color.WHITE, Color.CYAN);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(4, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(2, (long) response.get(MATCH)),
                () -> assertEquals(0, (long) response.get(NO_MATCH)));

    }


    @Test
    void PositionMatchedColorRepeat() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.RED, Color.BLACK, Color.CYAN);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(4, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(1, (long) response.get(MATCH)),
                () -> assertEquals(1, (long) response.get(NO_MATCH)));

    }


    @Test
    void OnlyMatchedColorRepeat() {

        List<Color> userGuess = Arrays.asList(Color.CYAN, Color.WHITE, Color.BLACK, Color.RED, Color.BLUE, Color.RED);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(0, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(5, (long) response.get(MATCH)),
                () -> assertEquals(1, (long) response.get(NO_MATCH)));

    }


    @Test
    void onePositionalMatch5noMatch() {

        List<Color> userGuess = Arrays.asList(Color.BLUE, Color.BLUE, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.ORANGE);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(

                () -> assertEquals(1, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(0, (long) response.get(MATCH)),
                () -> assertEquals(5, (long) response.get(NO_MATCH)));
    }


    @Test
    void oneColorMatch5noMatch() {

        List<Color> userGuess = Arrays.asList(Color.ORANGE, Color.RED, Color.ORANGE, Color.ORANGE, Color.ORANGE, Color.RED);

        Map<Response, Long> response = mastermind.guess(userGuess);

        assertAll(
                () -> assertEquals(0, (long) response.get(POSITIONAL_MATCH)),
                () -> assertEquals(1, (long) response.get(MATCH)),
                () -> assertEquals(5, (long) response.get(NO_MATCH)));
    }


    @Test
    void gameStartStatus() {

        assertEquals(GameStatus.PLAYING, mastermind.getGameStatus());
    }


    @Test
    void gameStatusAfteroneIncorrectGuesses() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.CYAN, Color.WHITE, Color.BLUE, Color.GREEN, Color.BLUE);

        mastermind.guess(userGuess);

        assertEquals(GameStatus.PLAYING, mastermind.getGameStatus());
    }


    @Test
    void gameStatusAftertwoIncorrectGuesses() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.CYAN, Color.WHITE, Color.BLUE, Color.GREEN, Color.BLUE);

        mastermind.guess(userGuess);
        mastermind.guess(userGuess);

        assertEquals(GameStatus.PLAYING, mastermind.getGameStatus());
    }


    @Test
    void gameStatusAfterCorrectGuess() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);

        mastermind.guess(userGuess);

        assertEquals(GameStatus.WON, mastermind.getGameStatus());
    }


    @Test
    void guessStatusAfterPlayerWon() {

        List<Color> matchGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);
        List<Color> nonmatchGuess = Arrays.asList(Color.RED, Color.RED, Color.RED, Color.ORANGE, Color.ORANGE, Color.CYAN);

        mastermind.guess(matchGuess);
        mastermind.guess(nonmatchGuess);

        assertEquals(GameStatus.WON, mastermind.getGameStatus());
    }


    @Test
    void gameStatusAfter20Unsuccessfultrials() {

        List<Color> userGuess = Arrays.asList(Color.RED, Color.CYAN, Color.WHITE, Color.BLUE, Color.GREEN, Color.BLUE);
        mastermind.tries = 19;

        mastermind.guess(userGuess);

        assertEquals(GameStatus.LOST, mastermind.getGameStatus());

    }


    @Test
    void gameStatusAtsuccessfulltrial20() {

        List<Color> correctGuess = Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.WHITE, Color.CYAN);
        mastermind.tries = 19;

        mastermind.guess(correctGuess);

        assertEquals(GameStatus.WON, mastermind.getGameStatus());
    }


    @Test
    void callGenerateRandomColorsOnInstantiation() {

        AtomicBoolean called = new AtomicBoolean();

        Mastermind stub = new Mastermind() {
            public List<Color> generateRandomColors() {
                List<Color> randomColors = new ArrayList<Color>();
                called.set(true);
                return randomColors;
            }
        };

        assertTrue(called.get());
    }


    @Test
    void randomColorGeneratorTest() {

        List<Color> firstRandom = mastermind.generateRandomColors();
        List<Color> secondRandom = mastermind.generateRandomColors();

        assertEquals(false, firstRandom.equals(secondRandom));
    }

    @Test
    void toSuppressCoverageIssueWithEnum() {

        Response.values();
        Response.valueOf("NO_MATCH");

        GameStatus.values();
        GameStatus.valueOf("WON");

        assertTrue(true);
    }
}
