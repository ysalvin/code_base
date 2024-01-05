// import javax.print.FlavorException;

/**
 * This class is used for modeling a hand of cards..
 * This Class is subclass of the CardList class
 */
abstract class Hand extends CardList {

    // public constructor
    /**
     * Creates and returns an instance of the Hand
     * class with the specified player and list of cards.
     * 
     * @param player the player who plays this hand
     * @param cards  the list of cards that this hand contains
     */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        this.cards = cards;
    }

    // private instance variable
    private CardGamePlayer player;
    private CardList cards;

    // public methods

    /**
     * a method for getting the player of this hand
     * 
     * @return the player of this Hand.
     */
    public CardGamePlayer getPlayer() {
        return this.player;
    }

    /**
     * a method for getting the top card of this hand.
     * 
     * @parm printFront a boolean value specifying whether to print the face (true)
     *       or the black (false) of the cards
     * @param printIndex a boolean value specifying whether to print the index in
     *                   front of each card
     */
    public void print(boolean printFront, boolean printIndex) {
        if (cards.size() > 0) {
            for (int i = 0; i < this.cards.size(); i++) {
                String string = "";
                if (printIndex) {
                    string = i + " ";
                }
                if (printFront) {
                    string = string + "[" + this.cards.getCard(i) + "]";
                } else {
                    string = string + "[  ]";
                }
                if (i % 13 != 0) {
                    string = " " + string;
                }
                System.out.print(string);
                if (i % 13 == 12 || i == cards.size() - 1) {
                    System.out.println("");
                }
            }
        } else {
            System.out.println("[Empty]");
        }
    }

    /**
     * a method for getting the cards list of this hand
     * 
     * @return a list of cards
     */
    public CardList getcards() {
        return this.cards;
    }

    /**
     * a method for getting the top card of this hand
     * 
     * @return a the top card in the hand
     */
    public Card getTopCard() {
        // String type=this.getType();
        // // String type[] = { "Single", "Pair", "Triple", "Straight", "Flush", "Full
        // House", "Quad", "Stright Flush" };
        // if (type=="Single" || type=="Pair" || type=="Triple" || type=="Straight" ||
        // type=="Straight Flush"){
        // this.cards.sort();
        // return cards.getCard(0);
        // }
        // this.cards.sort();
        // return top(this);
        this.cards.sort();
        return cards.getCard(0);
    }

    // private boolean compareSuit(Hand h1, Hand h2) {
    // if (h2.getTopCard().getSuit() >= h1.getTopCard().getSuit()) {
    // return false;
    // } else {
    // return true;
    // }
    // }

    /**
     * a method for compare the rank of 2 cards when only considering their rank
     * 
     * @parm h1 the first hand of cards
     * @parm h2 the second hand of cards
     * @return boolean value showing wether the first hand has a higher ranking than
     *         the second hand when considering their rank only
     * 
     */
    private boolean compareRank(Hand h1, Hand h2) {
        Card h1r = h1.getTopCard();
        Card h2r = h2.getTopCard();
        int result = h1r.compareTo(h2r);
        if (result == 1) {
            return true;
        } else {
            return false;
        }
        // if (h1r == 0) {
        // h1r = 13;
        // } else if (h1r == 1) {
        // h1r = 14;
        // }
        // if (h2r == 0) {
        // h2r = 13;
        // } else if (h2r == 1) {
        // h2r = 14;
        // }
        // if (h2r > h1r) {
        // return false;
        // } else if (h2r == h1r) {
        // return compareSuit(h1, h2);
        // } else {
        // return true;
        // }
    }

    /**
     * a method for compare the rank of 2 cards when only considering their rank
     * 
     * @parm h1 the first hand of cards
     * @parm h2 the second hand of cards
     * @return a string object showing the type (Single, Pair, Triple, Straight,
     *         Flush, FullHouse, Quad, or StraightFlush) that hand belongs to, it
     *         none of the above the fullfilled, return null instead
     * 
     */
    public String findType(CardGamePlayer player, CardList cards) {
        String type;
        Single single = new Single(player, cards);
        Pair pair = new Pair(player, cards);
        Triple triple = new Triple(player, cards);
        Straight straight = new Straight(player, cards);
        Flush flush = new Flush(player, cards);
        FullHouse fullhouse = new FullHouse(player, cards);
        Quad quad = new Quad(player, cards);
        StraightFlush straightFlush = new StraightFlush(player, cards);
        if (single.getType() != null) {
            type = single.getType();
        } else if (pair.getType() != null) {
            type = pair.getType();
        } else if (triple.getType() != null) {
            type = triple.getType();
        } else if (straightFlush.getType() != null) {
            type = straightFlush.getType();
        } else if (quad.getType() != null) {
            type = quad.getType();
        } else if (fullhouse.getType() != null) {
            type = fullhouse.getType();
        } else if (flush.getType() != null) {
            type = flush.getType();
        } else if (straight.getType() != null) {
            type = straight.getType();
        } else {
            type = "Pass";
        }
        return type;
    }

    /**
     * a method for checking if this hand beats a specified hand.
     * 
     * @parm hand a hand that are goinf to compar
     * @return a boolean to show wehther this hand can beats the original hand or
     *         not
     * 
     */
    public boolean beats(Hand hand) {
        int h1 = -1;
        int h2 = -1;
        String type[] = { "Single", "Pair", "Triple", "Straight", "Flush", "Full House", "Quad", "Stright Flush" };
        if (this.getcards().size() != hand.getcards().size()) {
            return false;
        }
        for (int i = 0; i < 7; i++) {
            if (this.getType() == type[i]) {
                h1 = i;
            }
            if (hand.getType() == type[i]) {
                h2 = i;
            }
        }
        if (h2 < h1) {
            return true;
        } else if (h2 > h1) {
            return false;
        }

        if ((h1 == h2)) {
            return compareRank(this, hand);
        }
        return true;
    }

    // abstract methods
    /**
     * a abstract method for checking if this is a valid hand
     */
    public abstract boolean isValid();

    /**
     * a abstract method for returning a string specifying the type of this hand.
     */
    public abstract String getType();
}
