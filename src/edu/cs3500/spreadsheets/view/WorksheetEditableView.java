package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.IWorksheetController;
import edu.cs3500.spreadsheets.model.IWorksheet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * An implementation of {@link IWorksheetEditableView} which displays the spreadsheet and allows the
 * user to edit the model through a controller.
 */
public class WorksheetEditableView extends JFrame implements IWorksheetEditableView {

  private JButton accept;
  private JButton reject;
  private JButton save;
  private JTextField input;
  private JPanel inputBar;
  private SpreadsheetPanel spreadsheet;
  private ISpreadsheetAdapter viewModel;

  /**
   * Instantiates the WorksheetEditableView with the given {@link IWorksheet}, utilizing the {@link
   * SpreadsheetPanel} to reflect the spreadsheet.
   *
   * @param worksheet the {@link IWorksheet} to base the values in the spreadsheet on.
   */
  public WorksheetEditableView(IWorksheet worksheet) {
    this.setLayout(new BorderLayout());
    this.viewModel = new ViewSpreadsheet(worksheet);

    inputBar = new JPanel();
    inputBar.setLayout(new FlowLayout());

    accept = new JButton("Accept");
    //accept.addActionListener(this);

    reject = new JButton("Reject");
    //clear.addActionListener(this);

    save = new JButton("Save");

    input = new JTextField();
    input.setText("");
    input.setPreferredSize(new Dimension(500, 30));

    inputBar.add(accept);
    inputBar.add(reject);
    inputBar.add(input);
    inputBar.add(save);

    this.add(inputBar, BorderLayout.NORTH);
    this.spreadsheet = new SpreadsheetPanel(worksheet, this.input);
    this.add(this.spreadsheet, BorderLayout.SOUTH);
    this.pack();

  }


  @Override
  public void renderView() {

    this.spreadsheet.renderView();
    this.setVisible(true);

  }


  @Override
  public void addController(IWorksheetController controller) {
    this.reject.addActionListener(evt -> {
      if (CellSelect.getCol() > 0 && CellSelect.getRow() > 0) {
        this.input.setText(this.viewModel.getContents(CellSelect.getCol(), CellSelect.getRow()));
      }
    });
    this.accept.addActionListener(
        evt -> controller.mutateWorksheet(CellSelect.getCol(), CellSelect.getRow(), this.input));
    this.save.addActionListener(evt -> controller.saveWorksheet());
  }
}

