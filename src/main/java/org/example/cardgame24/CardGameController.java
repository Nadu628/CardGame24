package org.example.cardgame24;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class CardGameController {
    private Map<String, Integer> cardValues;
    private List<String> currentCardNames;
    private List<Integer> currentCardValues;

    @FXML
    private Label enterAnExpressionLabel;

    @FXML
    private Label resultLabel;

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
        cardValues.put("ace", 1);
        cardValues.put("jack", 11);
        cardValues.put("queen", 12);
        cardValues.put("king", 13);


    }

    public void initialize() {
        System.out.println("initalize");
        currentCardValues= new ArrayList<>();
        refreshButton.setOnAction(e -> {
            refreshCards();
        });

        verifyButton.setOnAction(e -> {
            verifyExpression();
        });

        refreshCards();
    }

    private List<String> createDeck(){
        List<String> deck = new ArrayList<>();
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};
        String[] faceCards = {"ace", "jack", "queen", "king"};

        //add numberedCards
        for(String suit : suits){
            for(int i = 2; i <= 10; i++){
                deck.add(i + "_of_" + suit + ".png");
            }
            for(String faceCard : faceCards){
                deck.add(faceCard + "_of_" + suit + ".png");
            }
        }
        return deck;
    }

    private List<String> getRandomDeck(){
        List<String> deck = createDeck();
        Collections.shuffle(deck);
        return deck.subList(0,4); //get 4 cards
    }

    public int getCardValue(String cardName) {
        return cardValues.getOrDefault(cardName.split("_")[0], 0);
    }

    @FXML
    private void refreshCards() {
        currentCardNames = getRandomDeck();
        currentCardValues.clear();

        displayCards();
    }
    private void displayCards(){
        ImageView[] imageViews = {card1ImageView, card2ImageView, card3ImageView, card4ImageView};

        for (int i = 0; i < currentCardNames.size(); i++) {
            String imageFileName = currentCardNames.get(i);
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("org/example/cardgame24/cards/" + imageFileName);
            if(imageStream != null){
                Image cardImage = new Image(imageStream);
                imageViews[i].setImage(cardImage);
            }else{
                System.out.println("Image not found");
            }

        }
    }

    private boolean isValidExpression(String expression) {
        String[] tokens = expression.split(" ");
        Set<Integer> usedCards = new HashSet<>();

        for (String token : tokens) {
            if(cardValues.containsKey(token)){
                usedCards.add(cardValues.get(token));
            }
        }
        return usedCards.size() == 4 && usedCards.containsAll(new HashSet<>(currentCardValues));
    }

    private double evaluateExpression(String expression) throws Exception {
        expression = expression.replace(" ", "");
        double result = 0;
        String[] parts = expression.split("[-+*/]");

        result = Double.parseDouble(parts[0]);

        for(int i = 1; i < parts.length; i++){
            char operator = expression.charAt(parts[i-1].length());
            double nextNum = Double.parseDouble(parts[i]);

            switch(operator){
                case '+':
                    result += nextNum;
                    break;
                case '-':
                    result -= nextNum;
                    break;
                case '*':
                    result *= nextNum;
                    break;
                case '/':
                    if(nextNum == 0){
                        result /= nextNum;
                    }else{
                        throw new ArithmeticException("Division by zero");
                    }
                    break;

            }
        }
        return result;
    }

    private void verifyExpression() {
        String expression = enteredExpressionTextField.getText().trim();

        try{
            if(isValidExpression(expression)){
                double result = evaluateExpression(expression);
                if(result == 24){
                    resultLabel.setText("Correct result: " + result);
                }else{
                    resultLabel.setText("Incorrect result: " + result + ". Try again.");
                }
            }else{
                resultLabel.setText("Invalid expression. Try again.");
            }
        }catch(Exception e){
            resultLabel.setText("Invalid expression. Try again.");
        }
    }






    public void testImageAccess() {
        String testImage = "cards/10_of_spades.png";
        URL testUrl = getClass().getClassLoader().getResource(testImage);

        if (testUrl == null) {
            System.out.println("Test Image NOT FOUND: " + testImage);
        } else {
            System.out.println("Test Image FOUND at: " + testUrl);
        }
    }




}