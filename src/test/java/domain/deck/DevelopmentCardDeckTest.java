package domain.deck;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DevelopmentCardDeckTest {

    @Test
    void tc1_standardDeckHasTwentyFiveCards() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));

        assertEquals(25, deck.size());
    }

    @Test
    void tc2_standardDeckHasCatanDevelopmentCardCounts() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));

        assertEquals(standardCounts(), deck.typeCounts());
    }

    @Test
    void tc3_nullRandomRejected() {
        assertThrows(NullPointerException.class,
            () -> DevelopmentCardDeck.standardShuffled(null));
    }

    @Test
    void tc4_sameSeedProducesSameDrawOrder() {
        DevelopmentCardDeck first = DevelopmentCardDeck.standardShuffled(new Random(7));
        DevelopmentCardDeck second = DevelopmentCardDeck.standardShuffled(new Random(7));

        assertEquals(drawAllTypes(first), drawAllTypes(second));
    }

    @Test
    void tc5_differentSeedsCanProduceDifferentDrawOrder() {
        DevelopmentCardDeck first = DevelopmentCardDeck.standardShuffled(new Random(7));
        DevelopmentCardDeck second = DevelopmentCardDeck.standardShuffled(new Random(8));

        assertFalse(drawAllTypes(first).equals(drawAllTypes(second)));
    }

    @Test
    void tc6_drawReturnsCardAndReducesSize() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));

        DevelopmentCard card = deck.draw();

        assertAll(
            () -> assertEquals(24, deck.size()),
            () -> assertEquals(DevelopmentCard.class, card.getClass())
        );
    }

    @Test
    void tc7_drawFromEmptyDeckRejected() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));
        for (int i = 0; i < 25; i++) {
            deck.draw();
        }

        assertThrows(NoSuchElementException.class, deck::draw);
    }

    @Test
    void tc8_typeCountsReflectDrawnCard() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));
        DevelopmentCard drawn = deck.draw();
        Map<DevelopmentCardType, Long> expected = standardCounts();
        expected.put(drawn.getType(), expected.get(drawn.getType()) - 1L);

        assertEquals(expected, deck.typeCounts());
    }

    @Test
    void tc9_typeCountsIsUnmodifiable() {
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(new Random(1));

        assertThrows(UnsupportedOperationException.class,
            () -> deck.typeCounts().put(DevelopmentCardType.KNIGHT, 0L));
    }

    private static List<DevelopmentCardType> drawAllTypes(DevelopmentCardDeck deck) {
        List<DevelopmentCardType> types = new ArrayList<>();
        while (deck.size() > 0) {
            types.add(deck.draw().getType());
        }
        return types;
    }

    private static Map<DevelopmentCardType, Long> standardCounts() {
        EnumMap<DevelopmentCardType, Long> counts =
            new EnumMap<>(DevelopmentCardType.class);
        counts.put(DevelopmentCardType.KNIGHT, 14L);
        counts.put(DevelopmentCardType.VICTORY_POINT, 5L);
        counts.put(DevelopmentCardType.ROAD_BUILDING, 2L);
        counts.put(DevelopmentCardType.MONOPOLY, 2L);
        counts.put(DevelopmentCardType.YEAR_OF_PLENTY, 2L);
        return counts;
    }
}
