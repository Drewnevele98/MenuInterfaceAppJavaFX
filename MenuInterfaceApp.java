package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

public class MenuInterfaceApp extends Application {

    private TextArea textArea;
    private BorderPane root;
    private VBox container;

    @Override
    public void start(Stage primaryStage) {
        // Root pane that holds everything
        root = new BorderPane();

        // Text area for output
        textArea = new TextArea();
        textArea.setWrapText(true);

        // Menu bar setup
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Options");

        MenuItem showDateTime = new MenuItem("Show Date/Time");
        MenuItem saveToFile = new MenuItem("Save to File");
        MenuItem changeGreenHue = new MenuItem("Change Green Hue");
        MenuItem exit = new MenuItem("Exit");

        menu.getItems().addAll(showDateTime, saveToFile, changeGreenHue, exit);
        menuBar.getMenus().add(menu);

        // Container holds both menu and content, so we can color everything
        container = new VBox();
        container.getChildren().addAll(menuBar, textArea);
        root.setCenter(container);

        // Actions
        showDateTime.setOnAction(e -> {
            LocalDateTime now = LocalDateTime.now();
            textArea.appendText("Date/Time: " + now + "\n");
        });

        saveToFile.setOnAction(e -> {
            try (FileWriter writer = new FileWriter("log.txt", true)) {
                writer.write(textArea.getText());
                textArea.appendText("Saved to log.txt\n");
            } catch (IOException ex) {
                textArea.appendText("Error writing to file.\n");
            }
        });

        changeGreenHue.setOnAction(e -> {
            // Green hues typically fall between 100° and 140° on HSB scale
            float hue = 100 + new Random().nextFloat() * 40;
            Color greenHue = Color.hsb(hue, 0.6, 0.85);

            // Apply background color to all parts
            container.setBackground(new Background(new BackgroundFill(greenHue, null, null)));
            textArea.setStyle("-fx-control-inner-background: #" + colorToHex(greenHue) + "; -fx-text-fill: black;");
            textArea.appendText(String.format("Changed to green hue (hue=%.2f°)\n", hue));
        });

        exit.setOnAction(e -> {
            primaryStage.close();
        });

        // Final scene setup
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("JavaFX Menu App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper to convert Color to hex string
    private String colorToHex(Color color) {
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("%02X%02X%02X", r, g, b);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
