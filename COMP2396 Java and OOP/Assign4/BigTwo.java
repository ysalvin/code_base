import java.util.ArrayList;

// import javax.smart.cardio.Card;

/**
 * This class is used for modelling a Big Two card game, icluding the game
 * interface.
 */

public class BigTwo implements CardGame {

    // public constructor

    /**
     * a constructor for creating a Big Two card game, it will create 4 players and
     * store them in a player list, then the constuctor will create a UI Object for
     * the UI interface
     * 
     */
    public BigTwo() {
        ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
        playerList.add(new CardGamePlayer());
        playerList.add(new CardGamePlayer());
        playerList.add(new CardGamePlayer());
        playerList.add(new CardGamePlayer());
        this.playerList = playerList;
        // System.out.println(playerList.size());
        this.handsOnTable = new ArrayList<Hand>();
        // this.handsOnTable.clear();
        // this.handsOnTable.add(new Hand(0, 3));
        BigTwoGUI ui = new BigTwoGUI(this);
        this.ui = ui;
    }

    // private instance variables
    private int numOfPlayers;
    private Deck Deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private int currentPlayerIdx;
    private BigTwoGUI ui;

    // public methods (CardGame interface)
    /**
     * a method for getting the number of players
     * 
     * @return numer of players in this game
     */
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
     * a method for getting the deck of bigtwo game
     * 
     * @return the deck of the game
     */
    public Deck getDeck() {
        return this.Deck;
    }

    /**
     * a method for getting the player list
     * 
     * @return the player list of the game
     */
    public ArrayList<CardGamePlayer> getPlayerList() {
        return this.playerList;
    }

    /**
     * a method for getting the hand list on the game table
     * 
     * @return the hand list on the game table
     */
    public ArrayList<Hand> getHandsOnTable() {
        return this.handsOnTable;
    }

    /**
     * a method for getting the the index of the current player.
     * 
     * @return the index of the current player
     */
    public int getCurrentPlayerIdx() {
        return this.currentPlayerIdx;
    }

    private int ThreeOfDiamond() {
        for (int i = 0; i < 4; i++) {
            if (this.playerList.get(i).getCardsInHand().contains(new BigTwoCard(0, 2))) {
                return i;
            }
        }
        return -1;
    }

    public Hand getLastHand() {
        if (handsOnTable.size() > 0) {
            return handsOnTable.get(handsOnTable.size() - 1);
        } else {
            return null;
        }
    }

    /**
     * a method for start the game.
     * It will do the following steps:
     * 1.remove all the cards from players and the table deck
     * 2. distribute the cards to the players
     * 3. search for the player who own the Three of Diamond
     * 
     * @parm the deck of the game
     */
    public void start(Deck deck) {
        // this.handsOnTable = new ArrayList<Hand>();
        // (i) remove all the cards from the players as well as from the table
        for (int i = 0; i < 4; i++) {
            this.playerList.get(i).removeAllCards();
        }
        // (ii) distribute the cards to the players
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                this.playerList.get(i).addCard(deck.getCard(13 * i + j));
            }
            this.playerList.get(i).sortCardsInHand();
        }
        // boolean firstMove = true;
        // (iii) identify the player who holds the Three of Diamond
        this.currentPlayerIdx = ThreeOfDiamond();
        // (iv) set both the currentPlayerIdx of the BigTwo object and the activePlayer
        // of the BigTwoUI object to the index of the player who holds the Three of
        // Diamonds
        this.ui.setActivePlayer(this.currentPlayerIdx);
        // (v) call the repaint() method of the BigTwoUI object to show the cards on the
        // table
        playerList.get(currentPlayerIdx).sortCardsInHand();
        // ui.printMsg(this.playerList.get(currentPlayerIdx).getName() + "'s turn: ");
        // this.ui.repaint();

        // (vi) call the promptActivePlayer() method of the BigTwoUI object to prompt
        // user to select cards and make his/her move.
        if (!endOfGame()) {
            playerList.get(currentPlayerIdx).sortCardsInHand();
            this.ui.setActivePlayer(currentPlayerIdx);
            playerList.get(currentPlayerIdx).sortCardsInHand();
            ui.repaint();
            ui.promptActivePlayer();
            if (currentPlayerIdx != 3) {
                currentPlayerIdx++;
            } else {
                currentPlayerIdx = 0;
            }
        }
    }

    /**
     * a method for checking if this is a valid StraightFlush
     * hand.
     * 
     * @parm playerIdx an integer specifying the index of the current player.
     * @parm an interger array of storing the index of cards in the cardlistthat the
     *       player play in each hand
     * 
     * 
     */
    public void makeMove(int playerIdx, int[] cardIdx) {
        checkMove(playerIdx, cardIdx);
    }

    /**
     * a method for checking the move made by a player
     * 
     * @parm playerIdx an integer specifying the index of the current player.
     * @parm an interger array of storing the index of cards in the cardlistthat the
     *       player play in each hand
     */
    public void checkMove(int playerIdx, int[] cardIdx) {
        boolean last = false;
        if (handsOnTable.size() != 0) {
            last = handsOnTable.get(handsOnTable.size() - 1).getPlayer() == playerList.get(playerIdx);
        }
        if (cardIdx == null) {
            if (last || handsOnTable.size() == 0) {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            } else {
                ui.printMsg("{Pass}");

                System.out.println("");
                System.out.println("");
                return;
            }
        } else {
            Boolean first = true;
            Hand hand = composeHand(playerList.get(playerIdx), playerList.get(playerIdx).play(cardIdx));
            if (hand == null) {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            }
            if (handsOnTable.size() == 0) { // first player, must play diamond3
                if (playerList.get(playerIdx).play(cardIdx).contains(new BigTwoCard(0, 2))) {
                    handsOnTable.add(hand);
                    playerList.get(playerIdx).removeCards(hand);
                    if (currentPlayerIdx != 3) {
                        currentPlayerIdx++;
                    } else {
                        currentPlayerIdx = 0;
                    } ////////////////////////////////////////////////
                } else {
                    ui.printMsg("Not a legal move!!!\n");
                    // if (first) {
                    // if (currentPlayerIdx != 0) {
                    // currentPlayerIdx--;
                    // } else {
                    // currentPlayerIdx = 3;
                    // }
                    // // first = false;
                    // }
                    ui.promptActivePlayer();
                    return;
                }
            }

            else if (hand.beats(handsOnTable.get(handsOnTable.size() - 1)) || last) {
                handsOnTable.add(hand);
                playerList.get(playerIdx).removeCards(hand.getcards());
                if (currentPlayerIdx != 3) {
                    currentPlayerIdx++;
                } else {
                    currentPlayerIdx = 0;
                } ////////////////////////////////////////////////
            }

            else {
                ui.printMsg("Not a legal move!!!\n");
                ui.promptActivePlayer();
                return;
            }
        }
        String printStr = "";
        Hand lastHand = handsOnTable.get(handsOnTable.size() - 1);
        // ui.printMsg("{" + lastHand.getType() + "} ");
        if (lastHand != null) {
            for (int i = 0; i < lastHand.size(); i++) {
                printStr += "[" + lastHand.getCard(i).toString() + "] ";
            }
            this.ui.printMsg("{" + lastHand.getType() + "} " + printStr);
        }
        lastHand.getcards().print(true, false);
        System.out.println("");
    }

    /**
     * a method for checking if the game ends
     * 
     * @return a boolean to show whether the games has been ended or not
     */
    public boolean endOfGame() {
        for (int i = 0; i < 4; i++) {
            if (this.playerList.get(i).getNumOfCards() == 0) {
                this.ui.printMsg("Game ends\n");
                for (int j = 0; j < 4; j++) {
                    if (j != i) {
                        int n = this.playerList.get(i).getNumOfCards();
                        String s = "Player " + j + " has " + (13 - n) + " cards in hand.";
                        this.ui.printMsg(s);
                        this.ui.printMsg("\n");
                    } else {
                        this.ui.printMsg("Player " + i + " wins the game.\n");
                    }
                }
                return true;
            }
        }
        return false;
    }

    // public static methods
    /**
     * a method for starting a Big Two card game. It will creat a Big Two Object,
     * then creat a deck of cards and shuffle and finally start the game with the
     * deck of cards
     * 
     * @return a boolean to show whether the StraightFlush
     *         hand is valid or not.
     */
    public static void main(String[] args) {
        BigTwo bigTwo = new BigTwo();
        // (ii) create and shuffle a deck of cards
        BigTwoDeck bigtwodeck = new BigTwoDeck();
        bigtwodeck.initialize();
        // (iii) start the game with the deck of cards
        bigTwo.start(bigtwodeck);

    }

    /**
     * a method for checking if there is a valid hand from the list of cards of the
     * player
     * 
     * @return a Hand that is valid and when it respected hand type. if there is no
     *         valid hand, return null
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
        Single Single = new Single(player, cards);
        Pair Pair = new Pair(player, cards);
        Triple Triple = new Triple(player, cards);
        StraightFlush StraightFlush = new StraightFlush(player, cards);
        Quad Quad = new Quad(player, cards);
        FullHouse FullHouse = new FullHouse(player, cards);
        Flush Flush = new Flush(player, cards);
        Straight Straight = new Straight(player, cards);
        if (Single.isValid()) {
            return Single;
        } else if (Pair.isValid()) {
            return Pair;
        } else if (Triple.isValid()) {
            return Triple;
        } else if (StraightFlush.isValid()) {
            return StraightFlush;
        } else if (Quad.isValid()) {
            return Quad;
        } else if (FullHouse.isValid()) {
            return FullHouse;
        } else if (Flush.isValid()) {
            return Flush;
        } else if (Straight.isValid()) {
            return Straight;
        }
        return null;
    }
}
