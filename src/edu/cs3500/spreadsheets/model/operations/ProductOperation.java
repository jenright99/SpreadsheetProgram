package edu.cs3500.spreadsheets.model.operations;

import edu.cs3500.spreadsheets.model.IFormula;
import edu.cs3500.spreadsheets.model.IdentityFormula;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Returns the product of all the values given in the apply method, with 0 being the default value.
 */
public class ProductOperation implements IOperation<Double> {


  private boolean firstOp;

  /**
   * Sets the boolean firstOp to true, as the firstOp has yet to occur.
   */
  public ProductOperation() {
    this.firstOp = true;
  }

  @Override
  public Sexp apply(List<IFormula> args) {
    int sum = 1;
    this.firstOp = true;
    for (IFormula arg : args) {

      Sexp next = arg.evaluate();
      sum *= next.accept(this);

    }

    if (firstOp) {
      return new SNumber(0);
    }

    return new SNumber(sum);
  }

  @Override
  public String name() {
    return "PRODUCT";
  }

  @Override
  public Double visitBoolean(boolean b) {
    return 1.0;
  }

  @Override
  public Double visitNumber(double d) {
    if (firstOp) {
      firstOp = false;
    }
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
    return 1.0;
  }

  @Override
  public Double visitString(String s) {
    return 1.0;
  }
}
