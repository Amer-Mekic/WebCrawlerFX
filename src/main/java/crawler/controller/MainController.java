package main.java.crawler.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import main.java.crawler.logic.WebCrawler;

import java.util.List;
import java.util.Map;

public class MainController {
    @FXML
    private VBox progressOverlay;
    @FXML
    private TextField urlField;
    @FXML
    private ChoiceBox<Integer> maxLinksChoiceBox; // max links to crawl per page
    @FXML
    private ChoiceBox<Integer> maxDepthChoiceBox; // max total pages to crawl
    @FXML
    private ProgressBar progressBar;
    @FXML
    private VBox treeViewContainer;
    @FXML
    private TreeView<String> urlTreeView;

    @FXML
    public void initialize() {
        maxLinksChoiceBox.getItems().addAll(5, 10, 15);
        maxLinksChoiceBox.setValue(5); // default
        maxDepthChoiceBox.getItems().addAll(10, 50, 100, 200);
        maxDepthChoiceBox.setValue(10);
        treeViewContainer.setVisible(false); // initially hidden
        progressOverlay.setVisible(false);
// initially hidden
    }

    @FXML
    public void onCrawlButtonClicked() {
        String url = urlField.getText();
        int maxLinks = maxLinksChoiceBox.getValue();
        int maxDepth = maxDepthChoiceBox.getValue();

        // UI updates
        progressBar.setProgress(0);
        progressOverlay.setVisible(true);
        treeViewContainer.setVisible(false);
        urlTreeView.setRoot(null); // clear previous tree if any

        // Create and start crawl task
        Task<Map<String, List<String>>> crawlTask = createCrawlTask(url, maxLinks, maxDepth);
        progressBar.progressProperty().bind(crawlTask.progressProperty());

        crawlTask.setOnSucceeded(workerStateEvent -> {
            Map<String, List<String>> result = crawlTask.getValue();
            updateTreeView(result);

            // UI after done
            progressOverlay.setVisible(false);
            treeViewContainer.setVisible(true);
        });

        crawlTask.setOnFailed(e -> {
            progressOverlay.setVisible(false);
            showError("Crawl failed: " + crawlTask.getException().getMessage());
        });

        // Run the task on a background thread
        new Thread(crawlTask).start();
    }

    private Task<Map<String, List<String>>> createCrawlTask(String url, int maxLinks, int maxDepth) {
        return new Task<>() {
            @Override
            protected Map<String, List<String>> call() {
                return WebCrawler.getInstance().crawl(
                        url, maxLinks, maxDepth,
                        progress -> updateProgress(progress, 1.0)
                );
            }
        };
    }

    private void updateTreeView(Map<String, List<String>> crawlResult) {
        TreeItem<String> rootItem = new TreeItem<>("Root URL");
        rootItem.setExpanded(true);

        for (Map.Entry<String, List<String>> entry : crawlResult.entrySet()) {
            TreeItem<String> parent = new TreeItem<>(entry.getKey());

            for (String childUrl : entry.getValue()) {
                parent.getChildren().add(new TreeItem<>(childUrl));
            }

            rootItem.getChildren().add(parent);
        }

        Platform.runLater(() -> urlTreeView.setRoot(rootItem));
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}