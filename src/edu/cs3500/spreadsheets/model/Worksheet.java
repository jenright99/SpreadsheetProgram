package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an {@link IWorksheet} which uses {@link Coord}s to store cells and communicates values
 * with {@link Sexp}s.
 */
public class Worksheet implements IWorksheet<Coord, Sexp> {

  private HashMap<Coord, ICell> cells;

  private HashMap<Coord, Sexp> values;

  private HashMap<Coord, Boolean> cyclical;

  /**
   * Instantiates the cells, values, anc cyclical checking.
   */
  public Worksheet() {
    this.cells = new HashMap<Coord, ICell>();
    this.values = new HashMap<Coord, Sexp>();
    this.cyclical = new HashMap<Coord, Boolean>();
  }

  /**
   * Returns if the given Coord is referenced in the given Coordinates references.
   *
   * @param coord    the coord we are searching for.
   * @param contents the references we are searching.
   * @return whether the given coord is contained in the given Coordinate references.
   */
  private boolean checkCyclical(Coord coord, List<Coord> contents) {
    if (this.cyclical.containsKey(coord)) {
      return this.cyclical.get(coord);
    } else {
      boolean result = breadthFirstSearch(coord, contents);
      this.cyclical.put(coord, result);
      return result;
    }
  }

  /**
   * Preforms a breadth first search to see if the given {@link Coord} is in given Coordinate
   * references.
   *
   * @param coord    the coord we are searching for.
   * @param contents the references we are searching.
   * @return whether the given coord is contained in the given Coordinate references.
   */
  private boolean breadthFirstSearch(Coord coord, List<Coord> contents) {
    for (Coord c : contents) {
      if (c.equals(coord)) {
        return true;
      }
    }

    for (Coord c : contents) {
      if (this.containsCell(c) && this.checkCyclical(coord, this.cells.get(c).referencedCoords())) {
        return true;
      }
    }

    //this.cyclical.put(coord, false);

    return false;

  }

  @Override
  public void addCell(int col, int row, String contents) {
    this.values = new HashMap<Coord, Sexp>();

    Coord newCoord = new Coord(col, row);

    if (this.cyclical.containsKey(newCoord)) {
      this.cyclical.remove(newCoord);
    }
    IFormula cellContents;
    try {

      if (contents.charAt(0) == '=') {
        cellContents = Parser.parse(contents.substring(1)).accept(new FormulaCreator(this));
      } else {
        cellContents = new IdentityFormula(Parser.parse(contents));
      }

      if (this.checkCyclical(newCoord, cellContents.referencedCoords())) {
        cellContents = new IdentityFormula(new SSymbol("#ERROR"));
        this.cyclical.put(newCoord, true);
      } else {
        this.cyclical.put(newCoord, false);
      }
    } catch (IllegalArgumentException iae) {
      cellContents = new IdentityFormula(new SSymbol("#ERROR"));
    }

    if (this.containsCell(newCoord)) {
      this.cells.replace(newCoord, new Cell(cellContents, contents));
    } else {
      this.cells.put(newCoord, new Cell(cellContents, contents));
    }
  }


  @Override
  public boolean containsCell(Coord coord) {
    return cells.containsKey(coord);
  }

  @Override
  public String getValueAt(int col, int row) {
    return this.getValueAt(new Coord(col, row)).accept(new FormatedSexpString());
  }


  @Override
  public Sexp getValueAt(Coord coord) {
    if (this.containsCell(coord)) {
      if (this.values.containsKey(coord)) {
        return this.values.get(coord);
      }

      if (this.cyclical.containsKey(coord) && this.cyclical.get(coord)) {
        throw new IllegalArgumentException(
            "Error in cell " + coord.toString() + ": Cyclical Cell.");
      }

      Sexp newValue = this.cells.get(coord).evaluate();
      this.values.put(coord, newValue);
      return newValue;

    }
    return new SSymbol("");
  }

  @Override
  public String getContents(int col, int row) {
    return this.getContents(new Coord(col, row));
  }

  @Override
  public String getContents(Coord coord) {
    if (this.cells.containsKey(coord)) {
      return this.cells.get(coord).originalFormula();
    }

    return "";
  }

  @Override
  public int numberOfCells() {
    return this.cells.size();
  }

  @Override
  public void remove(int col, int row) {
    Coord newCoord = new Coord(col, row);
    if (this.containsCell(newCoord)) {
      this.cells.remove(newCoord);
      this.values = new HashMap<Coord, Sexp>();
      this.cyclical.remove(newCoord);
    }
  }

  @Override
  public List<Coord> nonEmptyCoords() {
    return new ArrayList<Coord>(this.cells.keySet());
  }
}
