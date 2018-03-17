package game;

import java.awt.Color;
import java.util.*;
import java.util.List;

import static game.Mastermind.Response.*;

import java.util.stream.IntStream;
import java.util.function.IntFunction;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;

public class Mastermind {
    public enum GameStatus {PLAYING, WON, LOST}

    public enum Response {POSITIONAL_MATCH, MATCH, NO_MATCH}

    private final int SIZE = 6;
    protected int tries = 0;
    private List<Color> generatedColors;
    private GameStatus gameStatus = GameStatus.PLAYING;

    protected Mastermind(List<Color> selection) {
        generatedColors = selection;
    }

    public Mastermind() {
        generatedColors = generateRandomColors();
    }

    public List<Color> generateRandomColors() {


        List<Color> randomColors = getAvailableColors();
        Random random = new Random();
        int seed = random.nextInt();
        Collections.shuffle(randomColors,new Random(seed));
        return randomColors.subList(0, 6);
    }

    public List<Color> getAvailableColors() {

        return Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.LIGHT_GRAY, Color.CYAN, Color.ORANGE, Color.DARK_GRAY, Color.MAGENTA, Color.PINK);
    }


    private void updateGameStatus(Map<Response, Long> response) {

        if (gameStatus != GameStatus.PLAYING) return;

        if (tries >= 20) {
            gameStatus = GameStatus.LOST;
        }

        if (response.get(POSITIONAL_MATCH) == 6) {
            gameStatus = GameStatus.WON;
        }
    }


    public Map<Response, Long> guess(List<Color> userGuess) {

        tries++;

        IntFunction<Response> computeMatchAtPosition = index ->
                generatedColors.get(index) == userGuess.get(index) ? POSITIONAL_MATCH :
                        userGuess.contains(generatedColors.get(index)) ? MATCH : NO_MATCH;

        Map<Response, Long> response =
                IntStream.range(0, SIZE)
                        .mapToObj(computeMatchAtPosition)
                        .collect(groupingBy(Function.identity(), counting()));

        response.computeIfAbsent(NO_MATCH, key -> 0L);
        response.computeIfAbsent(MATCH, key -> 0L);
        response.computeIfAbsent(POSITIONAL_MATCH, key -> 0L);

        updateGameStatus(response);

        return response;

    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }


}