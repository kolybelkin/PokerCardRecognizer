package test.poker.card.recognizer.service;

import test.poker.card.recognizer.service.PokerCard;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PokerCardRecognizerService {
    public static final int Y_POSITION = 590;
    public static final int CARDS_X_INTERVAL = 72;
    public static final int FIRST_CARD_X_POSITION = 146;
    public static final int CARD_WITH = 55;
    public static final int CARD_HEIGHT = 78;
    public static final int MAX_NUMBER_OF_CARDS = 5;
    public static final int LEVENSHTEIN_DISTANCE_MAX_ERROR = 20;

    public String recognizeCardAtTheCentreOfScreenshot(BufferedImage screenshot) throws IOException {
        List<BufferedImage> cardImages = new LinkedList<>();
        for (int i = 0; i < MAX_NUMBER_OF_CARDS; i++) {
            BufferedImage possibleCard = screenshot.getSubimage(FIRST_CARD_X_POSITION + (CARDS_X_INTERVAL * i), Y_POSITION, CARD_WITH, CARD_HEIGHT);
            if (!isItACard(possibleCard)) {
                break;
            }
            cardImages.add(possibleCard);
        }

        return cardImages
                .stream()
                .map(this::recognizeCard)
                .reduce(String::concat)
                .orElseThrow(IOException::new);
    }

    private String recognizeCard(BufferedImage cardImage) {
        long rankHash = convertImageToHash(cardImage.getSubimage(0, 0, 31, 27));
        long suitHash = convertImageToHash(cardImage.getSubimage(5, 29, 18, 16));
        return findLabel(PokerCard.Rank.values(), rankHash) + findLabel(PokerCard.Suit.values(), suitHash);
    }

    private String findLabel(PokerCard.HashesToLabel[] hashesToLabels, long actualHash) {
        int minimumFoundDiffValue = Integer.MAX_VALUE;
        String result = null;
        for (PokerCard.HashesToLabel value : hashesToLabels) {
            for (long nextHash : value.getHashes()) {
                int numberOfDifferentBits = compareHashes(Long.toBinaryString(nextHash), Long.toBinaryString(actualHash));
                if (numberOfDifferentBits <= LEVENSHTEIN_DISTANCE_MAX_ERROR && (numberOfDifferentBits < minimumFoundDiffValue)) {
                    minimumFoundDiffValue = numberOfDifferentBits;
                    result = value.getLabel();
                }
            }
        }
        return minimumFoundDiffValue != Integer.MAX_VALUE ? result : ".";
    }

    private int compareHashes(String hash1, String hash2) {
        int[][] dp = new int[hash1.length() + 1][hash2.length() + 1];
        for (int i = 0; i <= hash1.length(); i++) {
            for (int j = 0; j <= hash2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j - 1] + (hash1.charAt(i - 1) == hash2.charAt(j - 1) ? 0 : 1), dp[i - 1][j] + 1),
                            dp[i][j - 1] + 1
                    );
                }
            }
        }
        return dp[hash1.length()][hash2.length()];
    }

    private long convertImageToHash(BufferedImage image) {
        long hash = 0;
        BufferedImage resizedImage = resizeImageTo8x8(image);
        int bitIndex = 0;
        for (int y = 0; y < resizedImage.getHeight(); y++) {
            for (int x = 0; x < resizedImage.getWidth(); x++) {
                if (!isWhitePixel(resizedImage.getRGB(x, y))) {
                    hash |= (1L << bitIndex);
                    bitIndex++;
                }
            }
            if (hash != 0) {
                bitIndex++;
            }
        }
        return hash;
    }

    private BufferedImage resizeImageTo8x8(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, 8, 8, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private boolean isItACard(BufferedImage image) {
        int backgroundColor = image.getRGB(CARD_WITH - 1, 0);
        return isWhitePixel(backgroundColor);
    }

    private boolean isWhitePixel(int pixel) {
        Color usedGreyColor = new Color(120, 120, 120);
        return pixel == Color.WHITE.getRGB() || pixel == usedGreyColor.getRGB();
    }
}
