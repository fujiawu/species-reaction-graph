public class DSRgraph {

   private int S;  /* number of species */
   private int R;  /* number of reactions */
   private int E;  /* number of species-reaction relations */

   private Bag<Edge>[] sadj;  /* Edges for species   */
   private Bag<Edge>[] radj;  /* Edges for reactions */

   /* create an empty DSRgraph */
   public DSRgraph() {
       S = 0;
       R = 0;
       E = 0;
   }
    
   /* return the number of species */
   public int S() {
       return S;
   }
    
   /* return the number of reactions */
   public int R() {
       return R;
   }

   /* Add an edge to this DSRgraph */
   public void addEdge(Edge e) {
       int s = e.species();
       int r = e.reaction();
       sadj[s].add(e);
       radj[r].add(e);
   } 

}
