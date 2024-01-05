/**
 * This class is used for modelling a deck of cards used in a Big Two card game.
 * This Class is a subclass of the Deck class
 */
public class BigTwoDeck extends Deck {

	// overriding method
	/**
	 * a method for initializing a deck of Big Two cards. It should remove all cards
	 * from the deck, create 52 Big Two cards and add them to the deck.
	 *
	 */
	public void initialize() {

		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard card = new BigTwoCard(i, j);
				addCard(card);
			}
		}
		shuffle();
	}
}
