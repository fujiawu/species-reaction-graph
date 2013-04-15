/******************************************
 *
 *  Compilation: javac Reaction.java
 * 
 *
 *  A class representing a chemical reaction
 *
 *
 * ***************************************/

public class Reaction implements Comparable<Reaction> {

  private String name;
  private ST<Species, Double> st;
  private double fr;  /* forward rate */
  private double rr;  /* reverse rate */

  /* a helper class for outputing species and nu */
  public static class Reactant {
      public Species s;
      public double nu;
      public Reactant(Species s, double nu) {
             this.s = s;
             this.nu = nu;
      }
  }

  /* default constructor */
  public Reaction() {
      name = "noname";
      st = new ST<Species, Double>();
  }

  /* create a reaction with a name */
  public Reaction(String name) {
      this.name = name;
      st = new ST<Species, Double>();
  }

  /* add a reactant */
  public void addReactant(Species s, double nu) {
      if (st.contains(s)) throw new
          IllegalArgumentException("duplicate species in one reaction");
      st.put(s, nu);
  }

  /* add a reactant */
  public void addReactant(Reactant rt) {
      if (st.contains(rt.s)) throw new
          IllegalArgumentException("duplicate species in one reaction");
      st.put(rt.s, rt.nu);
  }

  /* remove a reactant */
  public void removeReactant(Species s) {
      st.delete(s);
  }

  /* remove a reactant */
  public void removeReactant(Reactant rt) {
      st.delete(rt.s);
  }

  /* update a reactant */
  public void updateReactant(Species s, double nu) {
      removeReactant(s);
      addReactant(s, nu);
  }

  /* update a reactant */
  public void updateReactant(Reactant rt) {
      removeReactant(rt);
      addReactant(rt.s, rt.nu);
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

  /* change the name */
  public void setName(String n) {
      name = n;
  }

  /* return the name */
  public String name() {
      return name;
  }

 /* return the st and coefficients */
  public Reactant[] reactants() {
      Reactant[] rs = new Reactant[st.size()];
      int i = 0;
      for (Species s : st.keys()) {
          rs[i] = new Reactant(s, st.get(s));
          i++;
      }
      return rs; 
  }

  /* compare function */
  public int compareTo(Reaction that) {
      return this.name().compareTo(that.name());
  }

  /* return String representation */
  public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(name + ":");
      StringBuilder left = new StringBuilder();
      StringBuilder right = new StringBuilder();
      Reactant[] rs = this.reactants();
      for (Reactant r : rs) {
          if (r.nu < 0) {
               if (r.nu == -1) {
               left.append(r.s.name() + "+");
               } else {
               left.append(Math.abs(r.nu) + r.s.name() + "+");
               }
          } 
          if (r.nu > 0) {
               if (r.nu == 1) {
               right.append(r.s.name() + "+");
               } else {
               right.append(r.nu + r.s.name() + "+");
               }
          }
      }
      sb.append(left.substring(0, left.length()-1));
      sb.append("=");
      sb.append(right.substring(0, right.length()-1));
      return sb.toString();
  }

  /* a test client */
  public static void main(String[] args) {
      Reaction r = new Reaction("H + O2 = HO2");
      Species h = new Species("H");
      Species o2 = new Species("O2");
      Species ho2 = new Species("HO2");
      r.addReactant(h, -2);
      r.addReactant(o2, -1);
      r.addReactant(ho2, 3);
      r.setName("branching");
      r.removeReactant(h);
      r.addReactant(h, -1);
      r.updateReactant(ho2, 1);
      StdOut.println(r);
  }

}
