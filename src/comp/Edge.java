package comp;

public class Edge implements IEdge {
	private INode to;
	private float cost;

	public Edge(INode to, float cost) {
		this.to = to;
		this.cost = cost;
	}

	@Override
	public INode getNode() {
		return this.to;
	}

	@Override
	public float getGain() {
		return this.cost;
	}

	@Override
	public IEdge clone() {
		IEdge edge = new Edge(this.to, this.cost);
		return edge;
	}
}
