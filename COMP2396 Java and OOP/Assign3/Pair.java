/**
 * This class is used for modeling a Pair Hand.
 * This Class is A subclass of pair.
 */
public class Pair extends Hand {

    /**
     * Creates and returns an instance of the pair class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid pair hand.
     * 
     * @return a boolean to show whether the pair hand is valid or not.
     */

    public boolean isValid() {
        if (card.size() != 2) {
            return false;
        }
        if (card.getCard(0).getRank() == card.getCard(1).getRank()) {
            return true;
        }
        return false;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object ("Pair") in these classes
     *         modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead,
     */
    public String getType() {
        if (this.isValid()) {
            return "Pair";
        }
        return null;
    }
}
