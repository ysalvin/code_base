public class ShapeTester {
    public static void main(String[] args) {
        System.out.println("Shape Tester class");
        Shape shape = new Shape();
        shape.rotate(99);
        System.out.println("colour" + shape.getColor());

        System.out.println("filled" + shape.getFilled());
        shape.setFilled(true);
        System.out.println("filled" + shape.getFilled());

        System.out.println("theta" + shape.getTheta());

        shape.setXc(144);
        shape.setYc(144);

        System.out.println("Xc" + shape.getXc());
        System.out.println("Yc" + shape.getYc());

        shape.translate(5, 5);
        System.out.println("Xc" + shape.getXc());
        System.out.println("Yc" + shape.getYc());

        // double XLocal[] = new double[shape.getXLocal().length];

        double[] XLocal = { 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 0.0 };
        shape.setXLocal(XLocal);
        for (int i = 0; i < XLocal.length; i++) {
            System.out.println(XLocal[i]);
        }
        // double YLocal[] = new double[shape.getYLocal().length];

        double[] YLocal = { 2, 3, 4, 5, 6, 7, 0 };
        shape.setYLocal(YLocal);
        for (int i = 0; i < YLocal.length; i++) {
            System.out.println(YLocal[i]);
        }
        System.out.println(shape.getYLocal());

        int[] canvasX = new int[7];
        canvasX = shape.getX();
        for (int i = 0; i < canvasX.length; i++) {
            System.out.println(canvasX[i]);
        }
        int[] canvasY = new int[7];
        canvasY = shape.getX();
        for (int i = 0; i < canvasY.length; i++) {
            System.out.println(canvasY[i]);
        }

    }
}