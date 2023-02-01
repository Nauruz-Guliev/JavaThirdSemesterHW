package ru.kpfu.itis.gnt.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;


public class CanvasDrawer {
    private final Canvas canvas;
    private final GraphicsContext ctx;


    public CanvasDrawer(Canvas canvas, GraphicsContext ctx) {
        this.canvas = canvas;
        this.ctx = ctx;
        this.textYPosition = 20;
    }

    public void drawText(String text) {
        if (textYPosition <= canvas.getHeight()) {
            textYPosition += TEXT_DISTANCE;
        }
        ctx.setFill(Color.BLACK);
        ctx.fillText(text, DEFAULT_TEXT_X_POSITION, textYPosition);
    }

    public void clearCanvas() {
        this.textYPosition = 40;
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void drawSun(double multiplier) {
        double coeff = multiplier * 2;

        double xSun = SUN_X_POSITION - coeff / 2;
        double ySun = SUN_Y_POSITION - coeff / 2;
        double sunSize = DEFAULT_SUN_SIZE + coeff;

        // clear previous drawing
        // not the most optimal approach of clearing
        double maxIncreaseSize = sunSize / 2 + 5;
        ctx.clearRect(xSun - maxIncreaseSize, ySun - maxIncreaseSize, xSun + maxIncreaseSize * 2, ySun + maxIncreaseSize * 2);

        double additionalDistance = (sunSize - 10 * 2);
        double extraVerticalDistance = 10;
        double xMiddle = xSun + sunSize / 2;
        double yMiddle = ySun + sunSize / 2;
        double xLeft = xMiddle - additionalDistance;
        double xRight = xMiddle + additionalDistance;
        double yTop = yMiddle - additionalDistance;
        double yBottom = yMiddle + additionalDistance;

        ctx.setFill(Color.ORANGE);
        ctx.setStroke(Color.ORANGE);
        ctx.setLineWidth(multiplier);
        // horizontal line
        ctx.strokeLine(xLeft, yMiddle, xRight, yMiddle);
        // vertical line
        ctx.strokeLine(xMiddle, yTop, xMiddle, yBottom);

        // vertical left-bottom to right-top
        ctx.strokeLine(xLeft + extraVerticalDistance, yBottom - extraVerticalDistance, xRight - extraVerticalDistance, yTop + extraVerticalDistance);
        // vertical left-top to right-bottom
        ctx.strokeLine(xLeft + extraVerticalDistance, yTop + extraVerticalDistance, xRight - extraVerticalDistance, yBottom - extraVerticalDistance);

        ctx.fillOval(xSun, ySun, sunSize, sunSize);
    }


    public void drawCloud(double multiplier) {
        ctx.setFill(Color.GRAY);
        double coeff = multiplier * 4;
        double cloudSize = DEFAULT_CLOUD_SIZE + coeff;
        double extraSmallSpacing = (cloudSize / 4);
        double bigSpacing = (cloudSize / 2) + extraSmallSpacing;
        double smallSpacing = (cloudSize / 2);

        double xCloud = DEFAULT_CLOUD_X_POSITION - coeff;
        double yCloud = DEFAULT_CLOUD_Y_POSITION - coeff;

        GridBagConstraints gbc = new GridBagConstraints();

        // clear previous cloud
        // not the most optimal approach of clearing
        // since the cloud position is not decided by user it should not be a problem
        double maxIncreaseSize = cloudSize/2 + 5;
        ctx.clearRect(xCloud - maxIncreaseSize, yCloud - maxIncreaseSize, xCloud + maxIncreaseSize * 4, yCloud + maxIncreaseSize * 4);


        //left ellipse
        ctx.fillOval(xCloud, yCloud, cloudSize, cloudSize);

        //right ellipse
        ctx.fillOval(xCloud + bigSpacing * 2 + extraSmallSpacing, yCloud, cloudSize, cloudSize);

        //double right ellipses
        ctx.fillOval(xCloud + bigSpacing + smallSpacing, yCloud - extraSmallSpacing, cloudSize, cloudSize);
        ctx.fillOval(xCloud + bigSpacing + smallSpacing, yCloud + extraSmallSpacing, cloudSize, cloudSize);

        //double left ellipses
        ctx.fillOval(xCloud + smallSpacing, yCloud + extraSmallSpacing, cloudSize, cloudSize);
        ctx.fillOval(xCloud + smallSpacing, yCloud - extraSmallSpacing, cloudSize, cloudSize);

        ctx.setFill(Color.ORANGE);
    }


    private final static int DEFAULT_TEXT_X_POSITION = 20;
    private int textYPosition;
    private final static int TEXT_DISTANCE = 20;

    private final static int SUN_X_POSITION = 300;
    private final static int SUN_Y_POSITION = 40;
    private final static int DEFAULT_SUN_SIZE = 50;


    private final static int DEFAULT_CLOUD_X_POSITION = 250;
    private final static int DEFAULT_CLOUD_Y_POSITION = 300;
    private final static int DEFAULT_CLOUD_SIZE = 40;

}
