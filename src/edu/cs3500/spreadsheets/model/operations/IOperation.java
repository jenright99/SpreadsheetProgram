package edu.cs3500.spreadsheets.model.operations;

import edu.cs3500.spreadsheets.model.IFormula;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.List;

/**
 * Preforms an operation on a list of {@link IFormula}s, return the output as a Sexp.
 *
 * @param <R> The data type of values being operated on.
 */
public interface IOperation<R> extends SexpVisitor<R> {

  /**
   * Preforms the operation on the given list of {@link IFormula} and returns the Sexp value.
   *
   * @param args the list of {@link IFormula}s to be operated on.
   * @return the produced Sexp.
   */
  Sexp apply(List<IFormula> args);

  /**
   * The string that a formula would contain if it wanted to call the operation on a set of
   * arguments.
   *
   * @return the string representation of the operation.
   */
  String name();

}
