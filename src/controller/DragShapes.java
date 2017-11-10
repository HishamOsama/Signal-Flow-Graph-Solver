package controller;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class DragShapes {

	private double x;
	private double y;

	public void makeDraggable(Circle circle, Label label, TextArea textArea) {
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x = mouseEvent.getX();
					y = mouseEvent.getY();
				}
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					double deltaX = mouseEvent.getX() - x;
					double deltaY = mouseEvent.getY() - y;
					circle.setCenterX(circle.getCenterX() + deltaX);
					circle.setCenterY(circle.getCenterY() + deltaY);
					label.setLayoutX(circle.getCenterX() - 20);
					label.setLayoutY(circle.getCenterY() + 20);
					textArea.setLayoutX(circle.getCenterX() - 20);
					textArea.setLayoutY(circle.getCenterY() + 20);
					x = mouseEvent.getX();
					y = mouseEvent.getY();
				}
			}
		};
		circle.setOnMousePressed(mouseHandler);
		circle.setOnMouseDragged(mouseHandler);
	}

}