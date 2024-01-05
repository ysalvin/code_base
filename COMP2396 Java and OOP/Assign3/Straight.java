/**
 * This class is used for modeling a Straight Hand.
 * This Class is A subclass of single.
 */
public class Straight extends Hand {

    /**
     * Creates and returns an instance of the Straight class.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.card = cards;
    }

    private CardList card;

    /**
     * a method for checking if this is a valid Straight hand.
     * 
     * @return a boolean to show whether the Straight hand is valid or not.
     */
    public boolean isValid() {
        if (card.size() != 5) {
            return false;
        }
        card.sort();
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
        for (int i = 2; i < 15 - 5; i++) {
            if (count[i] == 1 && count[i + 1] == 1 && count[i + 2] == 1 && count[i + 3] == 1 && count[i + 4] == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Give the name of the class.
     * 
     * @return the name of the class as a String object "Straight" in these classes
     *         modelling
     *         legal hands in a Big Two card game. If the hand is invalid, return
     *         null instead.
     * 
     */
    public String getType() {
        if (this.isValid()) {
            return "Straight";
        }
        return null;
    }
}
