/**
 * This class is used for modeling a Triple Hand.
 * This Class is A subclass of single.
 */
public class Triple extends Hand {
    /**
     * Creates and returns an instance of the triple class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid triple hand.
     * 
     * @return a boolean to show whether the triple hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() != 3) {
            return false;
        }
        if (card.getCard(0).getRank() == card.getCard(1).getRank()
                && card.getCard(0).getRank() == card.getCard(2).getRank()) {
            return true;
        }
        return false;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object "Triple" in these classes
     *         modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead.
     */
    public String getType() {
        if (this.isValid()) {
            return "Triple";
        }
        return null;
    }
}
