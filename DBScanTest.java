import java.io.FileNotFoundException;

public class DBScanTest {
    public static void main(String[] args) throws FileNotFoundException {
        DBScan test = new DBScan("Point_Cloud_2.csv",1, 8) ;
        test.findClusters() ;
        test.save("Point_Cloud_2.csv") ;
    }
}
