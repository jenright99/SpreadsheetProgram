package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.IWorksheet;
import java.util.List;

/**
 * Creates a basic 'View Only' version of an IWorksheet.
 *
 * @param <K> The Object used by the IWorksheet to store Coords.
 * @param <J> The Object used by the IWorksheet to store values.
 */
public class ViewSpreadsheet<K, J> implements ISpreadsheetAdapter<K> {

  IWorksheet<K, J> worksheet;

  /**
   * Instantiates the IWorksheet field of this ViewSpreadsheet.
   *
   * @param worksheet The worksheet to adapt.
   */
  public ViewSpreadsheet(IWorksheet worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public String getValueAt(int col, int row) {
    return this.worksheet.getValueAt(col, row);
  }

  @Override
  public String getContents(K coord) {
    return this.worksheet.getContents(coord);
  }

  @Override
  public String getContents(int col, int row) {
    return this.worksheet.getContents(col, row);
  }

  @Override
  public List<K> nonEmptyCoords() {
    return this.worksheet.nonEmptyCoords();
  }

}
