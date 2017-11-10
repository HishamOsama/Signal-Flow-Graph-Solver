package signal.flow.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp.Edge;
import comp.IEdge;
import comp.INode;
import comp.IPath;
import comp.Path;

public class SignalFlowGraph implements ISignalFlowGraph {
	private List<INode> graph;
	private Map<String, Integer> nameMap;
	private Boolean[] visit;
	private List<IPath> tempForwardPaths;
	private List<ArrayList<IEdge>> takenEdges;

	private List<IPath> forwardPaths, individualLoops;
	private List<Float> delta;

	public SignalFlowGraph(List<INode> graph) {
		this.graph = graph;
		initializee();
	}

	@Override
	public List<IPath> getAllForwardPaths(INode input, INode output) {
		this.forwardPaths = new ArrayList<IPath>();
		getAllForwardPathsII(input, output);
		for (ArrayList<IEdge> listEdges : this.takenEdges) {
			this.forwardPaths.add(getPathFromEdges(listEdges, input));
		}
		return this.forwardPaths;
	}

	public List<ArrayList<IEdge>> getAllForwardPathsII(INode input, INode output) {
		this.takenEdges = new ArrayList<ArrayList<IEdge>>();
		List<IEdge> takenEdges = new ArrayList<IEdge>();
		dfs(input, output, takenEdges);
		return this.takenEdges;
	}

	@Override
	public List<IPath> getAllIndividualLoops() {
		List<List<IEdge>> loopEdges = new ArrayList<List<IEdge>>();
		List<List<IEdge>> loopEdges2 = new ArrayList<List<IEdge>>();

		int num[] = new int[graph.size()];
		int ind = 0;
		for (INode node : graph) {
			loopEdges.addAll(getAllForwardPathsII(node, node));
			num[ind++] = loopEdges.size() - 1;
		}
		loopEdges2 = removeDuplicates(loopEdges);
		this.tempForwardPaths = new ArrayList<IPath>();
		for (List<IEdge> path : loopEdges2) {
			ind = loopEdges.indexOf(path);
			for (int i = 0; i < num.length; i++) {
				if (ind <= num[i]) {
					ind = i;
					break;
				}
			}
			this.tempForwardPaths.add(getPathFromEdges(path, graph.get(ind)));
		}
		this.individualLoops = this.tempForwardPaths;
		return this.tempForwardPaths;
	}

	public List<List<List<IPath>>> getAllNonTouchingLoopsPaths() {
		return getAllNonTouchingLoopsPaths(this.individualLoops);
	}

	@Override
	public List<Float> getDelta() {
		List<Float> delta = new ArrayList<Float>();
		List<List<IPath>> allIndividualLoops = new ArrayList<List<IPath>>();
		allIndividualLoops.add(this.individualLoops);
		int ind = 1;
		for (IPath forwardPath : this.forwardPaths) {
			allIndividualLoops.add(new ArrayList<IPath>());
			for (IPath loopPath : this.individualLoops) {
				if (checkLoopNonTouchingWithForwardPath(convertPathToNodes(forwardPath),
						convertPathToNodes(loopPath))) {
					allIndividualLoops.get(ind).add(loopPath);
				}
			}
			ind++;
		}

		for (List<IPath> loops : allIndividualLoops) {
			delta.add(calculateDelta(getAllNonTouchingLoopsPaths(loops)));
		}
		this.delta = (List<Float>) ((ArrayList<Float>) delta).clone();
		return delta;
	}

	private float calculateDelta(List<List<List<IPath>>> allLoops) {
		float answer = 1;
		int sign = -1;
		for (int i = 1; i < allLoops.size(); i++) {
			float segmaLoops = 0;
			for (int j = 0; j < allLoops.get(i).size(); j++) {
				float value = 1;
				for (IPath path : allLoops.get(i).get(j)) {
					value *= path.getGain();
				}
				segmaLoops += value;
			}
			answer += (sign * segmaLoops);
			sign *= -1;
		}
		return answer;
	}

	@Override
	public Float getOverAllTransferFunction(INode input, INode output) {
		if (!checkInputNode(input)) {
			INode inputNode = getInputNode();
			if (inputNode == null)
				return new Float(0);
			float answer1 = getOverAllTransferFunction(inputNode, output);
			float answer2 = getOverAllTransferFunction(inputNode, input);
			return answer1 / answer2;
		}
		float answer = 0;
		List<IPath> paths1 = getAllForwardPaths(input, output);
		getAllIndividualLoops();
		List<Float> deltaValues = getDelta();
		int M = paths1.size();
		for (int i = 1; i <= M; i++) {
			answer += (paths1.get(i - 1).getGain()) * (deltaValues.get(i));
		}
		answer /= delta.get(0);
		return answer;
	}

	private void initializee() {
		nameMap = new HashMap<String, Integer>();
		visit = new Boolean[this.graph.size()];
		int index = 0;
		for (INode node : this.graph) {
			nameMap.put(node.getNodeName(), index);
			visit[index++] = false;
		}
		handleMultipleEdges();
	}

	private void handleMultipleEdges() {
		for (INode node : this.graph) {
			List<INode> childs = new ArrayList<INode>();
			List<Float> gains = new ArrayList<Float>();
			for (IEdge edge : node.getEdges()) {
				int ind = childs.indexOf(edge.getNode());
				if (childs.contains(edge.getNode())) {
					gains.set(ind, gains.get(ind) + edge.getGain());
				} else {
					childs.add(edge.getNode());
					gains.add(edge.getGain());
				}
			}
			node.clearEdges();
			for (INode child : childs) {
				node.addEdge(new Edge(child, gains.get(childs.indexOf(child))));
			}
		}
	}

	private void dfs(INode node, INode targetNode, List<IEdge> takenEdges) {
		for (IEdge edge : node.getEdges()) {
			INode child = edge.getNode();

			if (child.getNodeName().equals(targetNode.getNodeName())) {
				takenEdges.add(edge);
				this.takenEdges.add((ArrayList<IEdge>) ((ArrayList<IEdge>) takenEdges).clone());
				takenEdges.remove(edge);
				continue;
			}
			if (!visit[nameMap.get(child.getNodeName())]) {
				takenEdges.add(edge);
				visit[nameMap.get(child.getNodeName())] = true;
				dfs(child, targetNode, takenEdges);
				takenEdges.remove(edge);
				visit[nameMap.get(child.getNodeName())] = false;
			}
		}
	}

	private IPath getPathFromEdges(List<IEdge> edges, INode startNode) {
		IPath path = new Path(startNode.getNodeName(), 1);
		for (IEdge edge : edges) {
			path.setContent(path.getContent() + " " + edge.getNode().getNodeName());
			path.setGain(path.getGain() * edge.getGain());
		}
		return path;
	}

	private List<List<IEdge>> removeDuplicates(List<List<IEdge>> edges) {
		List<List<IEdge>> takenEdgesWithOutDulicates = new ArrayList<List<IEdge>>();
		Boolean taken[] = new Boolean[edges.size()];
		Arrays.fill(taken, false);
		for (int i = 0; i < edges.size(); i++) {
			if (taken[i])
				continue;
			taken[i] = true;
			takenEdgesWithOutDulicates.add((List<IEdge>) ((ArrayList) edges.get(i)).clone());
			for (int j = i + 1; j < edges.size(); j++) {
				if (checkEqual(edges.get(i), edges.get(j))) {
					taken[j] = true;
				}
			}
		}
		return takenEdgesWithOutDulicates;
	}

	private Boolean checkEqual(List<IEdge> path1, List<IEdge> path2) {
		if (path1.size() != path2.size())
			return false;
		for (IEdge edge : path1) {
			if (!path2.contains(edge))
				return false;
		}
		return true;
	}

	private List<INode> convertPathToNodes(IPath path) {
		String[] names = path.getContent().split(" ");
		List<INode> nodes = new ArrayList<INode>();
		for (String name : names) {
			nodes.add(graph.get(nameMap.get(name)));
		}
		return nodes;
	}

	private Boolean checkNonTouching(int currLoop, List<Integer> prevLoops, List<IPath> indvidualLoops) {
		List<INode> loop1 = convertPathToNodes(indvidualLoops.get(currLoop));
		for (Integer loopIndex : prevLoops) {
			List<INode> loop2 = convertPathToNodes(indvidualLoops.get(loopIndex));
			for (INode node : loop1) {
				if (loop2.contains(node))
					return false;
			}
		}
		return true;
	}

	private Boolean checkLoopNonTouchingWithForwardPath(List<INode> forwardPath, List<INode> loopPath) {
		for (INode node : forwardPath) {
			if (loopPath.contains(node))
				return false;
		}
		return true;
	}

	private Boolean checkInputNode(INode node) {
		for (INode node1 : graph) {
			for (IEdge edge : node1.getEdges()) {
				if (edge.getNode().equals(node)) {
					return false;
				}
			}
		}
		return true;
	}

	private INode getInputNode() {
		Boolean[] flags = new Boolean[graph.size()];
		Arrays.fill(flags, true);
		for (INode node1 : graph) {
			for (IEdge edge : node1.getEdges()) {
				flags[nameMap.get(edge.getNode().getNodeName())] = false;
			}
		}
		for (int i = 0; i < flags.length; i++) {
			if (flags[i]) {
				return graph.get(i);
			}
		}
		return null;
	}

	private List<List<List<IPath>>> getAllNonTouchingLoopsPaths(List<IPath> individualLoops) {
		List<List<Integer>> adjList = getTwoNonTouching(individualLoops);
		List<List<List<IPath>>> answer = new ArrayList<List<List<IPath>>>();
		for (int i = 0; i <= individualLoops.size(); i++) {
			answer.add(new ArrayList<List<IPath>>());
		}
		for (int i = 0; i < individualLoops.size(); i++) {
			List<Integer> prev = new ArrayList<Integer>();
			prev.add(i);
			dfs2(i, adjList, individualLoops, prev, answer);
			prev.clear();
		}
		return answer;
	}

	private List<List<Integer>> getTwoNonTouching(List<IPath> individualLoops) {
		List<INode> nodes1, nodes2;
		List<List<Integer>> adjList = new ArrayList<List<Integer>>();
		for (int i = 0; i < individualLoops.size(); i++)
			adjList.add(new ArrayList<Integer>());
		for (int i = 0; i < individualLoops.size(); i++) {
			for (int j = i + 1; j < individualLoops.size(); j++) {
				nodes1 = convertPathToNodes(individualLoops.get(i));
				nodes2 = convertPathToNodes(individualLoops.get(j));
				boolean flag = true;
				for (INode node1 : nodes1) {
					if (nodes2.contains(node1)) {
						flag = false;
					}
				}
				if (flag) {
					adjList.get(i).add(j);
				}
			}
		}
		return adjList;
	}

	private void dfs2(int node, List<List<Integer>> adjList, List<IPath> individualLoops, List<Integer> prevLoops,
			List<List<List<IPath>>> answer) {
		for (Integer child : adjList.get(node)) {
			if (checkNonTouching(child, prevLoops, individualLoops)) {
				prevLoops.add(child);
				dfs2(child, adjList, individualLoops, prevLoops, answer);
				prevLoops.remove((prevLoops.size() - 1));
			}
		}
		int sz = prevLoops.size();
		List<IPath> temp = new ArrayList<IPath>();
		for (int i = 0; i < sz; i++) {
			temp.add(individualLoops.get(prevLoops.get(i)));
		}
		answer.get(sz).add(temp);
	}

}