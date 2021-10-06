package edu.cs3500.spreadsheets.controller;


import edu.cs3500.spreadsheets.view.SpreadsheetPanel;
import edu.cs3500.spreadsheets.view.ViewSpreadsheet;
import java.awt.event.MouseEvent;
import org.junit.Test;


import java.io.File;
import java.io.StringReader;
import java.util.Scanner;

import javax.swing.JTextField;


import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetICellBuilder;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.CellSelect;


import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link IWorksheetController} class and all related classes, such as the {@link
 * CellSelect} class.
 */
public class ControllerTest {

  @Test
  public void testMutateWorksheetSample11() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("4");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "4.000000");


  }

  @Test
  public void testMutateWorksheetSample12() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("5");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "5.000000");


  }

  @Test
  public void testMutateWorksheetDataTypeBoolean() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("false");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "false");


  }

  @Test
  public void testMutateWorksheetDataTypeString() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("\"false\"");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "\"false\"");


  }

  @Test
  public void testMutateWorksheetDataTypeSymbol() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("Symbol");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "Symbol");

  }

  @Test
  public void testMutateWorksheetDataTypeFunction() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(SUM 10 10)");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "20.000000");

  }

  @Test
  public void testMutateWorksheetDataTypeFunction2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(PRODUCT 10 10)");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "100.000000");

  }

  @Test
  public void testMutateWorksheetDataTypeFunction3() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(< 10 11)");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "true");

  }

  @Test
  public void testMutateWorksheetDataTypeFunction4() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(> 10 11)");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "false");

  }


  @Test
  public void testMutateWorksheetOverwriteCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "1");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("5");

    assertEquals(w.getValueAt(1, 1), "1.000000");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "5.000000");


  }

  @Test
  public void testMutateWorksheetPointToCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "1");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=A1");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 1), "1.000000");


  }

  @Test
  public void testMutateWorksheetPointToTwoCells() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "6");
    builder.createCell(1, 2, "3");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(SUM A1 A2)");

    controller.mutateWorksheet(1, 3, field);

    assertEquals(w.getValueAt(1, 3), "9.000000");


  }

  @Test
  public void testMutateWorksheetNestedFunction() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "6");
    builder.createCell(1, 2, "3");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(SUM A1 (PRODUCT 3 A2))");

    controller.mutateWorksheet(1, 3, field);

    assertEquals(w.getValueAt(1, 3), "15.000000");


  }

  @Test
  public void testMutateWorksheetListReference() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "1");
    builder.createCell(1, 2, "3");
    builder.createCell(1, 3, "=A1");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(SUM A1:A3)");

    controller.mutateWorksheet(1, 4, field);

    assertEquals(w.getValueAt(1, 4), "5.000000");


  }

  @Test
  public void testMutateWorksheetAddingMultiple() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("4");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "4.000000");

    field.setText("6");

    controller.mutateWorksheet(1, 2, field);

    assertEquals(w.getValueAt(1, 2), "6.000000");

    field.setText("false");

    controller.mutateWorksheet(1, 3, field);

    assertEquals(w.getValueAt(1, 3), "false");


  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateWorksheetCyclicalCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "=A2");
    builder.createCell(1, 2, "=A3");
    builder.createCell(1, 3, "1");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=A1");

    assertEquals(w.getValueAt(1, 1), "1.000000");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "1.000000");
    assertEquals(w.getValueAt(1, 3), "#ERROR");

  }


  @Test
  public void testMutateWorksheetFileWrite() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "testFile.txt");

    JTextField field = new JTextField();
    field.setText("5");

    controller.mutateWorksheet(1, 1, field);
    controller.saveWorksheet();

    File file =
        new File("testFile.txt");
    Scanner sc = null;
    try {
      sc = new Scanner(file);
    } catch (Exception e) {
      System.out.println("the test broke - no file found");
    }

    String fileString = "";
    while (sc.hasNextLine()) {
      fileString += sc.nextLine();
    }
    assertEquals(fileString, "A1 5");

  }

  @Test
  public void testMutateWorksheetFileWrite2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 2, "=(SUM 4 5)");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "testFile.txt");

    JTextField field = new JTextField();
    field.setText("5");

    controller.mutateWorksheet(1, 1, field);
    controller.saveWorksheet();

    File file =
        new File("testFile.txt");
    Scanner sc = null;
    try {
      sc = new Scanner(file);
    } catch (Exception e) {
      System.out.println("the test broke - no file found");
    }

    String fileString = "";
    while (sc.hasNextLine()) {
      fileString += sc.nextLine();
    }
    assertEquals(fileString, "A2 =(SUM 4 5)A1 5");

  }

  @Test
  public void testMutateWorksheetFileWriteRead() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 2, "=(SUM 4 5)");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "testFile.txt");

    JTextField field = new JTextField();
    field.setText("5");

    controller.mutateWorksheet(1, 1, field);
    controller.saveWorksheet();

    File fr = null;
    fr = new File("testFile.txt");

    Scanner sc = null;
    try {
      sc = new Scanner(fr);
    } catch (Exception e) {
      System.out.println("File did not load.");
    }
    String scf = "";

    while (sc.hasNext()) {
      scf += "" + sc.nextLine() + "\n";
    }

    //System.out.println(scf);

    WorksheetICellBuilder builder2 = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder2, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "5.000000");

  }

  @Test
  public void cellSelectMouseListenerTest() {

    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 2, "=(SUM 4 5)");

    Worksheet w = builder.createWorksheet();

    JTextField jt = new JTextField();

    CellSelect.deselect();

    CellSelect cs = new CellSelect(2, 1, jt, new ViewSpreadsheet(w), new SpreadsheetPanel(w));

    //since our event triggers on any mouse click,
    //the numbers and source can be anything, hence the random values.
    cs.mouseClicked(new MouseEvent(jt, 0, 0, 0, 0, 0, 1, false));

    assertEquals(1, CellSelect.getCol());
    assertEquals(2, CellSelect.getRow());
    assertEquals("=(SUM 4 5)", jt.getText());
  }

  @Test
  public void testMutateWorksheetFailState() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(> 10 11)");

    controller.mutateWorksheet(1, -1, field);

    assertEquals(w.getValueAt(1, 1), "");

  }

  @Test
  public void testMutateWorksheetFailState2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "9");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "");

  }

  @Test
  public void testMutateWorksheetFailStateBadInput1() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "9");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=AAAAAAAAAA");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "#ERROR");

  }

  @Test
  public void testMutateWorksheetFailStateBadInput2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "9");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=A-1");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "#ERROR");

  }

  @Test
  public void testMutateWorksheetFailStateBadInput3() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "9");

    Worksheet w = builder.createWorksheet();

    WorksheetController controller = new WorksheetController(w, "");

    JTextField field = new JTextField();
    field.setText("=(SUM 3 2");

    controller.mutateWorksheet(1, 1, field);

    assertEquals(w.getValueAt(1, 1), "#ERROR");

  }

  @Test
  public void cellSelectTests() {
    CellSelect select = new CellSelect(1, 1, null, null, null);
    assertEquals(CellSelect.getCol(), 0);
    assertEquals(CellSelect.getRow(), 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerFunctionalityFail() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    MockWorksheet mock = new MockWorksheet(w, null);
  }

  @Test
  public void testControllerFunctionality() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();

    Worksheet w = builder.createWorksheet();

    StringBuilder ap = new StringBuilder();

    MockWorksheet mock = new MockWorksheet(w, ap);

    IWorksheetController controller = new WorksheetController(mock, "");

    JTextField textField = new JTextField();
    textField.setText("=4");

    controller.mutateWorksheet(1, 1, textField);

    textField.setText("=A1");

    controller.mutateWorksheet(1, 2, textField);

    textField.setText("");

    controller.mutateWorksheet(1, 1, textField);

    assertEquals("Adding formula =4 at cordinate (1, 1)\n"
        + "Adding formula =A1 at cordinate (1, 2)\n"
        + "Removing cell at coordinate (1, 1)", ap.toString());

  }

}
