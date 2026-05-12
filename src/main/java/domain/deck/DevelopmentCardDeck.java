package domain.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

public final class DevelopmentCardDeck {

    private static final int KNIGHT_COUNT = 14;
    private static final int VICTORY_POINT_COUNT = 5;
    private static final int SPECIAL_PROGRESS_COUNT = 2;

    private final List<DevelopmentCard> cards;

    private DevelopmentCardDeck(List<DevelopmentCard> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public static DevelopmentCardDeck standardShuffled(Random rng) {
        if (rng == null) {
            throw new IllegalArgumentException("rng must not be null");
        }
        List<DevelopmentCard> cards = standardCards();
        Collections.shuffle(cards, rng);
        return new DevelopmentCardDeck(cards);
    }

    public int size() {
        return cards.size();
    }

    public DevelopmentCard draw() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("deck is empty");
        }
        return cards.remove(0);
    }

    public Map<DevelopmentCardType, Long> typeCounts() {
        EnumMap<DevelopmentCardType, Long> counts =
            new EnumMap<>(DevelopmentCardType.class);
        for (DevelopmentCardType type : DevelopmentCardType.values()) {
            counts.put(type, 0L);
        }
        for (DevelopmentCard card : cards) {
            counts.put(card.getType(), counts.get(card.getType()) + 1L);
        }
        return Collections.unmodifiableMap(counts);
    }

    private static List<DevelopmentCard> standardCards() {
        List<DevelopmentCard> cards = new ArrayList<>();
        addCards(cards, DevelopmentCardType.KNIGHT, KNIGHT_COUNT);
        addCards(cards, DevelopmentCardType.VICTORY_POINT, VICTORY_POINT_COUNT);
        addCards(cards, DevelopmentCardType.ROAD_BUILDING, SPECIAL_PROGRESS_COUNT);
        addCards(cards, DevelopmentCardType.MONOPOLY, SPECIAL_PROGRESS_COUNT);
        addCards(cards, DevelopmentCardType.YEAR_OF_PLENTY, SPECIAL_PROGRESS_COUNT);
        return cards;
    }

    private static void addCards(
            List<DevelopmentCard> cards,
            DevelopmentCardType type,
            int count) {
        for (int i = 0; i < count; i++) {
            cards.add(new DevelopmentCard(type));
        }
    }
}
