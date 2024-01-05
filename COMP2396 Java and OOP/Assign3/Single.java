/**
 * This class is used for modeling a Single Hand.
 * This Class is A subclass of single.
 */

public class Single extends Hand {

    /**
     * Creates and returns an instance of the Single class.
     * 
     * public Single(CardGamePlayer player, CardList cards) {
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    // private instance variable
    private CardList card;

    /**
     * a method for checking if this is a valid hand.
     * 
     * @return a boolean to show whether the hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object in these classes modelling
     *         legal hands in a Big Two card game.If the hand is invalid, return
     *         null instead,
     */
    public String getType() {
        if (this.isValid()) {
            return "Single";
        }
        return null;
    }

}
