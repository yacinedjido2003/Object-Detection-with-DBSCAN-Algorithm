/* importing the java libraries we'll use in the DBScan algorithm */
import java.util.*;
import java.io.*;

public class DBScan {

    private double eps ;
    private double minPts ;
    private int clusters ;
    private double R ;
    private double G ;
    private double B ;
    ArrayList<Point3D> point3ds ;
    
    private class NodeStack implements Stack {

        // reference to the head node
        protected Node top;
        // number of elements in the stack
        protected int size;

        /**
         * Constructor fo NodeStack class.
         * @return an empty stack
         */
        public NodeStack() {
            top  = null;
            size = 0 ; }
    
        public int size() {
            return size ; } /* size */
    
        public boolean isEmpty() {
            if( top == null )
                return true;
            return false ; } /* isEmpty */
    
        public Object top() throws EmptyStackException {
            if( isEmpty() )
                throw new EmptyStackException ( "Stack is empty." );
    
            return top.getElement() ; } /* top */
    
        public void push( Object elem ) {
            // create and link-in a new node
            Node v = new Node ( elem, top );
    
            top = v;
            size++ ; } /* push */
    
    
        public Object pop() throws EmptyStackException {
            if( isEmpty() )
                throw new EmptyStackException ( "Stack is empty." );
    
            Object temp = top.getElement();
    
            // link-out the former top node
            top = top.getNext();
            size--;
    
            return temp ; } /* pop */ } 
        
        public DBScan (String file, double eps, double minPts) throws FileNotFoundException {
            // initializes the parameters that will be used bt the algorithm and the list of Points to analyze
            try { this.point3ds=DBScan.read(file) ;
                  this.setEps(eps) ;
                  this.setMinPts(minPts) ;
                  clusters=0 ; } // initializes the # of clusters at its default value (0)

            catch (FileNotFoundException exception) {} }

        public void setEps(double eps) {
            this.eps=eps ; } // setter for the eps parameter of the DBScan algorithm 
        
        public void setMinPts(double minPts) {
            this.minPts=minPts ; } // setter for the minPts parameter of the DBScan algorithm

        public void setR(double R) {
            if (0<=R && R<=1) { this.R=R ; } } // setter for the R parameter of the DBScan algorithm

        public void setG(double G) {
            if (0<=G && G<=1) { this.G=G ; } } // setter for the G parameter of the DBScan algorithm

        public void setB(double B) {
            if (0<=B && R<=1) { this.B=B ; } } // setter for the B parameter of the DBScan algorithm
    
        public double getEps() {
            return eps ; } // getter for the eps parameter of the DBScan algorithm

        public double getMinPts() {
            return minPts ; } // getter for the MinPts parameter of the DBScan algorithm
        
        public int getNumberOfClusters() {
            return clusters ; } // getter for the number of clusters registered in the cloud of dots scanned by the LiDAR

        public double getR() {
            return R ; } // getter for the R parameter of the DBScan algorithm
            
        public double getG() {
            return G ; } // getter for the G parameter of the DBScan algorithm

        public double getB() {
            return B ; } // getter for the B parameter of the DBScan algorithm

        public static double [] RGBValuesGenerator(Point3D point) {
            
            /* R, G & B values attributed accoridng to the following formula (1/label) so that each point 
            of the same cluster will have same RGB values so the same color on the visualization tool */

            double [] RGBColors = new double [3] ;
            
            for (int i=0; i<RGBColors.length; i++) {
                if (point.getLabel()>0) {
                    RGBColors[i]=1.0/point.getLabel() ; }
                 else { RGBColors[i]=0 ; //noise
                } 
            }
                
            return RGBColors ;
        }
                        

        public void findClusters() {

            /* DBScan algorithm */ 
            for (Point3D Point : point3ds) {

                if (Point.getLabel()!=-1) {
                  continue ;
                }

                /* On parcourt le nuage de points et on évalue le voisinage de chaque point afin de détermnier
                si ce dernier est assez dense pour le considérer comme composant d'un objet */ 

                NearestNeighbors Sequence = new NearestNeighbors(point3ds) ;
                ArrayList<Point3D> neighbors = Sequence.rangeQuery (Point, eps) ;

                if (neighbors.size()<minPts) {
                    Point.setLabel(0) ;  // the value 0 defines the Noise label
                    continue ;
                }
                
                clusters++ ; /* On a trouvé un point appartenant à un objet : on incremente donc le compteur groupes */ 
                Point.setLabel(clusters) ; /* On labellise le point au groupe correspondant */ 

               NodeStack stack = new NodeStack() ;  /* On empile tous les elements du voisinage */ 
               for (Point3D neighbor : neighbors) {
               stack.push(neighbor) ; }

               Point3D neighborPoint ;

               /* On va dans cette boucle ajouter au cluster respectivement les voisins des voisins du point 
                initial jusqu'à ce que tous les points d'un meme objet soient entèrement regroupés */ 

                while (! stack.isEmpty()) {

                neighborPoint= (Point3D) stack.pop() ;                          

                if (neighborPoint.getLabel()==0) { //tant que le point est libre on l'ajoute au cluster
                    neighborPoint.setLabel(clusters) ; }
                
                if (neighborPoint.getLabel()!=-1 && neighborPoint.getLabel()!=0) {
                    continue ; }
                
                neighborPoint.setLabel(clusters);

                NearestNeighbors N = new NearestNeighbors(point3ds) ;
                ArrayList<Point3D> neighborhood = N.rangeQuery (neighborPoint, eps) ;

                if (neighborhood.size()>=minPts) { 
                     for (Point3D near : neighborhood) {
                        stack.push(near) ; }
                    } 
                }
            }
        }

        public ArrayList<Point3D> getPoints() {
            return point3ds ; // getter for the Array of points analyzed by the DBScan algorithm
        }

        public static ArrayList<Point3D> read (String filename) throws FileNotFoundException {

            /* On initialise une liste pour le nuage de points et un buffer pour lire le fichier CSV */ 
            ArrayList<Point3D> cloud = new ArrayList<Point3D>() ;
            BufferedReader br = new BufferedReader(new FileReader(filename)) ;

            String line ;
            int LineNumber=1 ;

            try { while ((line=br.readLine())!=null) {

                //On ignore la ligne contenant "x,y,z" (1ère ligne)
                if (LineNumber==1) {
                    LineNumber=LineNumber+1;
                }

                 /* On lit le fichier ligne par ligne et on recupere les coordonnées x, y et z de chaque point */
                 else  {
                    String [] pointln = line.split(",") ;
        
                double xO = Double.parseDouble(pointln[0]) ;
                double yO = Double.parseDouble(pointln[1]) ;
                double zO = Double.parseDouble(pointln[2]) ;
                
                 /* On crée le point 3D à partir des coordonnées recupérées et on l'ajoute dans 
                    la liste représentant notre nuage de points */ 
                Point3D point = new Point3D(xO, yO, zO) ;
                cloud.add(point) ; }
                }
                
                br.close() ; }

            catch (IOException error) {error.printStackTrace();} 
            
            return cloud ; } /* On retourne le nuage de points sous forme de liste */ 
        
        public void save (String filename) {

            String READEME = "Author : Yacine Ibrahim Djido (Student number : 300263186)" ;

            //En-tête du fichier de sorite
            String HEADER = "x,y,z,C,R,G,B" ;

            //Délimiteurs qui vont être dans le fichier CSV
            String DELIMITER = ",";
            String SEPARATOR = "\n";

            FileWriter file;

            try {
              //Nouveau fichier d'écriture
              file = new FileWriter(filename.replaceFirst(".csv", "_")+"clusters_"+getEps()+"_"+getMinPts()+"_"+getNumberOfClusters()+".csv");
              //Ajouter l'identification au fichier
              file.append(READEME);
              //Nouvelle ligne
              file.append(SEPARATOR);
              //Ajouter l'en-tête
              file.append(HEADER);
              //Ajouter une nouvelle ligne après l'en-tête
              file.append(SEPARATOR);

              /* On crée un itérateur sur la liste contenant notre nuage de points et pour chaque point, on écrit 
              les valeurs de x, y, z, C, R, G et B correspondantes dans le fichier CSV */
              Iterator<Point3D>  it = point3ds.iterator();

              while(it.hasNext()) {

              Point3D point = (Point3D) it.next() ;

              double [] RGB = DBScan.RGBValuesGenerator(point) ; //génère aléatoirement des valeurs RGB en fonction du label du point

              file.append(String.valueOf(point.getX()));
              file.append(DELIMITER);
              file.append(String.valueOf(point.getY()));
              file.append(DELIMITER);
              file.append(String.valueOf(point.getZ()));
              file.append(DELIMITER);
              file.append(String.valueOf(point.getLabel()));
              file.append(DELIMITER);
              file.append(String.valueOf(RGB[0]));
              file.append(DELIMITER);
              file.append(String.valueOf(RGB[1]));
              file.append(DELIMITER);
              file.append(String.valueOf(RGB[2]));
              file.append(SEPARATOR) ; } 

              file.close() ; } //on ferme le fichier à la fin de toutes les opérations d'écriture

              catch (Exception e) { e.printStackTrace() ; }
        }
}
    
