Note: While the list of changes is quite large, most changes were abstraction (moving code to helper classes).
Changes to IWorksheet:
	1) I mentioned in the last assignment how I improved my cyclical checking to improve efficiency. In actuallity, I simply broke our cyclical checking. Oops. Our cyclical checking has been reverted to what we originally had. 
	The cyclical checking is a breadth first search to check for a loop. A hashmap stores previously checked values so cells are not "double checked". Cells are checked when they are added or updated.
	2) Fixed a bug in remove which did not reset the values hashmap when a cell was removed. Thus, cells still though a value was in the removed cell, even though the cell was removed.
	3) We changed how an incorrectly parsed cell was handled. Originally, an exception was thrown, which we handled in the view.
	However, we decided that to better store the contents of the incorrectly parsed cell, we would instead store the value an ERRORED value, which can be interpretted by the view.
	This allows us to still store the incorrectly parsed cell in the model.
Addition of SpreadsheetPanel/Changes to WorksheetSpreadsheetView
	In our original design, WorksheetSpreadsheetView created the SpreadsheetPanel with private helper methods. It was also the actionlistener for mouse clicks.
	This was poor design, as use of a visual implementation of the spreadsheet was not limited to this single view.
	Thus, we moved this duty to the SpreadsheetPanel class, which extends JPanel. This class draws the spreadsheet as a JPanel, and handles ActionListening.
	The WorksheetSpreadsheetView now simple creates as SpreadsheetPanel and displays it. This futureproofs our design, and made creating the editable view much easier.
Changes to how SpreadsheetPanel draws the spreadsheet.
	We added a special MouseListener, CellSelect, to our implementation of SpreadsheetPanel. An instance of CellSelect is added to each cell.
	We also added functionality to how SpreadsheetPanel draws cells. Cells are drawn "highlighted" when they are selected by CellSelect.
	The exact functionality of CellSelect is explained in the CellSelect Javadocs.

	