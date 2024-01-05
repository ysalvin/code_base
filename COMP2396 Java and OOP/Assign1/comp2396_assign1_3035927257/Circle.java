/**
 * The Circle class is used to model circles
 */

public class Circle extends Shape {

    /**
     * a method for setting the local coordinates of the upper left and lower right
     * vertices of an axis-aligned bounding box of a standard circle
     * The value is stored in the xLocal and yLocal array
     * 
     * @param d radius of the cicle
     */

    public void setVertices(double d) {
        this.xLocal = new double[] { -d, d };
        this.yLocal = new double[] { -d, d };
    }

    /**
     * a public method for converting the x-coordinate of the upper left and lower
     * right vertices from the local coordinate to canvas coordinate system
     * 
     * @return return a int array, storing the x-coordinate of the upper left and
     *         lower
     *         right vertices (in counter-clockwise order) of the shape in canvas
     *         coordinate
     */

    public int[] getX() {
        int[] x_array = new int[2];
        x_array[0] = (int) Math.round(xLocal[0] + xc);
        x_array[1] = (int) Math.round(xLocal[1] + xc);
        return x_array;
    }

    /**
     * a public method for converting the y-coordinate of the upper left and lower
     * right vertices from the local coordinate to canvas coordinate system
     * 
     * @return return a int array, storing the y-coordinate of the upper left and
     *         lower
     *         right vertices (in counter-clockwise order) of the shape in canvas
     *         coordinate
     */

    public int[] getY() {
        int[] y_array = new int[2];
        y_array[0] = (int) Math.round(xLocal[0] + yc);
        y_array[1] = (int) Math.round(xLocal[1] + yc);
        return y_array;
    }
}