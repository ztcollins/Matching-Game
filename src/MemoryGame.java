import java.io.File;
import java.util.Arrays;
import processing.core.PApplet;
import processing.core.PImage;

//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Memory Game
// Course: CS 300 Spring 2021
//
// Author: Zachary Collins
// Email: ztcollins@wisc.edu
// Lecturer: Mouna Kacem
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: -
// Online Sources: -
//
///////////////////////////////////////////////////////////////////////////////
public class MemoryGame {

  // Congratulations message
  private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
  // Cards not matched message
  private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
  // Cards matched message
  private final static String MATCHED = "CARDS MATCHED! Good Job!";
  // 2D-array which stores cards coordinates on the window display
  private final static float[][] CARDS_COORDINATES =
      new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170}, {170, 324}, {324, 324},
          {478, 324}, {632, 324}, {170, 478}, {324, 478}, {478, 478}, {632, 478}};
  // Array that stores the card images filenames
  private final static String[] CARD_IMAGES_NAMES = new String[] {"ball.png", "redFlower.png",
      "yellowFlower.png", "apple.png", "peach.png", "shark.png"};

  private static PApplet processing; // PApplet object that represents
  // the graphic display window
  private static Card[] cards = new Card[CARDS_COORDINATES.length]; // one dimensional array of
                                                                    // cards
  private static PImage[] images = new PImage[CARD_IMAGES_NAMES.length]; // array of images of the
                                                                         // different cards
  private static Card selectedCard1; // First selected card
  private static Card selectedCard2; // Second selected card
  private static boolean winner; // boolean evaluated true if the game is won,
  // and false otherwise
  private static int matchedCardsCount; // number of cards matched so far
  // in one session of the game
  private static String message; // Displayed message to the display window
  // shuffle array
  private static int[] shuffledArray = new int[CARDS_COORDINATES.length];

  public static void main(String[] args) {
    Utility.startApplication();

  }


  /**
   * Defines the initial environment properties of this game as the program starts
   */
  public static void setup(PApplet processing) {
    // startNewGame();
    MemoryGame.processing = processing;
    // Set the color used for the background of the Processing window
    processing.background(245, 255, 250); // Mint cream color

    //images[] = new PImage[CARD_IMAGES_NAMES.length];
    // load ball.png image file as PImage object and store its reference into images[0]
    
    for (int i = 0; i < CARD_IMAGES_NAMES.length; i++) {
      images[i] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[i]);
    }
    startNewGame();
    if (processing.key == 'n'|| processing.key == 'N') {
      keyPressed();
    }
  }


  /**
   * Initializes the Game
   * 
   * @param imagesNew
   */
  public static void startNewGame() {
    selectedCard1 = null;
    selectedCard2 = null;
    winner = false;
    message = "";
    count = 0;
    matchedCardsCount = 0;
    
    shuffledArray = Utility.shuffleCards(CARDS_COORDINATES.length);
    
    cards = new Card[CARDS_COORDINATES.length];

    for (int i = 0; i < CARDS_COORDINATES.length; i++) {
      cards[i] =
          new Card(images[shuffledArray[i]], CARDS_COORDINATES[i][0], CARDS_COORDINATES[i][1]);
    }



  }


  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    startNewGame();
  }


  /**
   * Callback method draws continuously this application window display
   */
  public static void draw() {
    for (int i = 0; i < CARDS_COORDINATES.length; i++) {
      // cards[i].setVisible(true);
      cards[i].draw();
    }
    displayMessage(message);
  }

  /**
   * Displays a given message to the display window
   * 
   * @param message to be displayed to the display window
   */
  public static void displayMessage(String message) {
    processing.fill(0);
    processing.textSize(20);
    processing.text(message, processing.width / 2, 50);
    processing.textSize(12);
  }


  /**
   * Checks whether the mouse is over a given Card
   * 
   * @return true if the mouse is over the storage list, false otherwise
   */
  public static boolean isMouseOver(Card card) {


    if ((processing.mouseX >= (card.getX() - card.getWidth() / 2))
        && (processing.mouseX <= (card.getX() + card.getWidth() / 2))
        && (processing.mouseY <= (card.getY() + card.getHeight() / 2))
        && (processing.mouseY >= (card.getY() - card.getHeight() / 2))) {
      return true;
    } else {
      return false;
    }
  }


  /**
   * Callback method called each time the user presses the mouse
   */
  static int count = 0;
  public static void mousePressed() {
    
    if(count==2) {
      if(selectedCard1 != null && selectedCard2 != null) {
        if(matchingCards(selectedCard1, selectedCard2)) {
        selectedCard1.setMatched(true);
        selectedCard1.deselect();
        selectedCard2.deselect();
        //System.out.println("3");
        selectedCard1 = null;
        selectedCard2 = null;
        count = 0;
      }
      else {
      selectedCard1.deselect();
      selectedCard2.deselect();
      selectedCard1.setVisible(false);
      selectedCard2.setVisible(false);
      selectedCard1 = null;
      selectedCard2 = null;
      count = 0;
      //System.out.println("4");
      }
      }
      
    }
    
    if(count == 0) {
       for (int i = 0; i < cards.length; i++) {
      if(isMouseOver(cards[i])) {
       // System.out.println("card found 1");
        selectedCard1 = cards[i];
        selectedCard1.select();
        selectedCard1.setVisible(true);
        break;
      }
    }
    }
    if(count == 1) {
      for (int i = 0; i < cards.length; i++) {
     if(isMouseOver(cards[i])) {
       //System.out.println("card found 2");
       selectedCard2 = cards[i];
       selectedCard2.select();
       selectedCard2.setVisible(true);
       break;
     }
   }
   }
    count++;
    if(count==2) {
      if(selectedCard1 != null && selectedCard2 != null) {
        matchingCards(selectedCard1, selectedCard2);
      }
      else {
        count = 1;
      }
       
    }
   
  }


  /**
   * Checks whether two cards match or not
   * 
   * @param card1 reference to the first card
   * @param card2 reference to the second card
   * @return true if card1 and card2 image references are the same, false otherwise
   */
  public static boolean matchingCards(Card card1, Card card2) {
    if (card1.getImage().equals(card2.getImage())) {
      matchedCardsCount++;
      displayMessage(MATCHED);
      if(matchedCardsCount>5) {
        winner = true;
        displayMessage(CONGRA_MSG);
      }
      return true;
    } else {
      displayMessage(NOT_MATCHED);
      return false;
    }
  }



}
