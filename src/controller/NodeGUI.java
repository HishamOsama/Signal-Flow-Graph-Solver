package controller;

import comp.Nodes;
import controller.utils.Utils;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class NodeGUI {

	private int nodeLabelCount = 0;

	public void addNode(Pane pane, Label message) {
		EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {

					// Adding the node
					Circle circle = new Circle();
					circle.setFill(Color.MEDIUMAQUAMARINE);
					circle.setStroke(Color.BLACK);
					circle.setCenterX(mouseEvent.getX());
					circle.setCenterY(mouseEvent.getY());
					circle.setRadius(20);

					// Adding the node's value
					nodeLabelCount++;
					Label label = new Label("y" + Integer.toString(nodeLabelCount));
					label.setFont(new Font(10));
					label.setTextFill(Color.BLACK);
					label.setStyle("-fx-background-color: transparent;");
					label.setLayoutX(circle.getCenterX() - 20);
					label.setLayoutY(circle.getCenterY() + 20);
					label.setVisible(false);
					label.setManaged(false);

					TextArea textArea = new TextArea("y" + Integer.toString(nodeLabelCount));
					textArea.setLayoutX(circle.getCenterX() - 20);
					textArea.setLayoutY(circle.getCenterY() + 20);
					textArea.setMaxSize(10, 10);
					textArea.setEditable(false);
					textArea.setFont(new Font(10));
					textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent event) {
							textArea.setEditable(true);
							;
						}

					});
					textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent arg0) {
							if (arg0.getCode() == KeyCode.ENTER) {
								textArea.setEditable(false);
								if (!textArea.getText().trim().equals("") && isValid(textArea.getText())) {
									label.setText(textArea.getText());
								} else {
									textArea.setText(label.getText());
								}
							}
						}

					});

					Data.nodes.add(circle);
					Data.values.add(label);
					Data.basicNodes.add(new Nodes(label.getText()));

					pane.getChildren().add(circle);
					pane.getChildren().add(label);
					pane.getChildren().add(textArea);

					Utils utils = new Utils(pane);
					utils.cleanPane();
					message.setText("");
				}
			}
		};
		pane.setOnMousePressed(mouseHandler);
	}

	private boolean isValid(String newName) {
		for (Label label : Data.values) {
			if (label.getText().equals(newName))
				return false;
		}
		return true;
	}
}