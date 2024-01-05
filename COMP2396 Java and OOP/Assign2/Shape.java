import java.awt.Color;

/**
 * The Shape class is used to model general shape
 */

public class Shape {

    // private instance variables
    private Color color;
    private boolean filled;
    private double theta;
    private double xc;
    private double yc;
    private double[] xLocal;
    private double[] yLocal;

    // public method

    /**
     * a method for getting the color of the shape.
     * 
     * @return color of the sahpe
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * a method for getting the fill-type of the shape.
     * 
     * @return a boolean value specifying whether the shape is filled or not filled
     */
    public boolean getFilled() {
        return this.filled;
    }

    /**
     * a method for getting the orientation (in radians) of the shape in the
     * canvas coordinate system
     * 
     * @return a double value, showing the orientation (in radians)
     */
    public double getTheta() {
        return this.theta;
    }

    /**
     * a method for getting the x-coordinate of the center of the shape in the
     * canvas coordinate system.
     * 
     * @return a double value specifying the x-coordinate of the center of the shape
     *         in the canvas coordinate system.
     */
    public double getXc() {
        return this.xc;
    }

    /**
     * a method for getting the y-coordinate of the center of the shape in the
     * canvas coordinate system.
     * 
     * @return a double value specifying the y-coordinate of the center of the shape
     *         in the canvas coordinate system.
     */
    public double getYc() {
        return this.yc;
    }

    /**
     * a method for getting the x-coordinates of the vertices (in counter-clockwise
     * order) of the shape in its local coordinate system.
     * 
     * @return an double array, contaiing the the x-coordinates of the vertices (in
     *         counter-clockwise order) of the shape in its local coordinate system.
     */
    public double[] getXLocal() {
        return this.xLocal;
    }

    /**
     * a method for getting the y-coordinates of the vertices (in
     * counter-clockwise order) of the shape in its local coordinate system.
     * 
     * @return an double array, contaiing the the y-coordinates of the vertices (in
     *         counter-clockwise order) of the shape in its local coordinate system.
     * 
     */
    public double[] getYLocal() {
        return this.yLocal;
    }

    /**
     * a method for setting the color of the shape.
     * 
     * @parm color of the sahpe
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * a method for setting the fill-type of the shape.
     * 
     * @parm a boolean value specifying whether the shape is filled or not filled
     */
    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    /**
     * a method for setting the orientation (in radians) of the shape in the
     * canvas coordinate system
     * 
     * @parm a double value, showing the orientation (in radians)
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * a method for setting the x-coordinate of the center of the shape in the
     * canvas coordinate system.
     * 
     * @parm a double value specifying the x-coordinate of the center of the shape
     *       in the canvas coordinate system.
     */
    void setXc(double xc) {
        this.xc = xc;
    }

    /**
     * a method for getting the y-coordinate of the center of the shape in the
     * canvas coordinate system.
     * 
     * @parm a double value specifying the y-coordinate of the center of the shape
     *       in the canvas coordinate system.
     */
    public void setYc(double yc) {
        this.yc = yc;
    }

    /**
     * a method for setting the x-coordinates of the vertices (in counter-clockwise
     * order) of the shape in its local coordinate system.
     * 
     * @parm an double array, contaiing the the x-coordinates of the vertices (in
     *       counter-clockwise order) of the shape in its local coordinate system.
     */
    public void setXLocal(double[] xLocal) {
        this.xLocal = xLocal;
    }

    /**
     * a method for setting the x-coordinates of the vertices (in counter-clockwise
     * order) of the shape in its local coordinate system.
     * 
     * @parm an double array, contaiing the the x-coordinates of the vertices (in
     *       counter-clockwise order) of the shape in its local coordinate system.
     */
    public void setYLocal(double[] yLocal) {
        this.yLocal = yLocal;
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
        setXc(this.xc + dx);
        setYc(this.yc + dy);
        // this.xc += dx;
        // this.yc += dy;
    }

    /**
     * a public void method for rotating the shape
     * 
     * @param dt angle (in radian) the value is added to theta
     */

    public void rotate(double dt) {
        // this.theta += dt;
        setTheta(this.theta + dt);
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
                    .round(this.xLocal[i] * Math.sin(this.theta) + this.yLocal[i] * Math.cos(this.theta) + this.yc);
        }
        return y_vertices_canvas;
    }
}