package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a spreadsheet which uses a coordinate class K and a way to represent values J.
 *
 * @param <K> a coordinate class to store cells at.
 * @param <J> A value class to output cell contents.
 */
public interface IWorksheet<K, J> {

  /**
   * Adds a new cell at the given column and row with the given contents as a string. If there is
   * already a cell there, replace it.
   *
   * @param col      The column of the new cell.
   * @param row      The row of the new cell.
   * @param contents The contents of the new cell.
   */
  void addCell(int col, int row, String contents);

  /**
   * Returns true if the IWorksheet contains the given coordinate, represented as K.
   *
   * @param coord the K coordinate possibly contained in the IWorksheet.
   * @return whether or not the IWorksheet contains the K coordinate.
   */
  boolean containsCell(K coord);

  /**
   * Returns the value at the given column and row as a formatted String.
   *
   * @param col the column to be referenced.
   * @param row the row to be referenced.
   * @return the value at the given coordinate as a formatted String.
   */
  String getValueAt(int col, int row);

  /**
   * Returns the value at the given column and row as a J.
   *
   * @param coord the coordinate to be evaluated.
   * @return the value at the given coordinate as a J
   */
  J getValueAt(K coord);

  /**
   * Gets the contents of the cell at the given coordinates as a string.
   *
   * @param col the column being referenced.
   * @param row the column being referenced.
   * @return the string representation of the cell being referenced.
   */
  String getContents(int col, int row);

  /**
   * Gets the contents of the cell at the given coordinates as a string.
   *
   * @param coord the coord being referenced
   * @return the string representation of the cell being referenced.
   */
  String getContents(K coord);

  /**
   * Returns the number of cells in the IWorksheet.
   *
   * @return the number of cells.
   */
  int numberOfCells();

  /**
   * Removes the cell from the specified column and row if it is present.
   *
   * @param col the column of the cell being referenced.
   * @param row the row of the cell being referenced.
   */
  void remove(int col, int row);

  /**
   * Returns a list of the coordinates which reference cells in the IWorksheet. That is, all of the
   * non-empty cells.
   *
   * @return the coordinates who referenced cells as a List.
   */
  List<K> nonEmptyCoords();


}
