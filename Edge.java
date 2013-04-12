/******************************************
 *
 *  Compilation: javac Edge.java
 * 
 *
 *  A species-reaction relation with rate
 *  information
 *
 * ***************************************/


public class Edge {

  private final int s;  /* index of connected species */
  private final int r;  /* index of connected reaction */
  private double rate;  /* rate of this relation 
                           positive meaning next production of the species 
                           through this reaction */
  
  /* create an edge */
  public Edge(int s, int r, double rate) {
     this.s = s;
     this.r = r;
     this.rate = rate;
  }

  /* return the rate of this relation */
  public double rate() {
      return rate;
  }

  /* set the rate of this relation */
  public void setrate(double rate) {
      this.rate = rate;
  }

  /* return species index of this relation */
  public int species() {
      return s;
  } 

  /* return reaction index of this relation */
  public int reaction() {
      return r;
  }

  /* compare edges by rate */
  public int compareTo(Edge that) {
      if      (this.rate() < that.rate()) return -1;
      else if (this.rate() > that.rate()) return +1;
      else                                return 0;
  }

  /* return String representation */
  public String toString() {
      return String.format("%d-%d %0.5f", s, r, rate);
  }

}
