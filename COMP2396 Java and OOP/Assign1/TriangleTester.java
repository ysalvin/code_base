public class TriangleTester extends ShapeTester {
    public static void main(String[] args) {
        System.out.println("Shape Tester class");
        Triangle triangle = new Triangle();
        triangle.translate(5, 5);
        triangle.rotate(99);
        triangle.setVertices(5);
        triangle.getX();
        triangle.getY();
        System.out.println(triangle.filled);
        System.out.println(triangle.theta);
        System.out.println(triangle.xc);
        System.out.println(triangle.yc);
        System.out.println("xLocal");
        for (double i : triangle.xLocal) {
            System.out.println(i);
        }
        System.out.println(triangle.xLocal);
        System.out.println("yLocal");
        for (double i : triangle.yLocal) {
            System.out.println(i);
        }
        System.out.println(triangle.yLocal);
    }
}