package ru.kpfu.itis.gnt.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.kpfu.itis.gnt.exceptions.EmptyChoiceException;
import ru.kpfu.itis.gnt.exceptions.FileException;
import ru.kpfu.itis.gnt.exceptions.WeatherRequestException;
import ru.kpfu.itis.gnt.http.WeatherInfo;
import ru.kpfu.itis.gnt.http.models.WeatherModel;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public Slider drawingsSlider;
    @FXML
    private ComboBox<String> citiesComboBox;
    @FXML
    private Canvas canvas;
    private WeatherInfo weatherInfo;
    private GraphicsContext ctx;
    private CanvasDrawer drawer;

    private boolean isSunShown;
    private boolean isCloudShown;


    public void getWeatherForecast(ActionEvent actionEvent) {
        isSunShown = false;
        isCloudShown = false;
        new Thread(() -> {
            try {
                drawer.clearCanvas();
                String chosenCity = citiesComboBox.getValue();
                if (chosenCity == null) {
                    throw new EmptyChoiceException("You need to choose a city");
                } else {
                    WeatherModel weatherModel = weatherInfo.getCityWeather(chosenCity);
                    double temperature = Double.parseDouble(weatherModel.getTemperature());
                    String weatherCondition = weatherModel.getWeatherDescription();
                    String descriptiveTemperature = weatherModel.getDescriptiveTemperature();
                    if (weatherCondition.contains("Cloudy") || temperature < 0) {
                        drawer.drawCloud(drawingsSlider.getValue());
                        isCloudShown = true;
                    }
                    if (weatherCondition.contains("Sunny") || temperature > 10) {
                        drawer.drawSun(drawingsSlider.getValue());
                        isSunShown = true;
                    }
                    drawer.drawText(descriptiveTemperature);
                    drawer.drawText(weatherCondition);
                }
            } catch (WeatherRequestException | EmptyChoiceException e) {
                createAlertDialog(e.getMessage());
            }
        }).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.weatherInfo = new WeatherInfo();
        this.ctx = canvas.getGraphicsContext2D();
        this.drawer = new CanvasDrawer(canvas, ctx);
        this.citiesComboBox.setItems(FXCollections.observableList(weatherInfo.getCitiesMap().keySet().stream().toList()));
        this.setUpSliderListener();
        this.isSunShown = false;
        this.isCloudShown = false;
    }

    private void setUpSliderListener() {
        drawingsSlider.valueProperty().addListener((ov, old_val, new_val) -> {
            if (isSunShown) {
                drawer.drawSun(drawingsSlider.getValue());
            }
            if (isCloudShown) {
                drawer.drawCloud(drawingsSlider.getValue());
            }
        });

    }

    public void saveFile(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png"));
            fileChooser.setInitialFileName("canvas.png");
            File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
            if (!file.exists()) {
                file.createNewFile();
            }
            WritableImage wi = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, wi), null), "png", file);
        } catch (IOException ex) {
            createAlertDialog(ex.getMessage());
        }
    }


    private void createAlertDialog(String message) {
        // without this throws an exception because it's called from another thread
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    public void closeApp(ActionEvent actionEvent) {
        Stage stage = (Stage) canvas.getScene().getWindow();
        stage.close();
        // probably not needed
        System.exit(0);
    }
}