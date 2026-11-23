package entity.predictionDecorators;

import java.util.Collection;
import java.util.EnumSet;
import java.util.function.UnaryOperator;

/**
 * The factors a user can switch on. Each knows how to decorate a model with itself.
 * <p>
 * Open/Closed: adding a new factor = one new decorator class + one constant here.
 * Nothing in the use case, controller, presenter or view has to change.
 */
public enum StatFactor {;
//  POINT_DIFFERENTIAL("Point differential", yada yada),
//  FIELD_GOAL_PCT("Field goal %", yada yada),
//  THREE_POINT_PCT("3-point %", yada yada),
//  REBOUNDS("Rebounds", yada yada),
//  TURNOVERS("Turnovers", yada yada),
//  ASSISTS("Assists", yada yada),
//  DEFENSE("Steals + blocks", yada yada),
//  HOME_COURT("Home-court advantage", yada yada);

  private final String label;
  private final UnaryOperator<MatchupModel> decorator;

  StatFactor(String label, UnaryOperator<MatchupModel> decorator) {
    this.label = label;
    this.decorator = decorator;
  }

  public String getLabel() { return label; }

  public MatchupModel decorate(MatchupModel model) {
    return decorator.apply(model);
  }

  /** Wraps a BaselineModel with every selected factor, in order.
   */
  public static MatchupModel buildModel(Collection<StatFactor> selected) {
    MatchupModel model = new BaselineModel();
    if (selected == null || selected.isEmpty()) {
      return model;
    }
    for (StatFactor factor : EnumSet.copyOf(selected)) {
      model = factor.decorate(model);
    }
    return model;
  }
}
