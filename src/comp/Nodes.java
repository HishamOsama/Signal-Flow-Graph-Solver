package comp;

import java.util.ArrayList;
import java.util.List;

public class Nodes implements INode {
	private String name;
	private List<IEdge> edges;

	public Nodes(String name) {
		this.name = name;
		this.edges = new ArrayList<IEdge>();
	}

	@Override
	public String getNodeName() {
		return this.name;
	}

	@Override
	public List<IEdge> getEdges() {
		return this.edges;
	}

	public void addEdge(IEdge edge) {
		edges.add(edge);
	}

	@Override
	public void setNodeName(String newName) {
		this.name = newName;
	}

	@Override
	public void clearEdges() {
		this.edges.clear();
	}
}