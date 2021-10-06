package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single value as a formula.
 */
public final class IdentityFormula implements IFormula {

  private final Sexp value;

  /**
   * The {@link Sexp} value the IdentityFormula represents.
   *
   * @param value the value of the IdentityFormula.
   */
  public IdentityFormula(Sexp value) {
    this.value = value;
  }


  @Override
  public Sexp evaluate() {
    return value;
  }

  @Override
  public List<Coord> referencedCoords() {
    return new ArrayList<Coord>();
  }
}
