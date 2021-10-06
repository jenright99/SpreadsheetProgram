package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * A Representation of the {@link ICell} interface which stores {@link IFormula}s.
 */
public final class Cell implements ICell {

  private IFormula contents;
  private String originalFormula;

  /**
   * Creates a cell with the original formula and the formula as an {@link IFormula}.
   *
   * @param contents        the contents as an IFormula
   * @param originalFormula the contents as a string.
   */
  public Cell(IFormula contents, String originalFormula) {
    this.contents = contents;
    this.originalFormula = originalFormula;
  }

  /**
   * Creates a cell with contents but no original formula, used mostly for testing.
   *
   * @param contents the contents as an IFormula
   */
  public Cell(IFormula contents) {
    this(contents, "");
  }

  @Override
  public Sexp evaluate() {
    return contents.evaluate();
  }

  @Override
  public List<Coord> referencedCoords() {
    return contents.referencedCoords();
  }

  @Override
  public String originalFormula() {
    return this.originalFormula;
  }
}
