package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.operations.IOperation;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a function, with an {@link IOperation} and a list of arguments, represented as {@link
 * IFormula}, to apply the operation on.
 */
public final class FunctionFormula implements IFormula {

  private IOperation op;
  private List<IFormula> args;

  /**
   * Instantiates the operation and the arguments the operation is acting on.
   *
   * @param op the given {@link IOperation}
   * @param args the list of {@link IFormula} the operation will act on.
   */
  public FunctionFormula(IOperation op, List<IFormula> args) {
    this.op = op;
    this.args = args;
  }

  @Override
  public Sexp evaluate() {
    return this.op.apply(args);
  }

  @Override
  public List<Coord> referencedCoords() {
    ArrayList<Coord> coords = new ArrayList<Coord>();
    for (IFormula arg : args) {
      coords.addAll(arg.referencedCoords());
    }
    return coords;
  }
}
