package edu.cs3500.spreadsheets.controller;

import edu.cs3500.spreadsheets.model.IWorksheet;
import edu.cs3500.spreadsheets.view.CellSelect;
import edu.cs3500.spreadsheets.view.IWorksheetEditableView;
import edu.cs3500.spreadsheets.view.ViewSpreadsheet;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JTextField;

/**
 * An implementation of the {@link IWorksheetController} using an {@link WorksheetEditableView} to
 * visually represent the {@link IWorksheet} model.
 */
public class WorksheetController implements IWorksheetController {

  private IWorksheet model;
  private IWorksheetEditableView view;
  private String file;

  /**
   * Instantiates a WorksheetController and creates an editable view for the model.
   *
   * @param model The {@link IWorksheet} model used by the controller.
   */
  public WorksheetController(IWorksheet model, String file) {
    this.model = model;
    this.view = new WorksheetEditableView(this.model);
    this.file = file;
  }

  @Override
  public void mutateWorksheet(int col, int row, JTextField text) {
    if (col > 0 && row > 0) {
      if (text.getText().equals("")) {
        this.model.remove(col, row);
      } else {
        try {
          this.model.addCell(col, row, text.getText());
        } catch (IllegalArgumentException ie) {
          System.out.println("Error: Badly Formated Formula!");
        }
        text.setText("");
      }
      CellSelect.deselect();
      this.renderEditableView();
    }
  }

  @Override
  public void saveWorksheet() {
    try {
      FileWriter fr = new FileWriter(file);
      PrintWriter printWriter = new PrintWriter(fr);
      WorksheetTextualView textualView = new WorksheetTextualView(new ViewSpreadsheet(this.model),
          printWriter);
      textualView.renderView();
      printWriter.close();
    } catch (IOException io) {
      System.out.println("Could not save file :(");
    }
  }

  /**
   * Renders the editable view, accouting for any errors thrown.
   */
  private void renderEditableView() {
    //TODO this needs to improve.
    try {
      view.renderView();
    } catch (Exception e) {
      throw new IllegalArgumentException("Bad View");
    }
  }



  @Override
  public void run() {
    view.addController(this);
    this.renderEditableView();
  }

}
