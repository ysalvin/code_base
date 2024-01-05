public class HandTester {
    private static CardGamePlayer p1 = new CardGamePlayer("1P");
    private static CardGamePlayer p2 = new CardGamePlayer("2P");

    public static void main(String[] args) {
        CardList cards1, cards2;

        cards1 = new CardList();
        cards1.addCard(new BigTwoCard(0, 0));
        cards1.addCard(new BigTwoCard(0, 1));
        cards1.addCard(new BigTwoCard(0, 2));
        cards1.addCard(new BigTwoCard(0, 3));
        cards1.addCard(new BigTwoCard(0, 4));
        Hand h1 = new Pair(p1, cards1);
        System.out.println("1P plays " + h1.findType(p1, cards1) + ":");
        cards1.print();
        h1.getcards().print();
        System.out.println();

        cards2 = new CardList();
        cards2.addCard(new BigTwoCard(2, 1));
        cards2.addCard(new BigTwoCard(3, 1));
        // cards2.addCard(new BigTwoCard(0, 2));
        // cards2.addCard(new BigTwoCard(0, 3));
        // cards2.addCard(new BigTwoCard(0, 4));
        Hand h2 = new Pair(p2, cards2);
        System.out.println("2P plays " + h2.findType(p2, cards2) + ":");
        cards2.print();

        if (h1.beats(h2)) {
            System.out.println("1P wins!");
        } else {
            System.out.println("1P does not beat 2P.");
        }
    }
}
