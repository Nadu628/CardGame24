package org.example.cardgame24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipFile;

public class CardGameController {
    private Map<String, Integer> cardValues;
    private List<String> currentCardNames;
    private List<Integer> currentCardValues;

    @FXML
    private Label enterAnExpressionLabel;

    @FXML
    private TextField enteredExpressionTextField;

    @FXML
    private Button solutionButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button verifyButton;

    @FXML
    private ImageView card1ImageView;

    @FXML
    private ImageView card2ImageView;

    @FXML
    private ImageView card3ImageView;

    @FXML
    private ImageView card4ImageView;

    public CardGameController() {
        cardValues = new HashMap<>();
        for (int i = 2; i <= 10 ; i++) {
            cardValues.put(String.valueOf(i),i);
        }
        cardValues.put("Ace", 1);
        cardValues.put("Jack", 11);
        cardValues.put("Queen", 12);
        cardValues.put("King", 13);
    }

    public void initialize() {
        refreshButton.setOnAction(e -> {
            refreshCards();
        });
    }

    public List<Image> loadImageFromZip(String zipFilePath) throws Exception{
        List<Image> images = new ArrayList<>();

        try(ZipFile zipFile = new ZipFile(zipFilePath)){
            zipFile.stream().filter(zipEntry -> zipEntry.getName().endsWith(".png"))
                        .forEach(zipEntry -> {
                            try(InputStream stream = zipFile.getInputStream(zipEntry)){
                                images.add(new Image(stream));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
            }

            return images;
    }

    private List<String> getRandomDeck(){
        List<String> deck = createDeck();
        Collections.shuffle(deck);
        return deck.subList(0,4); //get 4 cards
    }

    private List<String> createDeck(){
        List<String> deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] faceCards = {"Ace", "Jack", "Queen", "King"};

        //add numberedCards
        for(String suit : suits){
            for(int i = 2; i <= 10; i++){
                deck.add(i + " of " + suit);
            }
            for(String faceCard : faceCards){
                deck.add(faceCard + " of " + suit);
            }
        }
        return deck;
    }

    public int getCardValue(String cardName) {
        return cardValues.getOrDefault(cardName, 0);
    }

    private void refreshCards(){
        currentCardNames = getRandomDeck();
        currentCardValues = new ArrayList<>();

        for(int i = 0; i < currentCardNames.size(); i++){
            String cardName = currentCardNames.get(i);
            int cardValue = getCardValue(cardName.split(" ")[0]);
            currentCardValues.add(cardValue);

            String imagePath = "/cards/." + cardName.replace(" ", "_") + ".png";
            Image cardImage = new Image(getClass().getResourceAsStream(imagePath));

            if(i == 0){
                card1ImageView.setImage(cardImage);
            }
            if(i == 1){
                card2ImageView.setImage(cardImage);
            }
            if(i == 2){
                card3ImageView.setImage(cardImage);
            }
            if(i == 3){
                card4ImageView.setImage(cardImage);
            }
        }

    }




}