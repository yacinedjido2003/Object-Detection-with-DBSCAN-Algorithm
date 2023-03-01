import java.util.*;

public class NearestNeighbors {

    private ArrayList<Point3D> pointsCloud ;
    private ArrayList<Point3D> nearPoints ;

    public NearestNeighbors(ArrayList<Point3D> points) {
        pointsCloud=points ; }

    public ArrayList<Point3D> getNeighbors() {
        return nearPoints ; } // getter of the neighbors of a tridimensionnal Point
    
    public ArrayList<Point3D> rangeQuery (Point3D point, double eps) {

        nearPoints = new ArrayList<Point3D>() ;

        for (Point3D otherPoint : pointsCloud) {
            if (point.distance(otherPoint)<=eps) {
                nearPoints.add(otherPoint) ; }
        }

        return nearPoints ; }     
}
