/******************************************
 *
 *  Compilation: javac Species.java
 * 
 *
 *  A class representing species
 *
 *
 * ***************************************/


public class Species implements Comparable<Species> {

  private String name;  /* String representation  */

  /* Note that any reaction is treated as reversible, so there is
   * really no distinct between reactants and products. However, we
   * still make reactant/products clear in our model.  */

  /* default constructor */
  public Species() {
      name = "noname";
  }

  /* create a species with name */
  public Species(String name) {
      this.name = name;
  }
 
  /* return the name */ 
  public String name() {
      return name;   
  }
   
  /* compare function */
  public int compareTo(Species that) {
      return this.name().compareTo(that.name());
  }
 
  /* return a string representation of a species */
  public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(name);
      return sb.toString();
  }

  /* test client */
  public static void main(String[] args) {
      Species s = new Species("H2");
      StdOut.println(s);
  }
}
