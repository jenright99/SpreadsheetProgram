package edu.cs3500.spreadsheets.view;

import java.io.IOException;
import java.util.List;

/**
 * A textual view of an {@link ISpreadsheetAdapter}, which prints to a provided Appendable.
 *
 * @param <K> The Object the {@link ISpreadsheetAdapter} uses to track coordinates.
 */
public class WorksheetTextualView<K> implements IWorksheetView {

  private ISpreadsheetAdapter<K> worksheet;
  private Appendable ap;

  /**
   * Instantiates the SpreadsheetAdapter and Appendable, throwing an error if the Appendable is
   * null.
   *
   * @param worksheet The {@link ISpreadsheetAdapter} to display.
   * @param ap        the Appendable to append to.
   */
  public WorksheetTextualView(ISpreadsheetAdapter<K> worksheet, Appendable ap) {
    if (ap == null) {
      throw new IllegalArgumentException("Appendable cannot be null!");
    }
    this.worksheet = worksheet;
    this.ap = ap;
  }

  @Override
  public void renderView() {
    try {
      this.ap.append(this.toString());
    } catch (IOException io) {
      //Do nothing
    }
  }

  @Override
  public String toString() {
    List<K> coords = this.worksheet.nonEmptyCoords();

    String result = "";
    for (K c : coords) {
      result = result + c.toString() + " " + this.worksheet.getContents(c) + "\n";
    }

    result = result.stripTrailing();

    return result;
  }
}
