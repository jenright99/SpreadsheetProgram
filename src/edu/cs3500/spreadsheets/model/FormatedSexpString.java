package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.List;

/**
 * A SexpVisitor which converts {@link Sexp} into properly formatted {@link String}s.
 * This includes trailing zeroes for doubles and quotes for Strings.
 */
public class FormatedSexpString implements SexpVisitor<String> {

  @Override
  public String visitBoolean(boolean b) {
    return String.format("%b", b);
  }

  @Override
  public String visitNumber(double d) {
    return String.format("%f", d);
  }

  @Override
  public String visitSList(List<Sexp> l) {
    //TODO this might be a bad case
    return l.get(0).accept(this);
  }

  @Override
  public String visitSymbol(String s) {
    return String.format("%s", s);
  }

  @Override
  public String visitString(String s) {
    return String.format("%s", new SString(s).toString());
  }
}
