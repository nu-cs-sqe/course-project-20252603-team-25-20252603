package domain.setup;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.board.Board;
import domain.deck.DevelopmentCardDeck;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.turn.TurnOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameTest {

    @Test
    void tc1_validGameStoresAllParts() {
        List<Player> players = players();
        Board board = Board.generateRandom(new Random(1));
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));
        TurnOrder turnOrder = new TurnOrder(players);

        Game game = new Game(players, board, deck, turnOrder);

        assertAll(
            () -> assertEquals(players, game.players()),
            () -> assertSame(board, game.board()),
            () -> assertSame(deck, game.deck()),
            () -> assertSame(turnOrder, game.turnOrder())
        );
    }

    @Test
    void tc2_nullPlayersRejected() {
        assertThrows(NullPointerException.class,
            () -> new Game(null, board(), deck(), turnOrder()));
    }

    @Test
    void tc3_emptyPlayersRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Game(List.of(), board(), deck(), turnOrder()));
    }

    @Test
    void tc4_nullPlayerEntryRejected() {
        List<Player> players = new ArrayList<>(players());
        players.set(1, null);

        assertThrows(IllegalArgumentException.class,
            () -> new Game(players, board(), deck(), turnOrder()));
    }

    @Test
    void tc5_nullPartsRejected() {
        List<Player> players = players();

        assertAll(
            () -> assertThrows(NullPointerException.class,
                () -> new Game(players, null, deck(), turnOrder())),
            () -> assertThrows(NullPointerException.class,
                () -> new Game(players, board(), null, turnOrder())),
            () -> assertThrows(NullPointerException.class,
                () -> new Game(players, board(), deck(), null))
        );
    }

    @Test
    void tc6_playersListIsDefensivelyCopied() {
        List<Player> players = new ArrayList<>(players());
        Game game = new Game(players, board(), deck(), new TurnOrder(players));

        players.clear();

        assertEquals(2, game.players().size());
    }

    @Test
    void tc7_playersListIsUnmodifiable() {
        Game game = new Game(players(), board(), deck(), turnOrder());

        assertThrows(UnsupportedOperationException.class,
            () -> game.players().add(new Player("Arjun", PlayerColor.WHITE)));
    }

    private static List<Player> players() {
        return List.of(
            new Player("Yuki", PlayerColor.RED),
            new Player("Minji", PlayerColor.BLUE)
        );
    }

    private static Board board() {
        return Board.generateRandom(new Random(1));
    }

    private static DevelopmentCardDeck deck() {
        return DevelopmentCardDeck.standardShuffled(new Random(1));
    }

    private static TurnOrder turnOrder() {
        return new TurnOrder(players());
    }
}
