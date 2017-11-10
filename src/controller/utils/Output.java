package controller.utils;

import java.util.ArrayList;
import java.util.List;

import comp.INode;
import comp.IPath;
import controller.Data;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import signal.flow.graph.ISignalFlowGraph;
import signal.flow.graph.SignalFlowGraph;

public class Output {

	public Output() {
		setTableColumns();
	}

	private TableView<ForwardPath> forwardPathsTable = new TableView<ForwardPath>();
	private ObservableList<ForwardPath> forwardPaths = FXCollections.observableArrayList();

	private TableView<ForwardPath> forwardPathsTable2 = new TableView<ForwardPath>();
	private ObservableList<ForwardPath> forwardPaths2 = FXCollections.observableArrayList();

	private TableView<ForwardPath> forwardPathsTable3 = new TableView<ForwardPath>();
	private ObservableList<ForwardPath> forwardPaths3 = FXCollections.observableArrayList();

	private TableView<ForwardPath> forwardPathsTable4 = new TableView<ForwardPath>();
	private ObservableList<ForwardPath> forwardPaths4 = FXCollections.observableArrayList();

	private Label forwordPathsLabel;
	private Label individualLoopsLabel;
	private Label touchingLoopsLabel;
	private Label transferfunctionLabel;
	private Label deltaLabel;

	private List<INode> graph = new ArrayList<INode>();
	private int count;
	private int i, j;
	private String delta = "Δ";
	private int fontSize = 20;

	public void setGraph() {
		for (int i = 0; i < Data.basicNodes.size(); i++) {
			graph.add(Data.basicNodes.get(i));
		}
	}

	public void selectSpecialNodes(Label message) {
		count = 0;

		for (Circle circle : Data.nodes) {
			EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					if (count == 0 && mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
						message.setText("SELECT THE DESTINATION NODE");
						i = getNode(circle.getCenterX(), circle.getCenterY());
						circle.setFill(Color.ORANGE);
					}
					if (count == 1 && mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
						message.setText("SOLUTION IS READY");
						j = getNode(circle.getCenterX(), circle.getCenterY());
						circle.setFill(Color.ORANGE);
					}
					if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
						count++;
					}
					if (count == 2) {
						try{
							print(graph, i, j);
						} catch(Exception e){
							
						}
						Data.nodes.get(i).setFill(Color.MEDIUMAQUAMARINE);
						Data.nodes.get(j).setFill(Color.MEDIUMAQUAMARINE);
						count = 0;
					}
				}
			};
			circle.setOnMousePressed(mouseHandler);
			circle.setOnMouseReleased(mouseHandler);
		}
	}

	private int getNode(double x, double y) {
		int nodeIndex = 0;
		for (int i = 0; i < Data.nodes.size(); i++) {
			if (x == Data.nodes.get(i).getCenterX() && y == Data.nodes.get(i).getCenterY()) {
				nodeIndex = i;
			}
		}
		return nodeIndex;
	}

	private void print(List<INode> graph, int i, int j) {

		clearLists();

		ISignalFlowGraph sfg = new SignalFlowGraph(graph);
		List<IPath> paths = sfg.getAllForwardPaths(Data.basicNodes.get(i), Data.basicNodes.get(j));
		forwordPathsLabel = new Label("Forward Paths = " + paths.size());
		for (IPath path : paths) {
			forwardPaths.add(new ForwardPath(path.getContent(), Float.toString(path.getGain())));
		}
		List<IPath> pathss = sfg.getAllIndividualLoops();
		individualLoopsLabel = new Label("Individual loops = " + pathss.size());
		for (IPath path : pathss) {
			forwardPaths2.add(new ForwardPath(path.getContent(), Float.toString(path.getGain())));
		}
		List<List<List<IPath>>> lists = sfg.getAllNonTouchingLoopsPaths();
		int ind = 0;
		for (List<List<IPath>> list1 : lists) {
			ind++;
			int ind2 = 0;

			for (List<IPath> list2 : list1) {
				ind2++;
				String temp = ""; // NEW
				for (IPath path : list2) {
					temp += path.getContent() + " | ";
				}
				forwardPaths3.add(new ForwardPath(Integer.toString(ind - 1), temp.substring(0, temp.length() - 3))); // NEW
			}
		}
		float answer = sfg.getOverAllTransferFunction(Data.basicNodes.get(i), Data.basicNodes.get(j));
		transferfunctionLabel = new Label("Over All Transfere Function = " + answer);

		List<Float> deltas = sfg.getDelta();
		for (int k = 0; k < deltas.size(); k++) {

			if (k == 0) {
				forwardPaths4.add(new ForwardPath(delta, Float.toString(deltas.get(k)))); // NEW
			} else {
				forwardPaths4.add(new ForwardPath(delta + Integer.toString(k), Float.toString(deltas.get(k)))); // NEW

			}
		}

		show();

	}

	private void show() {

		touchingLoopsLabel = new Label("Touching Loops");
		deltaLabel = new Label("Delta");

		Stage results = new Stage();
		results.initModality(Modality.WINDOW_MODAL);

		setLabelsStyle();

		VBox root = new VBox(forwordPathsLabel, forwardPathsTable, individualLoopsLabel, forwardPathsTable2,
				touchingLoopsLabel, forwardPathsTable3, deltaLabel, forwardPathsTable4, transferfunctionLabel);
		root.setAlignment(Pos.CENTER);

		Scene resultScene = new Scene(root, 500, 500);
		results.setScene(resultScene);
		results.setTitle("Solution");
		results.show();
	}

	private void clearLists() {
		forwardPaths.clear();
		forwardPaths2.clear();
		forwardPaths3.clear();
		forwardPaths4.clear();
	}

	private void setTableColumns() {
		TableColumn forwardPath = new TableColumn("Forward Path");
		forwardPath.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("path"));

		TableColumn gain = new TableColumn("Gain");
		gain.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("gain"));

		TableColumn forwardPath2 = new TableColumn("Loop");
		forwardPath2.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("path"));

		TableColumn gain2 = new TableColumn("Gain");
		gain2.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("gain"));

		TableColumn forwardPath3 = new TableColumn("Size n");
		forwardPath3.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("path"));

		TableColumn gain3 = new TableColumn("Path(s)");
		gain3.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("gain"));

		TableColumn forwardPath4 = new TableColumn("Delta");
		forwardPath4.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("path"));

		TableColumn gain4 = new TableColumn("Value");
		gain4.setCellValueFactory(new PropertyValueFactory<ForwardPath, String>("gain"));

		forwardPathsTable.setItems(forwardPaths);
		forwardPathsTable.getColumns().addAll(forwardPath, gain);

		forwardPathsTable2.setItems(forwardPaths2);
		forwardPathsTable2.getColumns().addAll(forwardPath2, gain2);

		forwardPathsTable3.setItems(forwardPaths3);
		forwardPathsTable3.getColumns().addAll(forwardPath3, gain3);

		forwardPathsTable4.setItems(forwardPaths4);
		forwardPathsTable4.getColumns().addAll(forwardPath4, gain4);
	}

	private void setLabelsStyle() {
		forwordPathsLabel.setFont(new Font(fontSize));
		individualLoopsLabel.setFont(new Font(fontSize));
		touchingLoopsLabel.setFont(new Font(fontSize));
		deltaLabel.setFont(new Font(fontSize));
		transferfunctionLabel.setFont(new Font(fontSize));
	}

}