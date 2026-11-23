package entity.predictionDecorators;

/**
 * Converts an expected point margin into a win probability using a logistic curve.
 * NBA game margins have a standard deviation of roughly 12 points, which a
 * logistic with scale ~7 approximates well: a 3-point favourite wins ~60%,
 * a 10-point favourite ~80%.
 */
public final class WinProbability {

  static final double SCALE = 7.0;

  private WinProbability() { }

  public static double fromMargin(double expectedHomeMargin) {
    return 1.0 / (1.0 + Math.exp(-expectedHomeMargin / SCALE));
  }
}
