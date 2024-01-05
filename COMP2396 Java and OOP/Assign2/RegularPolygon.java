/**
 * The RegularPolygon class is a sub-class of the Shape class, it is used to
 * create a regular n-sides polygons
 */

public class RegularPolygon extends Shape {

    // private instance variables
    private int numOfSides;
    private double radius;

    /**
     * a public constructors for constructing a regular n-side polygon with raduis r
     * 
     * @parm a int specifying the number of sides of the polygon
     * @parm a double value specifying the radius of sides of the polygon
     */

    // public constructors
    public RegularPolygon(int n, double r) {
        // (a) Set number of sides
        setNumOfSides(n);
        // (b) Radius
        setRadius(r);
        // (c) Set vertices of the polygon (n & r are known at this step)
        setVertices();
    }

    /**
     * a public constructors for constructing a regular n-side polygon with raduis 1
     * 
     * @parm a int specifying the number of sides of the polygon
     */

    public RegularPolygon(int n) { // set radius as 1.0
        // (a) Set number of sides
        setNumOfSides(n);
        // (b) Radius
        setRadius(1.0);
        // (c) Set vertices of the polygon (n & r are known at this step)
        setVertices();
    }

    /**
     * a public constructors for constructing a regular 3-side polygon(triangle)
     * with raduis 1.0
     * 
     */

    public RegularPolygon() {
        // (a) Set number of sides
        setNumOfSides(3);
        // (b) Radius
        setRadius(1.0);
        // (c) Set vertices of the polygon (n & r are known at this step)
        setVertices();
    }

    // public/private methods

    /**
     * a public method for getting the number of slides of the n-side polygon
     * 
     * @return a int value specifying the number of silides of the n-sides polygon
     */
    public int getNumOfSides() {
        return this.numOfSides;
    }

    /**
     * a public method for getting the radius of the n-side polygon
     * 
     * @return a int value specifying the radius of the n-sides polygon
     */
    public double getRadius() {
        return this.radius;
    }

    /**
     * a public method for setting the number of slides of the n-side polygon, if n
     * is less than 3, n is setted to 3 instead
     * 
     * @parm a int value specifying the number of slides of the n-sides polygon
     */
    public void setNumOfSides(int n) {
        if (n < 3) {
            n = 3;
        }
        this.numOfSides = n;
        setVertices();
    }

    /**
     * a public method for setting the radius of the n-side polygon
     * 
     * @parm a int value specifying the radius of the n-sides polygon
     */
    public void setRadius(double r) {
        if (r < 0) {
            r = 0;
        }
        this.radius = r;
        setVertices();
    }

    private void setVertices() {
        double r = this.radius;
        double n = (2 * Math.PI) / this.numOfSides;
        double a = 0;
        if (this.numOfSides % 2 == 0) {
            a = (Math.PI / this.numOfSides);
        }
        double[] x_local = new double[this.numOfSides];
        double[] y_local = new double[this.numOfSides];
        for (int i = 0; i < this.numOfSides; i++) {
            x_local[i] = r * Math.cos(a - i * n);
            y_local[i] = r * Math.sin(a - i * n);
        }
        setXLocal(x_local);
        setYLocal(y_local);
    }

    /**
     * a public method for checking whether a point gernerated by mosue-click is
     * inside a regualr n-sides polygon or not in the canvas coordinates system
     * 
     * @parm a double value specifying the x-coordinates of the point gernerated by
     *       mosue-click in the canvas coordinates system
     * @parm a double value specifying the y-coordinates of the point gernerated by
     *       mosue-click in the canvas coordinates system
     * @return a boolean value specifying whether the point is inside a n-side
     *         polygon or not
     * 
     */
    public boolean contains(double x, double y) {
        double x0 = x;
        double y0 = y;
        x = (x0 - getXc()) * Math.cos(-this.getTheta()) - (y0 - getYc()) * Math.sin(-this.getTheta());
        y = (x0 - getXc()) * Math.sin(-this.getTheta()) + (y0 - getYc()) * Math.cos(-this.getTheta());
        x0 = x;
        y0 = y;
        double[] xLocal = getXLocal();
        double line_x = xLocal[this.numOfSides / 2];
        double n = (2 * Math.PI) / this.numOfSides;
        for (int i = 0; i < this.numOfSides; i++) {
            x = x0 * Math.cos(i * n) - y0 * Math.sin(i * n);
            if (x < line_x) {
                return false;
            }
            // x = x0 * Math.cos((2 * Math.PI) / this.numOfSides) - y0 * Math.sin((2 *
            // Math.PI) / this.numOfSides);
            // y = x0 * Math.sin((2 * Math.PI) / this.numOfSides) + y0 * Math.cos((2 *
            // Math.PI) / this.numOfSides);
        }
        return true;
    }

}