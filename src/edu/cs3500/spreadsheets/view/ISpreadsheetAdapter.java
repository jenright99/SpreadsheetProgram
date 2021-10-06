package edu.cs3500.spreadsheets.view;

import java.util.List;

/**
 * An adapter for the IWorksheet. Basically creates a ViewOnly version of the IWorksheet, with only
 * the accessor methods available and non of the mutatable ones.
 *
 * @param <K> The Object used by the IWorksheet to store Coordinates.
 */
public interface ISpreadsheetAdapter<K> {

  /**
   * Get the evaulated value of the cell at the given coordinates.
   *
   * @param col the column of the referenced cell
   * @param row the row of the referenced cell
   * @return the evaluated value of the cell as a string.
   */
  String getValueAt(int col, int row);

  /**
   * The contents of the cell at the given coordinate as a formula.
   *
   * @param coord the coordinate of the cell being referenced.
   * @return the contents of the cell as a String
   */
  String getContents(K coord);

  /**
   * The contents of the cell at the given coordinate as a formula.
   *
   * @param col the column of the cell being referenced.
   * @param row the row of the cell being referenced.
   * @return the contents of the cell as a String
   */
  String getContents(int col, int row);

  /**
   * Gets all of the coordinates who reference non empty cells in the worksheet.
   *
   * @return the coordinates who referecned non empty cells as a List.
   */
  List<K> nonEmptyCoords();


}
