package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * A cell in a spreadsheet which communicates via {@link Sexp} and is referenced via {@link
 * Coord}s.
 */
public interface ICell {

  /**
   * Returns the value of the contents of the cell.
   *
   * @return A {@link Sexp} representation of the contents of the cell.
   */
  Sexp evaluate();

  /**
   * All the Coordinates referenced by the contents of this cell.
   *
   * @return the Coordinates referenced by the contetns of this cell as a list of {@link Coord}
   */
  List<Coord> referencedCoords();

  /**
   * Returns the string used to create the contents of this cell.
   *
   * @return the string used to create the contents of this cell.
   */
  String originalFormula();
}
