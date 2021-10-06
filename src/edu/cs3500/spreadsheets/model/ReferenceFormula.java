package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a reference to one or many other cells in a {@link IWorksheet}, stored as a list of
 * {@link Coord}s.
 */
public final class ReferenceFormula implements IFormula {

  private List<Coord> referencedCoords;
  private IWorksheet<Coord, Sexp> worksheet;

  /**
   * Instantiates the list of {@link Coord}s and the worksheet which this cell references.
   *
   * @param coords the list of {@link Coord}s to be referenced.
   * @param worksheet the {@link IWorksheet} the reference Formula is referencing.
   */
  public ReferenceFormula(List<Coord> coords, IWorksheet<Coord, Sexp> worksheet) {
    this.referencedCoords = coords;
    this.worksheet = worksheet;
  }


  @Override
  public Sexp evaluate() {
    List<Sexp> values = new ArrayList<Sexp>();
    for (Coord c : this.referencedCoords) {
      /*
      if (this.worksheet.containsCell(c)) {
        values.add(this.worksheet.getValueAt(c));
      }
       */
      values.add(this.worksheet.getValueAt(c));
    }
    return new SList(values);
  }

  @Override
  public List<Coord> referencedCoords() {
    return new ArrayList<Coord>(this.referencedCoords);
  }
}
