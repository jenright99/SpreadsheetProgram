package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Represents a Formula which outputs its result as a {@link Sexp} result.
 */
public interface IFormula {

  /**
   * The evaluation of the formula.
   *
   * @return the result as a {@link Sexp}
   */
  Sexp evaluate();

  /**
   * All of the coordinates referenced by this function.
   *
   * @return the coordiates refernced by this function as a list of {@link Coord}.
   */
  List<Coord> referencedCoords();
}
