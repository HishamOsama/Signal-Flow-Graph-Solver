package signal.flow.graph;

import java.util.List;

import comp.INode;
import comp.IPath;

public interface ISignalFlowGraph {
	public List<IPath> getAllForwardPaths(INode input, INode output);

	public List<IPath> getAllIndividualLoops();

	public List<List<List<IPath>>> getAllNonTouchingLoopsPaths();

	public List<Float> getDelta();

	public Float getOverAllTransferFunction(INode input, INode output);
}