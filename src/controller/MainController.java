package controller;

import controller.utils.Output;
import controller.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class MainController {

	@FXML
	private Pane pane;

	@FXML
	private TextField text;

	@FXML
	private Label message;

	private Utils utils;
	private Output output;
	private NodeGUI nodeGUI;
	private EdgeGUI edgeGUI;

	@FXML
	public void initialize() {
		utils = new Utils(pane);
		output = new Output();
		nodeGUI = new NodeGUI();
		edgeGUI = new EdgeGUI();
	}

	@FXML
	public void addNode() {
		utils.cleanPane();
		message.setText("CLICK TO ADD A NEW NODE");
		nodeGUI.addNode(pane, message);
	}

	@FXML
	public void addEdge() {
		utils.cleanPane();
		message.setText("SELECT THE FIRST NODE");
		edgeGUI.addEdge(pane, text, message);
	}

	@FXML
	public void dragNode() {
		utils.cleanPane();
		message.setText("DRAG AND DROP ANY FREE NODE");
		dragShapes();
	}

	private void dragShapes() {

		Circle tempCircle = null;
		boolean isCircle = false;

		for (Node component : pane.getChildren()) {
			if (isCircle) {
				DragShapes drag = new DragShapes();
				drag.makeDraggable(tempCircle, (Label) component,
						(TextArea) pane.getChildren().get(pane.getChildren().indexOf(component) + 1));
				isCircle = false;
			}
			if (component instanceof Circle) {
				if (utils.checkShapesIntersection((Circle) component)) {
					tempCircle = (Circle) component;
					isCircle = true;
				}
			}
		}
	}

	public void editName() {
		TextArea nodeName = null;
		boolean isTextArea = false;
		utils.cleanPane();
	}

	public void solve() {
		utils.cleanPane();
		message.setText("SELECT THE SOURCE NODE");
		// edit by me to handle name edit
		int currNode = 0;
		boolean flag = false;
		for (Node component : pane.getChildren()) {
			if (component instanceof Circle) {
				flag = true;
			}
			if (component instanceof Label && flag) {
				flag = false;
				Data.basicNodes.get(currNode++).setNodeName(((Label) component).getText());
			}
		}

		output.setGraph();
		output.selectSpecialNodes(message);
	}
}