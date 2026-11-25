package app;

import data_access.ApiClient;
import data_access.ApiGateway;
import data_access.ApiTeamDataAccessObject;
import data_access.CachingApiGateway;
import data_access.FilePredictionHistoryDataAccessObject;
import entity.predictionDecorators.StatFactor;
import interface_adapter.predict_match.PredictMatchController;
import interface_adapter.predict_match.PredictMatchPresenter;
import interface_adapter.predict_match.PredictMatchViewModel;
import use_case.predict_match.PredictMatchInteractor;
import view.PredictMatchView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.nio.file.Path;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stand-alone launcher for trying the use case on its own.
 */
public final class PredictMatchMain {

  private PredictMatchMain() { }

  public static void main(String[] args) {
    Path dataDir = Path.of(System.getProperty("user.home"), ".nba-analytics");

    ApiGateway api = new CachingApiGateway(
      new ApiClient(System.getenv("API_SPORTS_KEY")),
      dataDir.resolve("cache"), Duration.ofHours(12));
    ApiTeamDataAccessObject teamDao = new ApiTeamDataAccessObject(api);
    FilePredictionHistoryDataAccessObject historyDao =
      new FilePredictionHistoryDataAccessObject(dataDir.resolve("predictions.json"));

    Map<String, String> factorOptions = new LinkedHashMap<>();
    for (StatFactor f : StatFactor.values()) {
      factorOptions.put(f.name(), f.getLabel());
    }
    PredictMatchViewModel viewModel = new PredictMatchViewModel(factorOptions);
    PredictMatchView view = new PredictMatchView(viewModel);
    PredictMatchController controller = new PredictMatchController(
      new PredictMatchInteractor(teamDao, historyDao, new PredictMatchPresenter(viewModel)));
    view.setController(controller);

    controller.loadHistory(); // show predictions saved in earlier runs

    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("NBA Match Predictor");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(view);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}
