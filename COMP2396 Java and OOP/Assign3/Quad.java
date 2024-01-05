/**
 * This class is used for modeling a Quad Hand.
 * This Class is A subclass of quad.
 */
public class Quad extends Hand {

    /**
     * Creates and returns an instance of the Quad class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid quad hand.
     * 
     * @return a boolean to show whether the quad hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() != 5) {
            return false;
        }
        int realrank = 0;
        int count[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 5; i++) {
            count[realrank]++;
        }
        for (int i = 0; i < 13; i++) {
            if (count[i] == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object "quad" in these classes
     *         modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead.
     * 
     */

    public String getType() {
        if (this.isValid()) {
            return "Quad";
        }
        return null;
    }
}
