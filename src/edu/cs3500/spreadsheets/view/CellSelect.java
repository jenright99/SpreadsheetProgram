package edu.cs3500.spreadsheets.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

/**
 * A Mouse Listener which listens for mouse clicks on a cell in the {@link SpreadsheetPanel} and
 * communicates this selection to other classes. Utilizes the delegation pattern.
 */
public class CellSelect extends MouseAdapter {

  private static int row;
  private static int col;


  private int localRow;
  private int localCol;
  private JTextField input;
  private ISpreadsheetAdapter viewModel;
  private SpreadsheetPanel view;

  /**
   * Instantiates the CellSelect with the given fields.
   *
   * @param localRow  the row of the cell CellSelect is referncing
   * @param localCol  the column of the cell CellSelect is referencing
   * @param input     the {@link JTextField} to update with the new cell contents.
   * @param viewModel a view only version of the spreadsheet to get cell values from.
   * @param view      the {@link SpreadsheetPanel} which needs to be refreshed to reflect the new
   *                  data.
   */
  public CellSelect(int localRow, int localCol, JTextField input,
      ISpreadsheetAdapter viewModel, SpreadsheetPanel view) {
    this.localRow = localRow;
    this.localCol = localCol;
    this.input = input;
    this.viewModel = viewModel;
    this.view = view;
  }

  /**
   * Returns the row of the currently selected cell.
   *
   * @return the row of the currently selected cell.
   */
  public static int getRow() {
    return CellSelect.row;
  }

  /**
   * Returns the column of the currently selected cell.
   *
   * @return the column of the currently selected cell.
   */
  public static int getCol() {
    return CellSelect.col;
  }

  /**
   * Deselects the currently selected cell, setting both the selected row and column to 0.
   */
  public static void deselect() {
    CellSelect.row = 0;
    CellSelect.col = 0;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    CellSelect.row = this.localRow;
    CellSelect.col = this.localCol;
    if (input != null && viewModel != null) {
      input.setText(viewModel.getContents(this.localCol, this.localRow));
    }
    this.view.renderView();
  }
}

