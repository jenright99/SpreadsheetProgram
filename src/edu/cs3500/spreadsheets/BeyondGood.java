package edu.cs3500.spreadsheets;

import edu.cs3500.spreadsheets.controller.IWorksheetController;
import edu.cs3500.spreadsheets.controller.WorksheetController;
import edu.cs3500.spreadsheets.model.Cell;
import edu.cs3500.spreadsheets.view.IWorksheetView;
import edu.cs3500.spreadsheets.view.ViewSpreadsheet;
import edu.cs3500.spreadsheets.view.WorksheetSpreadsheetView;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Scanner;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.FormulaCreator;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetICellBuilder;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

/**
 * The main class for our program.
 */
public class BeyondGood {

  /**
   * The main entry point.
   *
   * @param args any command-line arguments.
   */
  public static void main(String[] args) {

    try {
      File fr = null;
      if (args[0].equals("-in")) {
        fr = new File(args[1]);
      } else if (args[0].equals("-gui")) {
        WorksheetICellBuilder builder = new WorksheetICellBuilder();

        Worksheet w = builder.createWorksheet();
        WorksheetSpreadsheetView view = new WorksheetSpreadsheetView(w);
        view.renderView();
      } else if (args[0].equals("-edit")) {
        WorksheetICellBuilder builder = new WorksheetICellBuilder();

        Worksheet w = builder.createWorksheet();

        //the file is empty because we are not provided a file location,
        // thus making saving impossible.
        IWorksheetController controller = new WorksheetController(w, "");
        controller.run();
      } else {
        throw new IllegalStateException("File Bad >:(");
      }
      Scanner sc = new Scanner(fr);
      String scf = "";

      while (sc.hasNext()) {
        scf += "" + sc.nextLine() + "\n";
      }

      //System.out.println(scf);

      WorksheetICellBuilder builder = new WorksheetICellBuilder();
      Worksheet worksheet = WorksheetReader.read(builder, new StringReader(scf));

      if (args[2].equals("-eval")) {
        int indexDigit = FormulaCreator.indexOfDigit(args[3]);
        int col = Coord.colNameToIndex(args[3].substring(0, indexDigit));
        int row = Integer.parseInt(args[3].substring(indexDigit));
        System.out.print(worksheet.getValueAt(col, row));
      } else if (args[2].equals("-save")) {
        FileWriter fileWriter = new FileWriter(args[3]);
        PrintWriter writer = new PrintWriter(fileWriter);

        ViewSpreadsheet<Coord, Cell> view = new ViewSpreadsheet<Coord, Cell>(worksheet);
        WorksheetTextualView<Coord> textView = new WorksheetTextualView<Coord>(view, writer);

        textView.renderView();
        writer.close();

      } else if (args[2].equals("-gui")) {
        IWorksheetView immutableView = new WorksheetSpreadsheetView(worksheet);
        immutableView.renderView();
      } else if (args[2].equals("-edit")) {
        IWorksheetController controller = new WorksheetController(worksheet, args[1]);
        controller.run();
      }


    } catch (Exception e) {
      System.out.println("Error: " + e.toString());
    }

  }
}












