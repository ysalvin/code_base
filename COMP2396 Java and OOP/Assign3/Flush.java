/**
 * This class is used for modeling a Flush Hand.
 * This Class is A subclass of flush.
 */
public class Flush extends Hand {

    /**
     * Creates and returns an instance of the Flush class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid Flush hand.
     * 
     * @return a boolean to show whether the Flush hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() != 5) {
            return false;
        }
        int suit = card.getCard(0).getSuit();
        for (int i = 1; i < 5; i++) {
            if (card.getCard(i).getSuit() != suit) {
                return false;
            }
        }
        return true;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object "Flush" in these classes
     *         modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead.
     * 
     */
    public String getType() {
        if (this.isValid()) {
            return "Flush";
        }
        return null;
    }
}
