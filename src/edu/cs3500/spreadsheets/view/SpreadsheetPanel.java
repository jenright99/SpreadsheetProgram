package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.IWorksheet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Generates a {@link JPanel} which reflects the provided {@link IWorksheet}. Utilises {@link
 * CellSelect} to track mouse clicks via the feature pattern.
 */
public class SpreadsheetPanel extends JPanel implements ActionListener {


  private ISpreadsheetAdapter worksheet;
  private final int CELLWIDTH = 60;
  private final int CELLHEIGHT = 25;
  private final int COLUMNS = 10;
  private final int ROWS = 10;
  private int scrollVertical;
  private int scrollHorizontal;
  public JButton rightButton;
  public JButton leftButton;
  public JButton upButton;
  public JButton downButton;
  private JPanel cellGrid;
  private JPanel buttons;
  private JTextField input;
  private IWorksheetView view;

  /**
   * Constructs the SpreadsheetPanel with the values of the given {@link IWorksheet}.
   *
   * @param worksheet the {@link IWorksheet} to base the values in the spreadsheet on.
   * @param input     a {@link JTextField} from a {@link IWorksheetEditableView} which may need to
   *                  be updated based on user selections.
   */
  public SpreadsheetPanel(IWorksheet worksheet, JTextField input) {
    this.worksheet = new ViewSpreadsheet(worksheet);
    this.input = input;
    this.scrollVertical = 1;
    this.scrollHorizontal = 1;

    this.cellGrid = new JPanel();
    cellGrid.setPreferredSize(new Dimension(CELLWIDTH * 11, CELLHEIGHT * 11));
    cellGrid.setLayout(new GridLayout(ROWS + 1, COLUMNS + 1));
    this.getCellGridPanel();
    buttons = new JPanel();
    this.setLayout(new BorderLayout(0, 0));
    this.add(BorderLayout.CENTER, this.cellGrid);

    rightButton = new JButton("Right");
    rightButton.addActionListener(this);

    leftButton = new JButton("Left");
    leftButton.addActionListener(this);

    upButton = new JButton("Up");
    upButton.addActionListener(this);

    downButton = new JButton("Down");
    downButton.addActionListener(this);

    buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttons.add(upButton);
    buttons.add(downButton);
    buttons.add(leftButton);
    buttons.add(rightButton);

    cellGrid.revalidate();

    this.add(BorderLayout.SOUTH, buttons);
  }

  /**
   * Creates a Spreadsheet Panel without an input text field.
   *
   * @param worksheet the {@link IWorksheet} to base the values in the spreadsheet on.
   */
  public SpreadsheetPanel(IWorksheet worksheet) {
    this(worksheet, null);
  }

  /**
   * Updates the values of the cellGrid, refreshing each cell and applying the vertical and
   * Horizontal offset.
   */
  private void getCellGridPanel() {

    JPanel blank = new JPanel();
    blank.setOpaque(true);
    Border blackBorder = BorderFactory.createLineBorder(Color.BLACK);
    blank.setBorder(blackBorder);
    cellGrid.add(blank);

    for (int i = 0; i < COLUMNS; i++) {
      JPanel temp = new JPanel();
      temp.add(new JLabel(Coord.colIndexToName(scrollHorizontal + i)));
      temp.setBorder(blackBorder);
      cellGrid.add(temp);
    }

    for (int row = 0; row < ROWS; row++) {
      JPanel temp = new JPanel();
      temp.add(new JLabel("" + (scrollVertical + row)));
      temp.setBorder(blackBorder);

      cellGrid.add(temp);
      for (int col = 0; col < COLUMNS; col++) {
        JPanel cell = renderCell(scrollHorizontal + col, scrollVertical + row);

        cell.addMouseListener(
            new CellSelect(row + this.scrollVertical, col + this.scrollHorizontal, this.input,
                this.worksheet, this));
        cellGrid.add(cell);
      }
    }

  }

  /**
   * Scrolls the view horizontally by the given number of cells. Insures scrollHorizontal is always
   * greater than or equal to one.
   *
   * @param change The value to scroll horizontally by.
   */
  private void horizontalButton(int change) {
    if (scrollHorizontal + change >= 1) {
      scrollHorizontal += change;
    }
  }

  /**
   * Scrolls the view vertically by the given number of cells. Insures scrollVertical is always
   * greater than or equal to one.
   *
   * @param change The value to scroll vertically by.
   */
  private void verticalButton(int change) {
    if (scrollVertical + change >= 1) {
      scrollVertical += change;
    }
  }

  /**
   * Creates a JPanel representation of the value in the referenced cell, putting #ERROR in the cell
   * if it cannot be referenced.
   *
   * @param col the column of the cell being referenced.
   * @param row the row of the cell being referenced.
   * @return a JPanel representation of that cell.
   */
  private JPanel renderCell(int col, int row) {
    String cellContents;
    try {
      cellContents = this.worksheet.getValueAt(col, row);
    } catch (Exception e) {
      cellContents = "#ERROR";
    }
    JLabel newLabel = new JLabel(cellContents);
    JPanel newPanel = new JPanel();
    newPanel.add(newLabel, BorderLayout.CENTER);
    newPanel.setPreferredSize(new Dimension(CELLWIDTH, CELLHEIGHT));
    Border newBorder = BorderFactory.createLineBorder(Color.BLACK);
    if (col == CellSelect.getCol() && row == CellSelect.getRow()) {
      newPanel.setBackground(Color.LIGHT_GRAY);
      newPanel.setOpaque(true);
    }
    newPanel.setBorder(newBorder);
    return newPanel;
  }

  /**
   * Refreshes the SpreadsheetPanel, updating all the cells in the cellGrid.
   */
  public void renderView() {
    this.cellGrid.removeAll();

    this.getCellGridPanel();

    this.add(this.cellGrid, BorderLayout.CENTER);

    cellGrid.revalidate();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(rightButton)) {
      this.horizontalButton(1);
    } else if (e.getSource().equals(leftButton)) {
      this.horizontalButton(-1);
    } else if (e.getSource().equals(upButton)) {
      this.verticalButton(-1);
    } else if (e.getSource().equals(downButton)) {
      this.verticalButton(1);
    }

    this.cellGrid.removeAll();

    this.getCellGridPanel();

    this.add(this.cellGrid, BorderLayout.CENTER);

    cellGrid.revalidate();
    //this.repaint();

  }
}
