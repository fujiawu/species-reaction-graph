/***********************************************************
 *
 *
 *
 *
 *   Compile: javac DSRgraph.java
 *
 *
 *   A class representing a reaction network (mechanism)
 *   using dipartite-species-reaction graph
 *
 **********************************************************/


public class DSRgraph {

   private final boolean MESSAGE = true;
   private final boolean ALLOW_DUPLICATE_REACTION = true;

   private int S;  /* number of species */
   private int R;  /* number of reactions */
   private int E;  /* number of species-reaction relations */
 
   private RedBlackBST<String, Species> species;    /* contain all the species */
   private RedBlackBST<String, Reaction> reactions; /* contain all the reactions */


   /* create an empty DSRgraph */
   public DSRgraph() {
       S = 0;
       R = 0;
       E = 0;
       species = new RedBlackBST<String, Species>();
       reactions = new RedBlackBST<String, Reaction>();
   }

   /* check existance of a species */
   public boolean contains(Species s) {
       return species.contains(s.name());
   } 
   
   /* check existance of a reaction */
   public boolean contains(Reaction r) {
       return reactions.contains(r.formula());
   }

   /* add a species */
   public void add(Species s) {
       if (contains(s)) {
           if (MESSAGE) StdOut.println("duplicate species");
       }
       else {
           species.put(s.name(), s);
           S++;
       }
   }

   /* add a reaction */
   public void add(Reaction r) {
       if (!contains(r)) {
          reactions.put(r.formula(), r);
          R++;
          for (Species s : r.reactants()) {
            if (!contains(s)) add(s);
          }
          for (Species s : r.products()) {
            if (!contains(s)) add(s);
          }
       }
       else {
         if (MESSAGE) StdOut.println("duplicate reaction");
         if (ALLOW_DUPLICATE_REACTION) {
            r.setFormula(r.formula() + " (dup)");
            add(r);
         }
       }
   }
 
   /* return the number of species */
   public int S() {
       return S;
   }
    
   /* return the number of reactions */
   public int R() {
       return R;
   }

   /* return all the species */
   public Iterable<Species> species() {
      Bag<Species> b = new Bag<Species>();
      for (String name : species.keys()) 
                 b.add(species.get(name));
      return b;
   }

   /* return all the species */
   public Iterable<Reaction> reactions() {
      Bag<Reaction> b = new Bag<Reaction>();
      for (String formula : reactions.keys()) 
                 b.add(reactions.get(formula));
      return b;
   }


   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(S + " species: \n");
      for (Species s : species()) sb.append(s + ", ");
      sb.delete(sb.length()-2, sb.length());
      sb.append("\n");
      sb.append(R + " reactions: \n");
      for (Reaction r : reactions()) sb.append(r);
      return sb.toString();
   }

   public static void main(String[] args) {
      Species a = new Species("a");
      Species b = new Species("b");
      Species c = new Species("c");
      Species d = new Species("d");
      Species e = new Species("e");
      Reaction r1 = new Reaction("r1");
      Reaction r2 = new Reaction("r2");
      Reaction r3 = new Reaction("r3");
      r1.addReactant(a);
      r1.addReactant(b);
      r1.addProduct(c);
      r2.addReactant(c);
      r2.addProduct(d);
      r3.addReactant(e);
      r3.addProduct(a);
      //StdOut.print(r1);
      //StdOut.print(r2);
      DSRgraph dsr = new DSRgraph();
      dsr.add(r1);
      dsr.add(r2);
      dsr.add(r3);
      StdOut.print(dsr);
   }

}
