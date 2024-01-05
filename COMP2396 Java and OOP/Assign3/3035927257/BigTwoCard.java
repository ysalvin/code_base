/**
 * This class is used for modelling a card used in a Big Two card game.
 * This Class is a subclass of the Card class
 */
public class BigTwoCard extends Card {

    // public constructor
    /**
     * a constructor for building a card with the specified suit and rank. suit is
     * an integer between 0 and 3, and rank is an integer between 0 and 12.
     * 
     * @param suit a integer resperenting the suit of a card
     * @param rank a integer resperenting the rank of a card
     */

    public BigTwoCard(int suit, int rank) {
        super(suit, rank);
        // this.suit = suit;
        // this.rank = rank;

    }

    /**
     * a method for comparing the order of this card with the specified card.
     * Returns a negative integer, zero, or a positive integer when this card is
     * less than, equal to, or greater than the specified card.
     * 
     * @parm card the card that are going to compare
     * @return Returns a negative integer, zero, or a positive integer when this
     *         card is less than, equal to, or greater than the given card.
     */
    public int compareTo(Card card) { // this compare with Card card
        if (this.rank == card.getRank() && this.suit == card.getSuit()) {
            return 0;
        } else if (this.rank == ((Card) card).getRank()) { // same rank, different suit
            if (this.suit > card.getSuit()) {
                return 1;
            } else {
                return -1;
            }
        } else { // same suit, different rank
            int thisrank = this.rank;
            if (thisrank == 0) {
                thisrank = 13;
            } else if (thisrank == 1) {
                thisrank = 14;
            }
            int cardrank = card.getRank();
            if (cardrank == 0) {
                cardrank = 13;
            } else if (cardrank == 1) {
                cardrank = 14;
            }
            // System.out.println("test");
            // System.out.println(thisrank);
            // System.out.println(this.suit);
            // System.out.println(cardrank);
            // System.out.println(card.getSuit());
            if (thisrank > cardrank) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
