package comp;

import java.util.List;

public interface INode {
	public String getNodeName();

	public List<IEdge> getEdges();

	public void addEdge(IEdge edge);

	public void setNodeName(String newName);

	public void clearEdges();
}
