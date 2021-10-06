package edu.cs3500.spreadsheets.view;

import org.junit.Test;

import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetICellBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link WorksheetTextualView} class to ensure it functions as intended.
 */
public class WorksheetTextualViewTest {

  @Test
  public void AllCellRenderTest1() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "1");
    builder.createCell(2, 1, "false");
    builder.createCell(1, 2, "\"String\"");
    builder.createCell(2, 2, "=(SUM 3 4)");

    Worksheet w = builder.createWorksheet();
    ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(w);

    StringBuilder sbuilder = new StringBuilder();

    WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, sbuilder);

    textView.renderView();

    assertEquals(sbuilder.toString(), "A2 \"String\"\n" +
        "A1 1\n" +
        "B2 =(SUM 3 4)\n" +
        "B1 false");
  }

  @Test
  public void AllCellRenderTestFunction() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "=(PRODUCT 3 4)");
    builder.createCell(2, 1, "=(> 3 4)");
    builder.createCell(1, 2, "=(< 3 4)");
    builder.createCell(2, 2, "=(SUM 3 4)");

    Worksheet w = builder.createWorksheet();
    ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(w);

    StringBuilder sbuilder = new StringBuilder();

    WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, sbuilder);

    textView.renderView();

    assertEquals(sbuilder.toString(), "A2 =(< 3 4)\n" +
        "A1 =(PRODUCT 3 4)\n" +
        "B2 =(SUM 3 4)\n" +
        "B1 =(> 3 4)");
  }

  @Test
  public void AllCellRenderTestNoCells() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();
    ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(w);

    StringBuilder sbuilder = new StringBuilder();

    WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, sbuilder);

    textView.renderView();

    assertEquals(sbuilder.toString(), "");
  }

  @Test
  public void testToString() {

    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "=(PRODUCT 3 4)");
    builder.createCell(2, 1, "=(> 3 4)");
    builder.createCell(1, 2, "=(< 3 4)");
    builder.createCell(2, 2, "=(SUM 3 4)");

    Worksheet w = builder.createWorksheet();
    ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(w);

    StringBuilder sbuilder = new StringBuilder();

    WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, sbuilder);

    assertEquals(textView.toString(), "A2 =(< 3 4)\n" +
        "A1 =(PRODUCT 3 4)\n" +
        "B2 =(SUM 3 4)\n" +
        "B1 =(> 3 4)");
  }

  @Test
  public void testToStringNoCells() {

    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();
    ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(w);

    StringBuilder sbuilder = new StringBuilder();

    WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, sbuilder);

    assertEquals(textView.toString(), "");
  }
}