package signal.flow.graph;

import java.util.ArrayList;
import java.util.List;

import comp.Edge;
import comp.INode;
import comp.IPath;
import comp.Nodes;

public class Main {

	public static void main(String[] args) {
		List<INode> graph = new ArrayList<INode>();

		// EX1
		/*
		 * INode node1 = new Nodes("R"); INode node2 = new Nodes("y1"); INode
		 * node3 = new Nodes("y2"); INode node4 = new Nodes("y3"); INode node5 =
		 * new Nodes("c"); node1.addEdge(new Edge(node2, 1)); node2.addEdge(new
		 * Edge(node3, 2)); node3.addEdge(new Edge(node4, 3)); node4.addEdge(new
		 * Edge(node5, 1)); node1.addEdge(new Edge(node3, 1)); node4.addEdge(new
		 * Edge(node3, -4)); node4.addEdge(new Edge(node2, -5));
		 * graph.add(node1); graph.add(node2); graph.add(node3);
		 * graph.add(node4); graph.add(node5);
		 */

		// EX2
		/*
		 * INode node1= new Nodes("y1"); INode node2= new Nodes("y2"); INode
		 * node3= new Nodes("y3"); INode node4= new Nodes("y4"); INode node5=
		 * new Nodes("y5"); INode node6= new Nodes("y6"); node1.addEdge(new
		 * Edge(node2, 1)); node2.addEdge(new Edge(node3, 3)); node2.addEdge(new
		 * Edge(node4, 2)); node3.addEdge(new Edge(node2, -7));
		 * node3.addEdge(new Edge(node4, 4)); node4.addEdge(new Edge(node5, 5));
		 * node5.addEdge(new Edge(node2, -9)); node5.addEdge(new Edge(node4,
		 * -8)); node5.addEdge(new Edge(node6, 6)); graph.add(node1);
		 * graph.add(node2); graph.add(node3); graph.add(node4);
		 * graph.add(node5); graph.add(node6);
		 */

		// EX3
		/*
		INode node1 = new Nodes("y1");
		INode node2 = new Nodes("y2");
		INode node3 = new Nodes("y3");
		INode node4 = new Nodes("y4");
		INode node5 = new Nodes("y5");
		INode node6 = new Nodes("y6");
		node1.addEdge(new Edge(node2, 1));
		node2.addEdge(new Edge(node3, 5));
		node2.addEdge(new Edge(node6, 10));
		node3.addEdge(new Edge(node4, 10));
		node4.addEdge(new Edge(node3, -1));
		node4.addEdge(new Edge(node5, 2));
		node5.addEdge(new Edge(node4, -2));
		node5.addEdge(new Edge(node2, -1));
		node6.addEdge(new Edge(node5, 2));
		node6.addEdge(new Edge(node6, -1));
		graph.add(node1);
		graph.add(node2);
		graph.add(node3);
		graph.add(node4);
		graph.add(node5);
		graph.add(node6);
		 */
		
		// worst case
		/*
		 * INode node1 = new Nodes("y1"); INode node2 = new Nodes("y2"); INode
		 * node3 = new Nodes("y3"); INode node4 = new Nodes("y4"); INode node5 =
		 * new Nodes("y5"); node1.addEdge(new Edge(node1, 1)); node2.addEdge(new
		 * Edge(node2, 1)); node3.addEdge(new Edge(node3, 1)); node4.addEdge(new
		 * Edge(node4, 1)); node5.addEdge(new Edge(node5, 1)); graph.add(node1);
		 * graph.add(node2); graph.add(node3); graph.add(node4);
		 * graph.add(node5);
		 */
		
		INode node0 = new Nodes("y0");
		INode node1 = new Nodes("y1");
		INode node2 = new Nodes("y2");
		INode node3 = new Nodes("y3");
		INode node4 = new Nodes("y4");
		INode node5 = new Nodes("y5");
		node0.addEdge(new Edge(node1, 1));
		node1.addEdge(new Edge(node2, 1));
		node1.addEdge(new Edge(node4, 1));
		node2.addEdge(new Edge(node3, 1));
		node3.addEdge(new Edge(node2, -1));
		node3.addEdge(new Edge(node5, 1));
		node4.addEdge(new Edge(node4, -1));
		node4.addEdge(new Edge(node5, 1));
		node5.addEdge(new Edge(node3, -1));
		node5.addEdge(new Edge(node1, -1));
		graph.add(node0);
		graph.add(node1);
		graph.add(node2);
		graph.add(node3);
		graph.add(node4);
		graph.add(node5);
		
		ISignalFlowGraph sfg = new SignalFlowGraph(graph);
		List<IPath> paths = sfg.getAllForwardPaths(node0, node5);
		System.out.println("Forward Paths = " + paths.size());
		for (IPath path : paths) {
			System.out.println(path.getContent() + "  " + path.getGain());
		}
		List<IPath> pathss = sfg.getAllIndividualLoops();
		System.out.println("-----------------------");
		System.out.println("Individual loops = " + pathss.size());
		for (IPath path : pathss) {
			System.out.println(path.getContent() + "  " + path.getGain());
		}
		List<List<List<IPath>>> lists = sfg.getAllNonTouchingLoopsPaths();
		int ind = 0;
		for (List<List<IPath>> list1 : lists) {
			System.out.println("-----------------------");
			System.out.println("size = " + ind);
			ind++;
			System.out.println("number of sets = " + list1.size());
			int ind2 = 0;
			for (List<IPath> list2 : list1) {
				System.out.println("set number " + ind2);
				ind2++;
				for (IPath path : list2) {
					System.out.print(path.getContent() + " | ");
				}
				System.out.println();
			}
		}
		System.out.println("-----------------------");
		float answer = sfg.getOverAllTransferFunction(node0, node5);
		System.out.println("Over All T.F = " + answer);
		System.out.println("-----------------------");
		List<Float> delta = sfg.getDelta();
		for(Float del : delta){
			System.out.println(del + " ");
		}
		
	}
}