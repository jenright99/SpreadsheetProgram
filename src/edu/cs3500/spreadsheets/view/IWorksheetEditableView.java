package edu.cs3500.spreadsheets.view;

import edu.cs3500.spreadsheets.controller.IWorksheetController;

/**
 * Adds a connection to a controller for views which can be edited. Utilizes the feature pattern.
 */
public interface IWorksheetEditableView extends IWorksheetView {

  /**
   * Gives access to a controller through the feature pattern.
   * @param controller the controller controlling the worksheet being viewed.
   */
  void addController(IWorksheetController controller);

}
