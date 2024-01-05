public class RegularPolygonTester {

    public static void main(String arg[]) {
        System.out.println("RegularPolygon Tester class");

        RegularPolygon RegularPolygonTester = new RegularPolygon(4, 12);
        System.out.println(RegularPolygonTester.getNumOfSides());
        System.out.println(RegularPolygonTester.getRadius());
        double[] XLocal = { 5, 5, -5, -5 };
        RegularPolygonTester.setXLocal(XLocal);
        for (int i = 0; i < XLocal.length; i++) {
            System.out.println(XLocal[i]);
        }
        // double YLocal[] = new double[shape.getYLocal().length];
        System.out.println(RegularPolygonTester.getXLocal());

        double[] YLocal = { -5, 5, 5, -5 };
        RegularPolygonTester.setYLocal(YLocal);
        for (int i = 0; i < YLocal.length; i++) {
            System.out.println(YLocal[i]);
        }
        System.out.println(RegularPolygonTester.getYLocal());

        int[] canvasX = new int[4];
        canvasX = RegularPolygonTester.getX();
        for (int i = 0; i < canvasX.length; i++) {
            System.out.println(canvasX[i]);
        }
        int[] canvasY = new int[4];
        canvasY = RegularPolygonTester.getX();
        for (int i = 0; i < canvasY.length; i++) {
            System.out.println(canvasY[i]);
        }
        System.out.println(RegularPolygonTester.contains(6, 5));
        System.out.println(RegularPolygonTester.contains(6, 3));
        System.out.println(RegularPolygonTester.contains(63, 5));
        System.out.println(RegularPolygonTester.contains(6, 53));
        System.out.println(RegularPolygonTester.contains(60, 50));
        System.out.println("0+0" + RegularPolygonTester.contains(0, 0));
        System.out.println(RegularPolygonTester.contains(5, 5));
        System.out.println(RegularPolygonTester.contains(-5, -5));
        System.out.println(RegularPolygonTester.contains(5, 5));
        System.out.println(RegularPolygonTester.contains(5, -5));

    }

}