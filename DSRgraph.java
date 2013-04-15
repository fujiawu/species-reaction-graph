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

   private int E;  /* number of species-reaction relations */

   private ST<String, Species> sDict;  /* String-Species dictionary */
   private ST<String, Reaction> rDict; /* String-Reaction dictionary */
 
   private ST<Species, ST<Reaction, Double>> species;    /* contain all the species */
   private SET<Reaction> reactions; /* contain all the reactions */

   /* create an empty DSRgraph */
   public DSRgraph() {
       E = 0;
       species = new ST<Species, ST<Reaction, Double>>();
       reactions = new SET<Reaction>();
       sDict = new ST<String, Species>();
       rDict = new ST<String, Reaction>();
   }

   /* check existance of a species */
   public boolean contains(Species s) {
       return species.contains(s);
   } 
   
   /* check existance of a reaction */
   public boolean contains(Reaction r) {
       return reactions.contains(r);
   }

   /* add a species */
   public void add(Species s) {
       if (contains(s)) {
           if (MESSAGE) StdOut.println("duplicate species");
       }
       else {
           species.put(s, new ST<Reaction, Double>());
           sDict.put(s.name(), s);
       }
   }

   /* remove a species */
   public void remove(Species s) {
       if (contains(s)) {
          for (Reaction r : species.get(s).keys()) remove(r);
          species.delete(s);
       }
   }

   /* add a reaction */
   public void add(Reaction r) {
       if (!contains(r)) {
          reactions.add(r);
          rDict.put(r.name(), r);
          for (Reaction.Reactant rt : r.reactants()) {
            if (!contains(rt.s)) add(rt.s);
            species.get(rt.s).put(r, rt.nu); 
            E++;
            }
       }
       else {
         if (MESSAGE) StdOut.println("duplicate reaction");
         if (ALLOW_DUPLICATE_REACTION) {
            r.setName(r.name() + "(dup)");
            add(r);
         }
       }
   }
 
   /* remove a reaction */
   public void remove(Reaction r) { 
      if (contains(r)) {
          reactions.delete(r); 
          for (Reaction.Reactant rt : r.reactants()) {
            if (!contains(rt.s)) species.get(rt.s).delete(r);
          }
      }
   }

   /* remove all reactions and species */
   public void removeAll() {
      for (Reaction r : reactions()) remove(r);
      for (Species s : species()) remove(s);
      clearDict();
   }

   /* clear Dictionary */
   private void clearDict() {
      sDict = new ST<String, Species>();
      rDict = new ST<String, Reaction>();
   }

   /* return the number of species */
   public int sSize() {
       return species.size();
   }
    
   /* return the number of reactions */
   public int rSize() {
       return reactions.size();
   }

   /* return all the species */
   public Iterable<Species> species() {
      return species;
   }

   /* return all the species */
   public Iterable<Reaction> reactions() {
      return reactions;
   }

   /* print to a string */
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(sSize() + " species: \n");
      for (Species s : species()) sb.append(s + ", ");
      sb.delete(sb.length()-2, sb.length());
      sb.append("\n");
      sb.append(rSize() + " reactions: \n");
      for (Reaction r : reactions()) sb.append(r + "\n");
      return sb.toString();
   }

   /* output a graphviz shape */
   public void Graphviz(String filename, String type) {
      if (type.equals("skeleton")) {
          StringBuilder sb = new StringBuilder();
          sb.append("digraph G {\n");
          sb.append("size = \"80,80\"; \n");
          for (Reaction r : reactions()) {
              for (Reaction.Reactant rt : r.reactants()) {
                sb.append("\"" + r.name() + "\" [shape=box]; \n");
                sb.append("\"" + r.name() + "\" -> \"" + rt.s.name() + "\" [arrowhead=none]; \n");
              }
          }
          sb.append("}\n");
          Out out = new Out(filename);
          out.print(sb);
      }
      else {
          throw new IllegalArgumentException("Incorrect Graph Type");
      }
   }


   /* read species-reaction information from chem.out */
   public void readChemOut(In in) {
       int state = 0;
       // 0 for ignore
       // 1 for reading species information
       // 2 for reading reaction information
       String s;
       while (!in.isEmpty()) {
           s = in.readLine();
           if (state == 0) {
              if (s.length() > 11 && s.substring(0, 11).equals(" CONSIDERED"))
              { in.readLine(); state = 1; }
              if (s.length() > 26 && 
                  s.substring(0,26).equals("      REACTIONS CONSIDERED"))
              { in.readLine(); state = 2;}

           }
           else if (state == 1) {
              if (s.length() > 11 && s.substring(0,11).equals(" ----------"))
              { state = 0;}
              else {
                String[] sa = s.split("\\s+");
                add(new Species(sa[2]));
              }
           }
           else if (state == 2) {
             if (s.length() > 7 && 
                   s.substring(0,7).equals("  NOTE:"))
              { state = 0;}
             else {
                String[] sa = s.split("\\s+");
                if (sa.length > 2 && sa[2].contains("=")) {
                   //StdOut.println(sa[2]);
                   Reaction newr = str2Reaction(sa[2]);
                   if (newr != null) add(newr);
                }
             }
          }
       }
   }

   /* remove brack and inside from a String */
   private static String removeBracket(String s) {
       StringBuilder sb = new StringBuilder();
       int state = 0;
       int i = 0;
       while(i < s.length()) {
           char c = s.charAt(i);
           if (state == 0) {
              if (c == '(') { state = 1; }
              else { sb.append(c); }
           }
           else if (state == 1) {
              if (c == ')') { state = 0; }
           }
           i++;
       } 
       return sb.toString(); 
   }

   /* create a new Reaction from a line of String */
   private Reaction str2Reaction(String s) {
       assert s.contains("=");
       Reaction reac = new Reaction(s);
       //StdOut.println(s);
       s = removeBracket(s);
       //StdOut.println(s);
       String[] sa = s.split("=");
       /* process LHS */
       ST<String, Double> left = getStr2Nu(sa[0]);
       ST<String, Double> right = getStr2Nu(sa[1]);
       ST<String, Double> total = mergeStr2Nu(left, right);
       for (String sname : total.keys()) {
          Species sp = sDict.get(sname);
          if (sp == null) throw new
              IllegalArgumentException("couldn't find species in dictionary");
          //StdOut.print(sp + ": "); StdOut.println(total.get(sname));
          reac.addReactant(sp, total.get(sname));
       }
       return reac; 
  }

   /* parse a string of reactant into meaningful species-nu pairs  */
   private static ST<String, Double> getStr2Nu(String str) {
          ST<String, Double> str2nu = new ST<String, Double>();
          String[] sa = str.split("\\+");
          for (String unit : sa) {
             String name = extractName(unit);
             double nu = extractNu(unit);
             if (!name.equals("M")) {
                if (str2nu.contains(name)) { nu = nu + str2nu.get(name); }
                str2nu.put(name, nu);
             }
          }
          return str2nu;
   }

   /* merge STs of species-nu pairs from left to right */
   private static ST<String, Double> mergeStr2Nu(ST<String, Double> left,  
                                     ST<String, Double> right) {
          for (String lname : left.keys()) {
             if (right.contains(lname))
                  { right.put(lname, right.get(lname) - left.get(lname)); }
             else { right.put(lname, -left.get(lname)); }
          }
          Bag<String> toDelete = new Bag<String>(); 
          for (String rname : right.keys()) {
             if (right.get(rname) == 0) toDelete.add(rname);
          }
          for (String zero : toDelete) { right.delete(zero); }
          return right; 
   }

   /* Extract the stoichiometric coefficient from a String */
   private static double extractNu(String s) {
       int i = 0;
       while (i < s.length()) {
          char c = s.charAt(i);
          if (!Character.isDigit(c) && c != '.') break;
          i++;
       }
       //StdOut.println(i);
       if (i == 0) {return 1;} else {
          return Double.parseDouble(s.substring(0,i));
          //String name = s.substring(i,s.length());
          //StdOut.print(nu); StdOut.print(" " + name + "\n");
       }
   }
  
   /* Extract the species name from a String  */
   private static String extractName(String s) {
      int i = 0;
       while (i < s.length()) {
          char c = s.charAt(i);
          if (!Character.isDigit(c) && c != '.') break;
          i++;
       }
       //StdOut.println(i);
       if (i == 0) {return s;} else {
          //return Double.parseDouble(s.substring(0,i));
          return s.substring(i,s.length());
          //StdOut.print(nu); StdOut.print(" " + name + "\n");
       }
   }

   /* add escapte to a string for graphviz */
   /* 
   private static String escape(String s) {
       StringBuilder sb = new StringBuilder(s);
       int i = 0;
       while (i < sb.length()) {
          if (sb.charAt(i) == ' ' || sb.charAt(i) == '|' 
                   || sb.charAt(i) == '(' || sb.charAt(i) == ')') 
          { sb = sb.insert(i, '\\'); i++; }
          i++;
       } 
       return sb.toString();
   }
   */

   public static void main(String[] args) {
      DSRgraph dsr = new DSRgraph();
      if (args.length < 2)
       throw new IllegalArgumentException("Specify input/output files"); 
      In in = new In(args[0]); 
      dsr.readChemOut(in);
      dsr.Graphviz(args[1], "skeleton");
   }

}
