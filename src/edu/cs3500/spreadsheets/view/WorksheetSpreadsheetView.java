package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.IWorksheet;
import javax.swing.JFrame;

/**
 * Creates a GUI representation of an IWorksheet. Utilizes the {@link ViewSpreadsheet} class to view
 * the IWorksheet. Uses the {@link SpreadsheetPanel} class to display the spreadsheet.
 */
public class WorksheetSpreadsheetView extends JFrame implements IWorksheetView {

  public WorksheetSpreadsheetView(IWorksheet model) {
    this.add(new SpreadsheetPanel(model));
    this.pack();
  }

  @Override
  public void renderView() {

    this.setVisible(true);
    this.pack();
  }
}
