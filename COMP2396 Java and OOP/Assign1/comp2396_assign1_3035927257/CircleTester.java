public class CircleTester extends ShapeTester {
    public static void main(String[] args) {
        System.out.println("Shape Tester class");
        Circle circle = new Circle();
        circle.translate(5, 5);
        circle.rotate(99);
        circle.setVertices(5);
        circle.getX();
        circle.getY();
        System.out.println(circle.filled);
        System.out.println(circle.theta);
        System.out.println(circle.xc);
        System.out.println(circle.yc);
        System.out.println("xLocal");
        for (double i : circle.xLocal) {
            System.out.println(i);
        }
        System.out.println(circle.xLocal);
        System.out.println("yLocal");
        for (double i : circle.yLocal) {
            System.out.println(i);
        }
        System.out.println(circle.yLocal);
    }
}