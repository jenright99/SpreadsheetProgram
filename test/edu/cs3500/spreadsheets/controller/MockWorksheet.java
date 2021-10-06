package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.sexp.Sexp;
import java.io.IOException;
import java.util.List;

/**
 * Mocks an {@link IWorksheet}, appending specific actions to an appendable before execution.
 */
public class MockWorksheet implements IWorksheet<Coord, Sexp> {

  IWorksheet<Coord, Sexp> worksheet;
  Appendable ap;

  /**
   * Instantes both the worksheet being mocked and the appendable.
   *
   * @param worksheet the worksheet to be mocked.
   * @param ap        the appendable to append commands too.
   */
  public MockWorksheet(IWorksheet<Coord, Sexp> worksheet, Appendable ap) {
    if (ap == null) {
      throw new IllegalArgumentException("Appendable cannot be null.");
    }
    this.worksheet = worksheet;
    this.ap = ap;
  }

  @Override
  public void addCell(int col, int row, String contents) {
    try {
      ap.append("Adding formula " + contents + " at cordinate (" + col + ", " + row + ")\n");
    } catch (IOException io) {
      //Do nothing
    }
    this.worksheet.addCell(col, row, contents);
  }

  @Override
  public boolean containsCell(Coord coord) {
    return this.worksheet.containsCell(coord);
  }

  @Override
  public String getValueAt(int col, int row) {
    return this.worksheet.getValueAt(col, row);
  }

  @Override
  public Sexp getValueAt(Coord coord) {
    return this.worksheet.getValueAt(coord);
  }

  @Override
  public String getContents(int col, int row) {
    return this.getContents(col, row);
  }

  @Override
  public String getContents(Coord coord) {
    return this.getContents(coord);
  }

  @Override
  public int numberOfCells() {
    return this.worksheet.numberOfCells();
  }

  @Override
  public void remove(int col, int row) {
    try {
      this.ap.append("Removing cell at coordinate (" + col + ", " + row + ")");
    } catch (IOException io) {
      //Do nothing
    }
    this.worksheet.remove(col, row);
  }

  @Override
  public List<Coord> nonEmptyCoords() {
    return this.worksheet.nonEmptyCoords();
  }
}
