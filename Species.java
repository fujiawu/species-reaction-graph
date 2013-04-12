/******************************************
 *
 *  Compilation: javac Species.java
 * 
 *
 *  A class representing species
 *
 *
 * ***************************************/


public class Species {

  private String name;  /* String representation  */
  private Bag<Reaction> reactIn;  /* reactions participate as reacants */
  private Bag<Reaction> produceBy; /* reactions participate as products */

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
 
  public void addReactIn(Reaction r) {
      reactIn.add(r);
  }

  public void addProduceBy(Reaction r) {
      produceBy.add(r);
  }
 
  /* return the name */ 
  public String name() {
      return name;   
  }
   
  /* return reactions that participate as reactants */
  public Iterable<Reaction> reactIn() {
      return reactIn;
  }

  /* return reactions that participate as products */
  public Iterable<Reaction> produceBy() {
      return produceBy;
  }


}
