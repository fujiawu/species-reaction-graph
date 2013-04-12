/******************************************
 *
 *  Compilation: javac Reaction.java
 * 
 *
 *  A class representing a chemical reaction
 *
 *
 * ***************************************/


public class Reaction {

  private String formula;
  private Bag<Species> reactants;
  private Bag<Species> products;
  private double fr;  /* forward rate */
  private double rr;  /* reverse rate */

  /* default constructor */
  public Reaction() {
      formula = "noformula";
  }

  /* create a reaction with a formula */
  public Reaction(String formula) {
      this.formula = formula;
  }

  /* add a reactant */
  public void addReactant(Species r) {
      reactants.add(r);
  }

  /* add a product */
  public void addProduct(Species r) {
      products.add(r);
  }

  /* return the total rate */
  public double rate() {
      return fr-rr;
  }

  /* return the forward rate */
  public double forwardRate() {
      return fr;
  }

  /* return the reverse rate */ 
  public double reverseRate() {
      return rr;
  }

  /* set the forward rate */
  public void setForwardRate(double fr) {
      if (fr < 0) throw new 
          IllegalArgumentException("forward rate cannot be negative");
      this.fr = fr;
  }

  /* set the reverse rate */
  public void setReverseRate(double rr) {
      if (rr < 0) throw new 
          IllegalArgumentException("reverse rate cannot be negative");
      this.rr = rr;
  }

  /* return the reactants */
  public Iterable<Species> reactants() {
      return reactants;
  } 

  /* return the products */
  public Iterable<Species> products() {
      return products;
  }

  /* return String representation */
  public String toString() {
      return formula;
  }

}
