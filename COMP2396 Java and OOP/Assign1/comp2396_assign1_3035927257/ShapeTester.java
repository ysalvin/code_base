
public class ShapeTester {
    public static void main(String[] args) {
        System.out.println("Shape Tester class");
        Shape shape = new Shape();
        shape.translate(5, 5);
        shape.rotate(99);
        System.out.println(shape.filled);
        System.out.println(shape.theta);
        System.out.println(shape.xc);
        System.out.println(shape.yc);
        System.out.println(shape.xLocal);
        System.out.println(shape.yLocal);
    }

}
