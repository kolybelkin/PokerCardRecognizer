package test.poker.card.recognizer.service;

public interface PokerCard {
    interface HashesToLabel {
        long[] getHashes();
        String getLabel();
    }
    enum Rank implements HashesToLabel {
        ACE("A", new long[]{3661677L, 16235995L}),
        KING("K", new long[]{454071L, 62643695L}),
        QUEEN("Q", new long[]{1055898493L}),
        JACK("J", new long[]{15701L}),
        TEN("10", new long[]{4158512893L, 4223528701L}),
        NINE("9", new long[]{258964445L, 62643645L}),
        EIGHT("8", new long[]{62643677L, 64871869L, 64732605L}),
        SEVEN("7", new long[]{46511L, 93023L}),
        SIX("6", new long[]{3898327L, 125558493L, 129752765L, 2022319L}),
        FIVE("5", new long[]{1011159L, 978391L}),
        FOUR("4", new long[]{6258139L}),
        THREE("3", new long[]{502639L, 973751, 1956719L}),
        TWO("2", new long[]{502461L, 8238557L});
        private final long[] hashes;
        private final String label;

        Rank(String label, long[] hashes) {
            this.label = label;
            this.hashes = hashes;
        }

        @Override
        public long[] getHashes() {
            return hashes;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    enum Suit implements HashesToLabel {
        CLUBS("c", new long[]{236189382523L}),//♣
        DIAMONDS("d", new long[]{1576531933L, 7446970235L}),//♦
        HEARTS("h", new long[]{394595909567L, 29929496511L}),//♥
        SPADES("s", new long[]{8211842725853L, 4114511032251L, 255516733373L});//♠
        private final long[] hashes;
        private final String label;

        Suit(String label, long[] hashes) {
            this.label = label;
            this.hashes = hashes;
        }

        @Override
        public long[] getHashes() {
            return hashes;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }
}
