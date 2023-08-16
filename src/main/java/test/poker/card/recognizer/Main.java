package test.poker.card.recognizer;

import test.poker.card.recognizer.service.PokerCardRecognizerService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        String absolutePathToImageDir = args[0];
        File imageDir = Paths.get(absolutePathToImageDir).toFile();
        File[] images = imageDir.listFiles(file -> file.isFile() && file.getName().endsWith(".png"));
        PokerCardRecognizerService recognizerService = new PokerCardRecognizerService();
        for (File image : images) {
            try {
                String actualName = recognizerService.recognizeCardAtTheCentreOfScreenshot(ImageIO.read(image));
                System.out.println(String.format("%s - %s", image.getName(), actualName));
            } catch (IOException e) {
                System.err.println(String.format("Error occurred while recognizing file - %s", image.getName()));
                e.printStackTrace();
            }
        }
    }
}