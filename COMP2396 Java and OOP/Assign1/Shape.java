import java.awt.Color;

/**
 * This class is the superclass of Circle, Square and Triangle class. The shape
 * class is used ti model the general shape on the GUI
 */

public class Shape {

    public Color color;
    public Boolean filled;
    public double theta;
    public double xc;
    public double yc;
    public double[] xLocal;
    public double[] yLocal;

    /**
     * a method for setting the local coordinates of the vertices of a shape. This
     * is a dummy method as it is supposed to be overridden in the subclasses.
     * 
     * @param d (dummy class)
     * @return (dummy class)
     */

    public void setVertices(double d) {
        // dummy method
    }

    /**
     * a public void method for translating the center of the shape by dx and dy,
     * along the canvas coordinate system
     * 
     * @param dx the x-direction distance that the center of the shape need to
     *           translte from the center of canvas coordinate system
     * @param dy the y-direction distance that the center of the shape need to
     *           translte from the center of canvas coordinate system
     */

    public void translate(double dx, double dy) {
        this.xc += dx;
        this.yc += dy;
    }

    /**
     * a public void method for rotating the shape
     * 
     * @param dt angle (in radian) the value is added to theta
     */

    public void rotate(double dt) {
        this.theta += dt;
    }

    /**
     * a public method for converting the x-coordinate of the vertices from the
     * x-coordinate of the vertices in local coordinate system to canvas coordinate
     * system
     * 
     * @return return a int array, storing the x-coordinate of vertices (in
     *         counter-clockwise order)
     *         of the shape in canvas coordinate
     */

    public int[] getX() {
        int[] x_vertices_canvas = new int[this.xLocal.length];
        for (int i = 0; i < this.xLocal.length; i++) {
            x_vertices_canvas[i] = (int) Math.round(
                    this.xLocal[i] * Math.cos(this.theta) - this.yLocal[i] * Math.sin(this.theta) + this.xc);
        }
        return x_vertices_canvas;
    }

    /**
     * a public method for converting the y-coordinate of the vertices from the
     * y-coordinate of the vertices in local coordinate system to canvas coordinate
     * system
     * 
     * @return return a int array, storing the y-coordinate of vertices (in
     *         counter-clockwise order)
     *         of the shape in canvas coordinate
     */

    public int[] getY() {
        int[] y_vertices_canvas = new int[this.yLocal.length];
        for (int i = 0; i < this.yLocal.length; i++) {
            y_vertices_canvas[i] = (int) Math
                    .round(this.yLocal[i] * Math.sin(this.theta) + this.yLocal[i] * Math.cos(this.theta) + this.yc);
        }
        return y_vertices_canvas;

    }
}