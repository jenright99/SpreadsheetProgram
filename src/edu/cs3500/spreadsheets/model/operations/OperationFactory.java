package edu.cs3500.spreadsheets.model.operations;

/**
 * Returns an {@link IOperation} that corresponds with the provided string.
 */
public class OperationFactory {

  /**
   * Takes in a string and returns the {@link IOperation} that corresponds with it, and null if none
   * exist.
   *
   * @param s the string representation of the operation.
   * @return the corresponding IOperation, or null if none exist.
   */
  public static IOperation createOperation(String s) {
    if (s.equals(new GreaterThanOperation().name())) {
      return new GreaterThanOperation();
    } else if (s.equals(new LessThanOperation().name())) {
      return new LessThanOperation();
    } else if (s.equals(new SumOperation().name())) {
      return new SumOperation();
    } else if (s.equals(new ProductOperation().name())) {
      return new ProductOperation();
    } else {
      return null;
    }
  }

}
