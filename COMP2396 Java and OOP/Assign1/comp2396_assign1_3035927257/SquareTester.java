public class SquareTester extends ShapeTester {
    public static void main(String[] args) {
        System.out.println("Shape Tester class");
        Square square = new Square();
        square.translate(5, 5);
        square.rotate(99);
        square.setVertices(5);
        square.getX();
        square.getY();
        System.out.println(square.filled);
        System.out.println(square.theta);
        System.out.println(square.xc);
        System.out.println(square.yc);
        System.out.println("xLocal");
        for (double i : square.xLocal) {
            System.out.println(i);
        }
        System.out.println(square.xLocal);
        System.out.println("yLocal");
        for (double i : square.yLocal) {
            System.out.println(i);
        }
        System.out.println(square.yLocal);
    }

}