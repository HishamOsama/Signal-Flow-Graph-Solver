package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

public class DirectedEgde {

	static List<Arrow> arrows = new ArrayList<Arrow>();

	public static class Arrow extends Polygon {

		public double rotate;
		public float t;
		CubicCurve curve;
		Rotate rz;
		Label label = new Label();

		public Arrow(Pane pane, Label l, CubicCurve curve, float t, double... arg0) {
			super(arg0);
			this.curve = curve;
			this.t = t;
			init();
			label = l;
			label.setFont(Font.font(20));
			pane.getChildren().add(label);
		}

		private void init() {

			setFill(Color.BLACK);

			rz = new Rotate();
			{
				rz.setAxis(Rotate.Z_AXIS);
			}
			getTransforms().addAll(rz);

			update();
		}

		public void update() {
			double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
			double scale = size / 4d;

			Point2D ori = eval(curve, t);
			Point2D tan = evalDt(curve, t).normalize().multiply(scale);

			setTranslateX(ori.getX());
			setTranslateY(ori.getY());
			label.setLayoutX(ori.getX());
			label.setLayoutY(ori.getY());

			double angle = Math.atan2(tan.getY(), tan.getX());

			angle = Math.toDegrees(angle);

			// arrow origin is top => apply offset
			double offset = -90;
			if (t > 0.5)
				offset = +90;

			rz.setAngle(angle + offset);

		}

		private Point2D eval(CubicCurve c, float t) {
			Point2D p = new Point2D(
					Math.pow(1 - t, 3) * c.getStartX() + 3 * t * Math.pow(1 - t, 2) * c.getControlX1()
							+ 3 * (1 - t) * t * t * c.getControlX2() + Math.pow(t, 3) * c.getEndX(),
					Math.pow(1 - t, 3) * c.getStartY() + 3 * t * Math.pow(1 - t, 2) * c.getControlY1()
							+ 3 * (1 - t) * t * t * c.getControlY2() + Math.pow(t, 3) * c.getEndY());
			return p;
		}

		private Point2D evalDt(CubicCurve c, float t) {
			Point2D p = new Point2D(
					-3 * Math.pow(1 - t, 2) * c.getStartX()
							+ 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlX1()
							+ 3 * ((1 - t) * 2 * t - t * t) * c.getControlX2() + 3 * Math.pow(t, 2) * c.getEndX(),
					-3 * Math.pow(1 - t, 2) * c.getStartY()
							+ 3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlY1()
							+ 3 * ((1 - t) * 2 * t - t * t) * c.getControlY2() + 3 * Math.pow(t, 2) * c.getEndY());
			return p;
		}
	}

	public static class Anchor extends Circle {
		Anchor(Color color, DoubleProperty x, DoubleProperty y) {
			super(x.get(), y.get(), 2.5);
			setFill(color.deriveColor(1, 1, 1, 0.5));
			setStroke(color);
			setStrokeWidth(2);
			setStrokeType(StrokeType.OUTSIDE);

			x.bind(centerXProperty());
			y.bind(centerYProperty());
			enableDrag();
		}

		private void enableDrag() {
			final Delta dragDelta = new Delta();
			setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					dragDelta.x = getCenterX() - mouseEvent.getX();
					dragDelta.y = getCenterY() - mouseEvent.getY();
					getScene().setCursor(Cursor.MOVE);
				}
			});
			setOnMouseReleased(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					getScene().setCursor(Cursor.HAND);
				}
			});
			setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					double newX = mouseEvent.getX() + dragDelta.x;
					if (newX > 0 && newX < getScene().getWidth()) {
						setCenterX(newX);
					}
					double newY = mouseEvent.getY() + dragDelta.y;
					if (newY > 0 && newY < getScene().getHeight()) {
						setCenterY(newY);
					}

					// update arrow positions
					for (Arrow arrow : arrows) {
						arrow.update();
					}
				}
			});
			setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						getScene().setCursor(Cursor.HAND);
					}
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						getScene().setCursor(Cursor.DEFAULT);
					}
				}
			});
		}

		private class Delta {
			double x, y;
		}
	}
}