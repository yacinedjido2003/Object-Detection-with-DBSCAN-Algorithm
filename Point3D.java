
// importing the Math library for the mathematical functions we'll need to do the computations required in this class
import java.lang.Math;

public class Point3D {

    private double x;
    private double y;
    private double z;
    private int label; //we will labelize the points with positive integer numbers

    public Point3D (double x, double y, double z) {
        // constructor of a Point in the tridimensionnal plan with initialized undefined label
        this.x=x;
        this.y=y;
        this.z=z;
        this.label=-1 ; } //the value -1 specifies that the label is undefined 

    public Point3D (double x, double y, double z, int label) {
        // constructor of a Point in the tridimensionnal plan with specified label
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        if (label>-1)  this.setLabel(label) ; }

    public double distance(Point3D other) {

        //computes and returns the euclidian distance between two points of the plan

        double deltaX = this.x-other.x ;
        double deltaY = this.y-other.y ;
        double deltaZ = this.z-other.z ;

        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)) ; }

    public void setLabel (int label) {
        this.label=label ; } // setter for the cluster label of the 3D Point

    public void setX (double x) {
        this.x=x ; } // setter for the x field of the 3D Point

    public void setY (double y) {
        this.y=y ; } // setter for the y field of the 3D Point

    public void setZ (double z) {
        this.z=z ; } // setter for the z field of the 3D Point

    public int getLabel() {
        return this.label ; } // getter for the cluster label of the 3D Point

    public double getX() {
        return x ; } // getter for the x field of the 3D Point

    public double getY() {
        return y ; } // getter for the y field of the 3D Point

    public double getZ() {
        return z ; } // getter for the z field of the 3D Point
    
    public String toString() {
        // displays (returns) the coordinates of the 3D Point
        return String.valueOf(x)+','+String.valueOf(y)+','+String.valueOf(z) ; }
}
