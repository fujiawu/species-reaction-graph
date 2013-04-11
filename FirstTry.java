import org.jgrapht.*;
import org.jgrapht.graph.*;
//import org.jgraphx.JGraph;
import javax.swing.*;

public class FirstTry extends JFrame {

   public FirstTry () {
     setTitle("My Empty Frame");
     setSize(300,200); // default size is 0,0
     setLocation(10,200); // default is 0,0 (top left corner)
   }
 
   public static void main(String[] args) {
      SimpleDirectedWeightedGraph g; 
      StdOut.println("hello world");
      JFrame f = new FirstTry();
      f.show();
   }

}
