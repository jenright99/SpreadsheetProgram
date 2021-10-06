package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.operations.IOperation;
import edu.cs3500.spreadsheets.model.operations.OperationFactory;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Converts a {@link Sexp} into an {@link IFormula} representation of that {@link Sexp}.
 */
public class FormulaCreator implements SexpVisitor<IFormula> {

  private IWorksheet<Coord, Sexp> worksheet;

  /**
   * Instantiates the worksheet, which tells the {@link ReferenceFormula}s which worksheet they are
   * referencing.
   *
   * @param worksheet The {@link IWorksheet} that the reference formulas reference.
   */
  public FormulaCreator(
      IWorksheet<Coord, Sexp> worksheet) {
    this.worksheet = worksheet;
  }

  //The Alphabet of characters that a Coordinate could be.
  public static final ArrayList<Character> ALPHABET = new ArrayList<Character>(
      Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
          'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));

  /**
   * Converts a string representation of a single coordinate or a list of coordinates into a list of
   * {@link Coord}.
   *
   * @param s the string to be converted.
   * @return a List of {@link Coord} representation of the string s.
   */
  public static List<Coord> stringToCoord(String s) {

    s = s.toUpperCase();

    if (s.contains(":")) {
      int colon = s.indexOf(':');
      String left = s.substring(0, colon);
      String right = s.substring(colon + 1);

      int leftDigit = FormulaCreator.indexOfDigit(left);
      int rightDigit = FormulaCreator.indexOfDigit(right);

      int row1 = Coord.colNameToIndex(left.substring(0, leftDigit));
      int col1 = Integer.parseInt(left.substring(leftDigit));
      int row2 = Coord.colNameToIndex(right.substring(0, rightDigit));
      int col2 = Integer.parseInt(right.substring(rightDigit));

      ArrayList<Coord> coords = new ArrayList<Coord>();
      //Get every coord between those two coordinates
      for (int i = row1; i <= row2; i++) {
        for (int j = col1; j <= col2; j++) {
          coords.add(new Coord(i, j));
        }
      }

      return coords;
    } else {

      int indexOfDigit = FormulaCreator.indexOfDigit(s);

      Coord newCoord = new Coord(Coord.colNameToIndex(s.substring(0, indexOfDigit)),
          Integer.parseInt(s.substring(indexOfDigit)));

      return new ArrayList<Coord>(Arrays.asList(newCoord));
    }

  }

  /**
   * Returns the first character in a given string that is a digit. Since the string is a cell
   * reference, errors are thrown if the reference is misformatted.
   *
   * @param s the string representation of a reference.
   * @return the first instance of a digit.
   */
  public static int indexOfDigit(String s) {
    int counter = 0;

    if (s.contains(":")) {
      throw new IllegalArgumentException("Badly Formatted Reference!");
    }
    try {
      while (!Character.isDigit(s.charAt(counter))) {
        if (!ALPHABET.contains(s.charAt(counter))) {
          throw new IllegalArgumentException("Illegal Symbol in Reference!");
        }
        counter++;
      }
    } catch (IndexOutOfBoundsException ioe) {
      throw new IllegalArgumentException("Badly Formatted Reference!");
    }
    return counter;
  }

  @Override
  public IFormula visitBoolean(boolean b) {
    return new IdentityFormula(new SBoolean(b));
  }

  @Override
  public IFormula visitNumber(double d) {
    return new IdentityFormula(new SNumber(d));
  }

  @Override
  public IFormula visitSList(List<Sexp> l) {

    IOperation op = OperationFactory.createOperation(l.get(0).toString());

    if (op == null) {
      if (l.size() == 1) {
        return l.get(0).accept(this);
      } else {
        throw new IllegalArgumentException("Bad Forumula.");
      }
    }

    ArrayList<IFormula> args = new ArrayList<IFormula>();

    for (int i = 1; i < l.size(); i++) {
      args.add(l.get(i).accept(this));
    }

    return new FunctionFormula(op, args);
  }

  @Override
  public IFormula visitSymbol(String s) {

    if (ALPHABET.contains(s.charAt(0))) {
      return new ReferenceFormula(FormulaCreator.stringToCoord(s), this.worksheet);
    }

    return new IdentityFormula(new SSymbol(s));

  }

  @Override
  public IFormula visitString(String s) {
    return new IdentityFormula(new SString(s));
  }
}
