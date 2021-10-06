package edu.cs3500.spreadsheets.model;

import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.cs3500.spreadsheets.model.operations.GreaterThanOperation;
import edu.cs3500.spreadsheets.model.operations.LessThanOperation;
import edu.cs3500.spreadsheets.model.operations.ProductOperation;
import edu.cs3500.spreadsheets.model.operations.SumOperation;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SList;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.Sexp;

import static org.junit.Assert.assertEquals;

/**
 * This class serves the function of containing and running test methods for the IWorksheet
 * interface, as well as its associated methods, classes, and interfaces.
 */
public class IWorksheetTest {


  @Test
  public void TestEmptySpreadsheet() {
    HashMap<Coord, ICell> map = new HashMap<>(2);
    Worksheet sheet = new Worksheet();

    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet w = new Worksheet();

    assertEquals(w.numberOfCells(), sheet.numberOfCells());
  }

  @Test
  public void testAllCellsThereBuilder() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "1");
    builder.createCell(2, 1, "false");
    builder.createCell(1, 2, "\"String\"");
    builder.createCell(2, 2, "=(SUM 3 4)");

    Worksheet w = builder.createWorksheet();

    assertEquals(w.getValueAt(1, 1), "1.000000");
    assertEquals(w.getValueAt(1, 2), "\"String\"");
    assertEquals(w.getValueAt(2, 1), "false");
    assertEquals(w.getValueAt(2, 2), "7.000000");


  }

  @Test
  public void testAllCellsThereReader() {

    String scf = new String("A1 1\nA2 \"String\"\nB1 false\nB2 =(SUM 3 4)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet w = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(w.getValueAt(1, 1), "1.000000");
    assertEquals(w.getValueAt(1, 2), "\"String\"");
    assertEquals(w.getValueAt(2, 1), "false");
    assertEquals(w.getValueAt(2, 2), "7.000000");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSelfReferential1() {
    String scf = new String("A1 =A2\nA2 =B1\nB1 =B2\nB2 =A1");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet w = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(w.getValueAt(1, 1), "1.000000");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSelfReferential2() {
    String scf = new String("A1 =A1");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet w = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(w.getValueAt(1, 1), "1.000000");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSelfReferential3() {
    String scf = new String("A1 1\nA2 \"String\"\nB1 false\nB2 =A1:B2");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet w = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(w.getValueAt(2, 2), "1.000000");

  }


  @Test
  public void cellContentsBlank() {
    IdentityFormula formula1 = new IdentityFormula(new SSymbol(""));
    String scf = new String("A1 =A2");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 2), "");
  }

  @Test
  public void cellContentsString() {
    String scf = new String("A1 \"Wow\"");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "\"Wow\"");
  }

  @Test
  public void cellContentsNumber() {
    String scf = new String("A1 4");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "4.000000");
  }

  @Test
  public void cellContentsBoolean() {
    String scf = new String("A1 false");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void cellContentsBooleanT() {
    String scf = new String("A1 true");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "true");
  }

  @Test
  public void cellContentsSymbol() {
    String scf = new String("A1 SUM");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "SUM");
  }

  @Test
  public void cellContentsNestedFormula() {
    String scf = new String("A1 =(SUM 2 (PRODUCT 2 3))");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "8.000000");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cellContentsFailedFormula() {
    String scf = new String("A1 =(GREER 2 3 4)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));
  }

  @Test
  public void cellContentsSumFormula() {
    String scf = new String("A1 =(SUM 2 4)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "6.000000");
  }

  @Test
  public void cellContentsProductFormula() {
    String scf = new String("A1 =(PRODUCT 8 3)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "24.000000");
  }

  @Test
  public void cellContentsGreaterThanFormula() {
    String scf = new String("A1 =(> 8 3)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "true");
  }

  @Test
  public void cellContentsGreaterThanFormula2() {
    String scf = new String("A1 =(> 8 9)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void cellContentsGreaterThanFormula3() {
    String scf = new String("A1 =(> 8 8)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void cellContentsLessThanFormula1() {
    String scf = new String("A1 =(< 8 10)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "true");
  }

  @Test
  public void cellContentsLessThanFormula2() {
    String scf = new String("A1 =(< 8 6)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void cellContentsLessThanFormula3() {
    String scf = new String("A1 =(< 8 8)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void workSheetReferences1() {
    String scf = new String("A1 4\nA2 5\nB1 =(SUM A1 A2)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "9.000000");
  }

  @Test
  public void workSheetReferencesSameInput() {
    String scf = new String("A1 4\nA2 5\nB1 =(SUM A1 A1)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "8.000000");
  }

  @Test
  public void workSheetReferencesBadTypeInput() {
    String scf = new String("A1 4\nA2 5\nB1 =(SUM true A1)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "4.000000");
  }

  @Test
  public void workSheetReferencesBadTypeInput2() {
    String scf = new String("A1 4\nA2 5\nB1 =(SUM \"String\" A1)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "4.000000");
  }

  @Test
  public void workSheetReferencesListInput() {
    String scf = new String("A1 4\nA2 5\nA3 6\nB1 =(SUM A1:A3)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "15.000000");
  }

  @Test
  public void workSheetReferencesIndirect() {
    String scf = new String("A1 =(A3:A5)\nA2 5\nB1 =(SUM A1 A2)\nA3 1\nA4 2\nA5 3");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "11.000000");
  }


  @Test(expected = IllegalArgumentException.class)
  public void workSheetBadlyFormattedInput() {
    String scf = new String("A1 =(A3:A5");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(2, 1), "11.000000");
  }

  @Test(expected = IllegalArgumentException.class)
  public void workSheetBadlyFormattedInputMispelling() {
    String scf = new String("A1 =(SUMM 4 5)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

  }

  @Test(expected = IllegalStateException.class)
  public void workSheetBadlyFormattedInputNoCell() {
    String scf = new String("AA =(SUM 4 5)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

  }

  @Test(expected = IllegalStateException.class)
  public void workSheetBadlyFormattedInputNoCell2() {
    String scf = new String("A 2 =(SUM 4 5)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

  }

  @Test
  public void worksheetLargeCellNumberInput() {
    String scf = new String("AA11 =(< 8 6)");
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

    assertEquals(worksheet.getValueAt(27, 11), "false");
  }

  @Test
  public void worksheetAddCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    assertEquals(worksheet.getValueAt(1, 1), "");

    worksheet.addCell(1, 1, "true");

    assertEquals(worksheet.getValueAt(1, 1), "true");
  }

  @Test
  public void worksheetAddCellReferential() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "1");
    worksheet.addCell(1, 2, "2");
    worksheet.addCell(1, 3, "3");
    worksheet.addCell(1, 4, "=A1:A3");

    assertEquals(worksheet.getValueAt(1, 4), "1.000000");
  }


  @Test(expected = IllegalArgumentException.class)
  public void worksheetAddCellSelfReferential() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "1");
    worksheet.addCell(1, 2, "2");
    worksheet.addCell(1, 3, "3");
    worksheet.addCell(1, 4, "=A1:A4");

    assertEquals(worksheet.getValueAt(1, 4), "1.000000");
  }

  @Test
  public void worksheetContainsCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();
    worksheet.addCell(1, 1, "false");
    assertEquals(worksheet.containsCell(new Coord(1, 1)), true);

    assertEquals(worksheet.containsCell(new Coord(1, 2)), false);

    worksheet.addCell(1, 2, "1");

    assertEquals(worksheet.containsCell(new Coord(1, 2)), true);
  }

  @Test
  public void worksheetGetValueAtVersions() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();
    worksheet.addCell(1, 1, "false");

    assertEquals(worksheet.getValueAt(new Coord(1, 1)), new SBoolean(false));

    assertEquals(worksheet.getValueAt(1, 1), "false");
  }

  @Test
  public void worksheetGetContents() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();
    worksheet.addCell(1, 1, "=(SUM 4 2)");

    assertEquals(worksheet.getContents(1, 1), "=(SUM 4 2)");
  }

  @Test
  public void worksheetGetContents2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();
    worksheet.addCell(1, 1, "=(SUM 4 (PRODUCT 2 (SUM 4 5)))");

    assertEquals(worksheet.getContents(1, 1), "=(SUM 4 (PRODUCT 2 (SUM 4 5)))");
  }

  @Test
  public void worksheetGetContentsNoCell() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();
    worksheet.addCell(1, 1, "Hi");

    assertEquals(worksheet.getContents(1, 2), "");
  }

  @Test
  public void worksheetTestNumberOfCells() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    assertEquals(worksheet.numberOfCells(), 0);

    worksheet.addCell(1, 1, "Hi");

    assertEquals(worksheet.numberOfCells(), 1);

    worksheet.addCell(1, 2, "Hi");
    worksheet.addCell(1, 3, "Hi");

    assertEquals(worksheet.numberOfCells(), 3);
  }


  @Test
  public void worksheetTestRemove() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "Hi");
    worksheet.addCell(1, 2, "Hello");
    worksheet.addCell(1, 3, "Hey");

    assertEquals(worksheet.numberOfCells(), 3);

    worksheet.remove(1, 1);

    assertEquals(worksheet.numberOfCells(), 2);
    assertEquals(worksheet.getValueAt(1, 1), "");

  }

  @Test
  public void worksheetTestRemoveNotExist() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "Hi");
    worksheet.addCell(1, 2, "Hello");
    worksheet.addCell(1, 3, "Hey");

    worksheet.remove(1, 4);

    assertEquals(worksheet.numberOfCells(), 3);
    assertEquals(worksheet.getValueAt(1, 4), "");

  }

  @Test
  public void worksheetTestFarAwayValue() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "Hi");
    worksheet.addCell(4000, 4000, "Hey");

    assertEquals(worksheet.getValueAt(4000, 4000), "Hey");

  }


  @Test
  public void worksheetTestOverwrite() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "Hi");
    worksheet.addCell(1, 1, "Hey");

    assertEquals(worksheet.getValueAt(1, 1), "Hey");

  }

  @Test
  public void worksheetTestOverwrite2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    Worksheet worksheet = builder.createWorksheet();

    worksheet.addCell(1, 1, "Hi");
    worksheet.addCell(1, 1, "2");

    assertEquals(worksheet.getValueAt(1, 1), "2.000000");

  }


  @Test
  public void testFunctionFormulaEvaluate1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(4.0)));
    fList.add(new IdentityFormula((new SNumber(5.0))));
    assertEquals(new FunctionFormula(new SumOperation(), fList).evaluate(), new SNumber(9.0));
  }

  @Test
  public void testFunctionFormulaEvaluate2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(9.0)));
    fList.add(new IdentityFormula((new SNumber(5.0))));
    assertEquals(new FunctionFormula(new SumOperation(), fList).evaluate(), new SNumber(14.0));
  }

  @Test
  public void testFunctionFormulaEvaluate3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(9.0)));
    fList.add(new IdentityFormula((new SNumber(5.0))));
    assertEquals(new FunctionFormula(new ProductOperation(), fList).evaluate(), new SNumber(45.0));
  }

  @Test
  public void testFunctionFormulaEvaluate4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(9.0)));
    fList.add(new IdentityFormula((new SNumber(5.0))));
    assertEquals(new FunctionFormula(new LessThanOperation(), fList).evaluate(),
        new SBoolean(false));
  }

  @Test
  public void testFunctionFormulaEvaluate5() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(9.0)));
    fList.add(new IdentityFormula((new SNumber(5.0))));
    assertEquals(new FunctionFormula(new GreaterThanOperation(), fList).evaluate(),
        new SBoolean(true));
  }

  @Test
  public void testIdentityFormulaEvaluate() {
    IdentityFormula f = new IdentityFormula(new SString("Wow"));
    assertEquals(f.evaluate(), new SString("Wow"));
    IdentityFormula f2 = new IdentityFormula(new SNumber(4.0));
    assertEquals(f2.evaluate(), new SNumber(4.0));
    IdentityFormula f3 = new IdentityFormula(new SBoolean(false));
    assertEquals(f3.evaluate(), new SBoolean(false));
    IdentityFormula f4 = new IdentityFormula(new SSymbol("Wow"));
    assertEquals(f4.evaluate(), new SSymbol("Wow"));
  }


  @Test
  public void testSumOperationApply4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SBoolean(false)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testGreaterThanOperationApply() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test
  public void testGreaterThanOperationApply2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(5.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test
  public void testGreaterThanOperationApply3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(7.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationApplyFail1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SBoolean(false)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationApplyFail2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SString("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationApplyFail3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationApplyFail4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    assertEquals(new GreaterThanOperation().apply(fList), new SBoolean(false));
  }

  @Test
  public void testLessThanOperationApply() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test
  public void testLessThanOperationApply2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(5.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(true));
  }

  @Test
  public void testLessThanOperationApply3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(7.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationApplyFail1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SBoolean(false)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationApplyFail2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SString("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationApplyFail3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationApplyFail4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    assertEquals(new LessThanOperation().apply(fList), new SBoolean(false));
  }

  @Test
  public void testProductOperationApply1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(36.0));
  }

  @Test
  public void testProductOperationApply2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(5.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(30.0));
  }

  @Test
  public void testProductOperationApply3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(7.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(42.0));
  }

  @Test
  public void testProductOperationApplyFail1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SBoolean(false)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testProductOperationApplyFail2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SString("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testProductOperationApplyFail3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new ProductOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testProductOperationApplyFail4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    assertEquals(new ProductOperation().apply(fList), new SNumber(6.0));
  }


  @Test
  public void testSumOperationApply1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(4.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(10.0));
  }

  @Test
  public void testSumOperationApply2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(-1.0)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(5.0));
  }

  @Test
  public void testSumOperationApply3() {
    ArrayList<IFormula> fListP = new ArrayList<>(3);
    fListP.add(new IdentityFormula(new SNumber(2.0)));
    fListP.add(new IdentityFormula(new SNumber(6.0)));

    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new FunctionFormula(new ProductOperation(), fListP));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(18.0));
  }


  @Test
  public void testSumOperationApplyFail1() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SBoolean(false)));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testSumOperationApplyFail2() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SString("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testSumOperationApplyFail3() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    fList.add(new IdentityFormula(new SNumber(6.0)));
    assertEquals(new SumOperation().apply(fList), new SNumber(6.0));
  }

  @Test
  public void testSumOperationApplyFail4() {
    ArrayList<IFormula> fList = new ArrayList<>(3);
    fList.add(new IdentityFormula(new SNumber(6.0)));
    fList.add(new IdentityFormula(new SSymbol("Wow")));
    assertEquals(new SumOperation().apply(fList), new SNumber(6.0));
  }


  @Test
  public void TestCellEvaluate() {
    ArrayList<IFormula> list1 = new ArrayList<IFormula>();
    list1.add(new IdentityFormula(new SNumber(1)));
    list1.add(new IdentityFormula(new SNumber(2)));
    FunctionFormula formula1 = new FunctionFormula(new SumOperation(), list1);
    Cell cell1 = new Cell(formula1);
    assertEquals(cell1.evaluate(), new SNumber(3.0));
  }

  @Test
  public void TestCellEvaluate2() {
    IdentityFormula formula1 = new IdentityFormula(new SSymbol("Symbol"));
    Cell cell1 = new Cell(formula1);
    assertEquals(cell1.evaluate(), new SSymbol("Symbol"));
  }

  @Test
  public void TestCellEvaluate3() {
    IdentityFormula formula1 = new IdentityFormula(new SString("Symbol"));
    Cell cell1 = new Cell(formula1);
    assertEquals(cell1.evaluate(), new SString("Symbol"));
  }

  @Test
  public void testCellReferencedCoords() {

    Cell testCell = new Cell(new IdentityFormula(new SSymbol("SUM")));

    assertEquals(testCell.evaluate(), new SSymbol("SUM"));

    Cell testCell2 = new Cell(new IdentityFormula(new SBoolean(false)));

    assertEquals(testCell2.evaluate(), new SBoolean(false));

    Cell testCell3 = new Cell(new IdentityFormula(new SString("String")));

    assertEquals(testCell3.evaluate(), new SString("String"));

  }


  @Test
  public void testFormulaCreatorStringToCoord() {
    List<Coord> list = FormulaCreator.stringToCoord("A2");
    assertEquals(list.get(0), new Coord(1, 2));

    List<Coord> list2 = FormulaCreator.stringToCoord("A1:A3");
    assertEquals(list2.get(0), new Coord(1, 1));
    assertEquals(list2.get(1), new Coord(1, 2));
    assertEquals(list2.get(2), new Coord(1, 3));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaCreatorStringToCoord2() {
    List<Coord> list = FormulaCreator.stringToCoord("AAAAA");
    assertEquals(list.get(0), new Coord(1, 2));
  }

  @Test
  public void testFormulaCreatorStringToCoord3() {
    List<Coord> list = FormulaCreator.stringToCoord("A1:B2");
    assertEquals(list.get(0), new Coord(1, 1));
    assertEquals(list.get(1), new Coord(1, 2));
    assertEquals(list.get(2), new Coord(2, 1));

    List<Coord> list2 = FormulaCreator.stringToCoord("a1:a3");
    assertEquals(list2.get(0), new Coord(1, 1));
    assertEquals(list2.get(1), new Coord(1, 2));
    assertEquals(list2.get(2), new Coord(1, 3));

  }


  @Test
  public void testFormulaCreatorIndexOfDigit() {
    int indexDigit = FormulaCreator.indexOfDigit("A1");
    int col = Coord.colNameToIndex("A1".substring(0, indexDigit));
    int row = Integer.parseInt("A1".substring(indexDigit));
    assertEquals(col, 1);
    assertEquals(row, 1);
  }

  @Test
  public void testFormulaCreatorIndexOfDigit2() {
    int indexDigit = FormulaCreator.indexOfDigit("AB111111");
    int col = Coord.colNameToIndex("AB111111".substring(0, indexDigit));
    int row = Integer.parseInt("AB111111".substring(indexDigit));
    assertEquals(col, 28);
    assertEquals(row, 111111);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaCreatorIndexOfDigitFail() {
    int indexDigit = FormulaCreator.indexOfDigit("A1:A2");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaCreatorIndexOfDigitFail2() {
    int indexDigit = FormulaCreator.indexOfDigit("@1");

  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormulaCreatorIndexOfDigitFail3() {
    int indexDigit = FormulaCreator.indexOfDigit("AAAAA");

  }

  @Test
  public void testFormulaCreatorVisitBoolean() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4.0");
    Worksheet ws = builder.createWorksheet();
    FormulaCreator f1 = new FormulaCreator(ws);
    assertEquals(f1.visitBoolean(true).evaluate(), new SBoolean(true));

    assertEquals(f1.visitBoolean(false).evaluate(), new SBoolean(false));
  }

  @Test
  public void testFormulaCreatorVisitString() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4.0");
    Worksheet ws = builder.createWorksheet();
    FormulaCreator f1 = new FormulaCreator(ws);
    assertEquals(f1.visitString("Wow").evaluate(), new SString("Wow"));

    assertEquals(f1.visitString("Incredible").evaluate(), new SString("Incredible"));
  }


  @Test
  public void testFormulaCreatorVisitNumber() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4.0");
    Worksheet ws = builder.createWorksheet();
    FormulaCreator f1 = new FormulaCreator(ws);
    assertEquals(f1.visitNumber(4.0).evaluate(), new SNumber(4.0));

    assertEquals(f1.visitNumber(-4.0).evaluate(), new SNumber(-4.0));
  }


  @Test
  public void testFormulaCreatorVisitSymbol() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4.0");
    Worksheet ws = builder.createWorksheet();
    FormulaCreator f1 = new FormulaCreator(ws);
    assertEquals(f1.visitNumber(4.0).evaluate(), new SNumber(4.0));

    assertEquals(f1.visitNumber(-4.0).evaluate(), new SNumber(-4.0));
  }

  //Jason's Tests Have Become Active

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail1() {
    new LessThanOperation().apply(new ArrayList<IFormula>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail2() {
    IFormula four = new IdentityFormula(new SNumber(4));
    new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail3() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(new SSymbol("Tree"));
    new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail4() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(new SString("Tree"));
    new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail5() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula notTrue = new IdentityFormula(new SBoolean(true));
    new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, notTrue)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanOperationFail6() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(4), new SNumber(5)))));
    new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test
  public void testLessThanOperationSucceed() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(5)))));
    assertEquals(new SBoolean(true),
        new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree))));
  }

  @Test
  public void testLessThanOperationSucceed2() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(5)))));
    assertEquals(new SBoolean(false),
        new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(tree, four))));
  }

  @Test
  public void testLessThanOperationSucceed3() {
    IFormula four = new IdentityFormula(new SNumber(4));
    assertEquals(new SBoolean(false),
        new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, four))));
  }

  @Test
  public void testLessThanOperationSucceed4() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula five = new IdentityFormula(new SNumber(5));
    assertEquals(new SBoolean(true),
        new LessThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, five))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail1() {
    new GreaterThanOperation().apply(new ArrayList<IFormula>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail2() {
    IFormula four = new IdentityFormula(new SNumber(4));
    new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail3() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(new SSymbol("Tree"));
    new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail4() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(new SString("Tree"));
    new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail5() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula notTrue = new IdentityFormula(new SBoolean(true));
    new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, notTrue)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGreaterThanOperationFail6() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(4), new SNumber(5)))));
    new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree)));
  }

  @Test
  public void testGreaterThanOperationSucceed1() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(5)))));
    assertEquals(new SBoolean(false),
        new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, tree))));
  }

  @Test
  public void testGreaterThanOperationSucceed2() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula tree = new IdentityFormula(
        new SList(new ArrayList<Sexp>(Arrays.asList(new SNumber(5)))));
    assertEquals(new SBoolean(true),
        new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(tree, four))));
  }

  @Test
  public void testGreaterThanOperationSucceed3() {
    IFormula four = new IdentityFormula(new SNumber(4));
    assertEquals(new SBoolean(false),
        new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, four))));
  }

  @Test
  public void testGreaterThanOperationSucceed4() {
    IFormula four = new IdentityFormula(new SNumber(4));
    IFormula five = new IdentityFormula(new SNumber(5));
    assertEquals(new SBoolean(false),
        new GreaterThanOperation().apply(new ArrayList<IFormula>(Arrays.asList(four, five))));
  }

  @Test
  public void testIdentityFormula() {
    Sexp four = new SNumber(4);
    assertEquals(four, new IdentityFormula(four).evaluate());
  }

  @Test
  public void testIdentityFormula2() {
    Sexp notFalse = new SBoolean(true);
    assertEquals(notFalse, new IdentityFormula(notFalse).evaluate());
  }

  @Test
  public void testIdentityFormula3() {
    Sexp notFalse = new SString("notFalse");
    assertEquals(notFalse, new IdentityFormula(notFalse).evaluate());
  }

  @Test
  public void testIdentityFormula4() {
    Sexp notFalse = new SSymbol("notFalse");
    assertEquals(notFalse, new IdentityFormula(notFalse).evaluate());
  }

  @Test
  public void testIdentityFormula5() {
    Sexp notFalse = new SSymbol("notFalse");
    assertEquals(notFalse, new IdentityFormula(notFalse).evaluate());
  }

  @Test
  public void testIdentityFormula6() {
    Sexp notFalse = new SSymbol("notFalse");
    assertEquals(new ArrayList<Coord>(), new IdentityFormula(notFalse).referencedCoords());
  }

  @Test
  public void testReferenceFormula() {
    assertEquals(new ArrayList<Coord>(),
        new ReferenceFormula(new ArrayList<Coord>(), null).referencedCoords());
  }

  @Test
  public void testReferenceFormula2() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4");
    Worksheet worksheet = builder.createWorksheet();

    Coord a1 = new Coord(1, 1);

    ArrayList<Coord> coords = new ArrayList<Coord>(Arrays.asList(a1));

    ReferenceFormula ref = new ReferenceFormula(coords, worksheet);

    assertEquals(new SList(new SNumber(4)), ref.evaluate());
    assertEquals(coords, ref.referencedCoords());

  }

  @Test
  public void testReferenceFormula3() {
    WorksheetICellBuilder builder = new WorksheetICellBuilder();
    builder.createCell(1, 1, "4");
    builder.createCell(1, 2, "5");
    Worksheet worksheet = builder.createWorksheet();

    Coord a1 = new Coord(1, 1);
    Coord a2 = new Coord(1, 2);

    ArrayList<Coord> coords = new ArrayList<Coord>(Arrays.asList(a1, a2));

    ReferenceFormula ref = new ReferenceFormula(coords, worksheet);

    assertEquals(new SList(new SNumber(4), new SNumber(5)), ref.evaluate());
    assertEquals(coords, ref.referencedCoords());
  }

}