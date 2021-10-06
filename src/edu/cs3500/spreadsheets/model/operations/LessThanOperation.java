package edu.cs3500.spreadsheets.model.operations;

import edu.cs3500.spreadsheets.model.IFormula;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.List;

/**
 * Takes in two {@link IFormula}s, which evaluate to Doubles, and returns whether or not the first
 * is less than the second.
 */
public class LessThanOperation implements IOperation<Double> {


  @Override
  public Sexp apply(List<IFormula> args) {
    //TODO Hey, am I right?
    if (args.size() != 2) {
      throw new IllegalArgumentException("Please enter two arguments to the < operation.");
    }

    Sexp arg1 = args.get(0).evaluate();
    Sexp arg2 = args.get(1).evaluate();

    //System.out.println("Comparing " + arg1.accept(this) + " < " + arg2.accept(this));

    return new SBoolean(arg1.accept(this) < arg2.accept(this));


  }

  @Override
  public String name() {
    return "<";
  }

  @Override
  public Double visitBoolean(boolean b) {
    throw new IllegalArgumentException("Less than only takes number arguments.");
  }

  @Override
  public Double visitNumber(double d) {
    return d;
  }

  @Override
  public Double visitSList(List<Sexp> l) {
    if (l.size() != 1) {
      throw new IllegalArgumentException("Please enter two arguments to the < operation.");
    } else {
      return l.get(0).accept(new SumOperation());
    }
  }

  @Override
  public Double visitSymbol(String s) {
    throw new IllegalArgumentException("Less than only takes number arguments.");
  }

  @Override
  public Double visitString(String s) {
    throw new IllegalArgumentException("Less than only takes number arguments.");
  }
}
