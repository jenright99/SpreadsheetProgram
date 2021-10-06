package edu.cs3500.spreadsheets.model;

import edu.cs3500.spreadsheets.model.WorksheetReader.WorksheetBuilder;

/**
 * A {@link WorksheetBuilder} which builds a {@link Worksheet}.
 */
public class WorksheetICellBuilder implements WorksheetBuilder<Worksheet> {


  private Worksheet worksheet;

  /**
   * Initializes the {@link Worksheet}.
   */
  public WorksheetICellBuilder() {
    this.worksheet = new Worksheet();
  }

  /**
   * Creates a WorksheetICellBuilder with the given Worksheet.
   *
   * @param worksheet the {@link Worksheet} to build.
   */
  private WorksheetICellBuilder(Worksheet worksheet) {
    this.worksheet = worksheet;
  }

  @Override
  public WorksheetBuilder<Worksheet> createCell(int col, int row, String contents) {
    this.worksheet.addCell(col, row, contents);
    return new WorksheetICellBuilder(this.worksheet);
  }

  @Override
  public Worksheet createWorksheet() {
    return this.worksheet;
  }
}
