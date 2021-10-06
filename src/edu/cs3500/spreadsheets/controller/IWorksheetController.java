package edu.cs3500.spreadsheets.controller;

import javax.swing.JTextField;

/**
 * A controller for an {@link edu.cs3500.spreadsheets.model.IWorksheet} which utilizes the feature
 * pattern.
 */
public interface IWorksheetController {

  /**
   * Mutates the IWorksheet with the given values.
   *
   * @param col  the column of the cell being mutated.
   * @param row  the row of the cell being mutated.
   * @param text a {@link JTextField} where the user has input data.
   */
  void mutateWorksheet(int col, int row, JTextField text);

  /**
   * Saves the IWorksheet to some appendable using the
   * {@link edu.cs3500.spreadsheets.view.WorksheetTextualView}.
   */
  void saveWorksheet();


  /**
   * Runs the controller, giving the user a visual representation of the {@link
   * edu.cs3500.spreadsheets.model.IWorksheet} and the ability to edit the worksheet.
   */
  void run();

}
