public class BigTwoDeckTester {
    public static void main(String[] args) {
        BigTwoDeck deck = new BigTwoDeck();
        System.out.println(deck.size());
        System.out.println(deck.getCard(0).getClass());
        System.out.println(deck.getCard(39).compareTo(deck.getCard(40)));
        deck.print();
    }
}