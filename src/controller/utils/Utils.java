package controller.utils;

import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class Utils {

	private Pane pane;

	public Utils(Pane pane) {
		this.pane = pane;
	}

	public void cleanPane() {
		resetPaneEvents();
		resetComponentsEvents();
		removeMovableAnchros();
	}

	public boolean checkShapesIntersection(Circle circle) {

		for (Node component : pane.getChildren()) {
			if (component instanceof CubicCurve) {
				if (((CubicCurve) component).getStartX() == circle.getCenterX()
						&& ((CubicCurve) component).getStartY() == circle.getCenterY()) {
					return false;
				}
				if (((CubicCurve) component).getEndX() == circle.getCenterX()
						&& ((CubicCurve) component).getEndY() == circle.getCenterY()) {
					return false;
				}
			}
		}
		return true;
	}

	private void resetPaneEvents() {
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					// Do Nothing...
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					// Do Nothing...
				} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
					// Do Nothing...
				}
			}
		};
		pane.setOnMousePressed(mouseHandler);
		pane.setOnMouseDragged(mouseHandler);
		pane.setOnMouseReleased(mouseHandler);
	}

	private void resetComponentsEvents() {

		for (Node component : pane.getChildren()) {
			if (component instanceof Circle) {
				component.setCursor(Cursor.DEFAULT);
				EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
							// Do Nothing...
						} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
							// Do Nothing...
						} else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
							// Do Nothing...
						}
					}
				};
				component.setOnMousePressed(mouseHandler);
				component.setOnMouseDragged(mouseHandler);
				component.setOnMouseReleased(mouseHandler);
			}
		}
	}

	private void removeMovableAnchros() {
		ArrayList<Node> anchors = new ArrayList<Node>();
		for (Node component : pane.getChildren()) {
			if (component instanceof Circle) {
				if (((Circle) component).getRadius() == 2.5) {
					anchors.add(component);
				}
			}
		}
		pane.getChildren().removeAll(anchors);
	}

}