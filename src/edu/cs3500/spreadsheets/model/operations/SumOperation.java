package edu.cs3500.spreadsheets.model.operations;

import edu.cs3500.spreadsheets.model.IFormula;
import edu.cs3500.spreadsheets.model.IdentityFormula;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the sum of all the values in the given list of formulas.
 */
public class SumOperation implements IOperation<Double> {

  @Override
  public Sexp apply(List<IFormula> args) {
    double sum = 0;
    for (IFormula arg : args) {
      Sexp next = arg.evaluate();
      sum += next.accept(this);
    }

    return new SNumber(sum);
  }

  @Override
  public String name() {
    return "SUM";
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 0.0;
  }

  @Override
  public Double visitNumber(double d) {
    return d;
  }

  @Override
  public Double visitSList(List<Sexp> l) {
    List<IFormula> formulas = new ArrayList<IFormula>();
    for (Sexp value : l) {
      formulas.add(new IdentityFormula(value));
    }

    return this.apply(formulas).accept(this);
  }

  @Override
  public Double visitSymbol(String s) {
    return 0.0;
  }

  @Override
  public Double visitString(String s) {
    return 0.0;
  }
}
