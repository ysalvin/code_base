public class Shape(){
    //All instane variable
    Color color;
    Boolean filled;
    double theta;
    double xc;
    double yc;
    double[] xLocal;
    double[] yLocal;

    public void setVertices(double d) {
        // dummy method
    }

    public void translate(double dx, double dy) {
        this.xc += dx;
        this.yc += dy;
    }

    public void rotate(double dt) {
        this.theta += dt;
    }

    public int[] getX() {
        int x_vertices_canvas[];
        for (int i = 0; i < this.xLocal.length(); i++) {
            x_vertices_canvas[i] = (int) round(
                    this.xLocal[i] * Math.cos(this.theta) - this.yLocal[i] * Math.sin(this.theta) + this.xc);
        }
        return x_vertices_canvas;
    }

    public int[] getY()
        int[] y_vertices_canvas;
        for (int i=0; i < this.yLocal.length(); i++ ){
            y_vertices_canvas[i] = round ( this.xLocal[i] * Math.sin(this.theta) + this.yLocal[i] * Math.cos(this.theta) + this.yc) ;
        }
        return y_vertices_canvas;
    
}