package controller;

import comp.Edge;
import controller.DirectedEgde.Anchor;
import controller.DirectedEgde.Arrow;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class EdgeGUI {

	private int count;

	public void addEdge(Pane pane, TextField text, Label message) {

		count = 1;
		DirectedEgde.arrows.clear();

		CubicCurve cubic = new CubicCurve();
		cubic.setFill(Color.TRANSPARENT);
		cubic.setStroke(Color.MEDIUMAQUAMARINE);
		cubic.setStrokeWidth(3);

		for (Circle circle : Data.nodes) {
			EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (count == 1 && mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
						message.setText("SELECT THE SECOND NODE");
						cubic.setStartX(circle.getCenterX());
						cubic.setStartY(circle.getCenterY());
						cubic.setControlX1(circle.getCenterX());
						cubic.setControlY1(circle.getCenterY());
					}
					if (count == 2 && mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {

						message.setText("MOVE EDGE FROM THE ANCHORS");

						cubic.setEndX(circle.getCenterX());
						cubic.setEndY(circle.getCenterY());
						cubic.setControlX2(circle.getCenterX());
						cubic.setControlY2(circle.getCenterY());

						Anchor control1 = new Anchor(Color.WHITE, cubic.controlX1Property(), cubic.controlY1Property());
						Anchor control2 = new Anchor(Color.WHITE, cubic.controlX2Property(), cubic.controlY2Property());

						pane.getChildren().addAll(cubic, control1, control2);

						double[] arrowShape = new double[] { 0, 0, 10, 20, -10, 20 };
						Label label = new Label(text.getText());
						if (label.getText() == "") {
							label.setText("1");
						}
						Arrow arrow = new Arrow(pane, label, cubic, 0.6f, arrowShape);

						label.setLayoutX(arrow.getTranslateX());
						label.setLayoutY(arrow.getTranslateY());

						addEdge(cubic, label);

						DirectedEgde.arrows.add(arrow);

						pane.getChildren().addAll(DirectedEgde.arrows);
					}
					if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
						count++;
					}

				}
			};
			circle.setOnMousePressed(mouseHandler);
			circle.setOnMouseReleased(mouseHandler);
		}
	}

	private void addEdge(CubicCurve c, Label l) {

		double x1 = c.getStartX();
		double y1 = c.getStartY();
		double x2 = c.getEndX();
		double y2 = c.getEndY();

		int a = 0;
		int b = 0;

		for (int i = 0; i < Data.nodes.size(); i++) {
			if (x1 == Data.nodes.get(i).getCenterX() && y1 == Data.nodes.get(i).getCenterY()) {
				a = i;
			}
			if (x2 == Data.nodes.get(i).getCenterX() && y2 == Data.nodes.get(i).getCenterY()) {
				b = i;
			}
		}

		int cc;
		if (l.getText() == "") {
			cc = 1;
		} else {
			cc = getInt(l.getText());
		}

		Data.basicNodes.get(a).addEdge(new Edge(Data.basicNodes.get(b), cc));
	}

	private int getInt(String test) {
		try {
			return Integer.parseInt(test.trim());
		} catch (Exception e) {
			return 0;
		}
	}

}