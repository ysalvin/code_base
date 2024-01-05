/**
 * This class is used for modeling a FullHouse Hand.
 * This Class is A subclass of fullhouse.
 */
public class FullHouse extends Hand {

    /**
     * Creates and returns an instance of the Fullhouse class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid FullHouse hand.
     * 
     * @return a boolean to show whether the FullHouse hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() != 5) {
            return false;
        }
        int realrank = 0;
        int count[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        for (int i = 0; i < 5; i++) {
            realrank = card.getCard(i).getRank();
            if (realrank == 0) {
                realrank = 13;
            } else if (realrank == 1) {
                realrank = 14;
            }
            count[realrank]++;
        }
        int pair[] = { 0, 0 };
        for (int i = 1; i < 15; i++) {
            if (count[i] == 2) {
                pair[0]++;
            } else if (count[i] == 3) {
                pair[1]++;
            } else {
                return false;
            }
        }
        if (pair[0] == 1 && pair[1] == 1) {
            return true;
        }
        return false;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object ("FullHouse") in these
     *         classes modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead.
     * 
     */

    public String getType() {
        if (this.isValid()) {
            return "Fullhouse";
        }
        return null;
    }
}
