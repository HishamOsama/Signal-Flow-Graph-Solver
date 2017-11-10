package comp;

public interface IEdge extends Cloneable {
	public INode getNode();

	public float getGain();

	public IEdge clone();
}
