import java.awt.*;
import javax.swing.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.*;
import edu.uci.ics.jung.visualization.*;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.visualization.renderers.*;
import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.Factory;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.HashSet;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.visualization.control.*;

class Main{
	public static void main(String[] args){
		SimpleGraphView sgv = new SimpleGraphView(); //We create our graph in here
		// The Layout<V, E> is parameterized by the vertex and edge types
		Layout<Integer, String> layout = new FRLayout2(sgv.g);
		layout.setSize(new Dimension(1024,768)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		VisualizationViewer<Integer,String> vv =
			new VisualizationViewer<Integer,String>(layout);
		vv.setPreferredSize(new Dimension(1024,768)); //Sets the viewing area size
		Color slate = new Color(25,25,35);
		Transformer<Integer,Paint> vertexPaint = new Transformer<Integer,Paint>() {
			public Paint transform(Integer i) {
				Random R = new Random();
				Color c = new Color(R.nextInt(255), R.nextInt(255), R.nextInt(255));
				return c;
			}
		}; 
		Transformer<String,Paint> edgePaint = new Transformer<String,Paint>() {
			public Paint transform(String i) {
				return Color.white;
			}
		}; 
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		vv.getRenderContext().setArrowFillPaintTransformer(edgePaint);
		vv.setBackground(slate);
		DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
		gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(gm); 
		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setLocation(200,200);
		frame.setVisible(true);
	}

}

class SimpleGraphView{
	public DirectedSparseGraph<Integer,String> g;
	public DirectedGraph<Integer,String> ed;
	private Transformer<String, Integer> trans;
	private Map<String, Double> cart;
	private Factory<String> edgeFact;
	private Factory<Integer> vertFact;
	private Factory<DirectedGraph<Integer, String>> graphFact;
	//Fix the random thing soon
	private Random R = new Random();
	public SimpleGraphView(){

		cart = new HashMap<String, Double>();

		trans = new Transformer<String, Integer>(){
			public Integer transform(String link){
				return R.nextInt();
			}
		};

		edgeFact = new Factory<String>(){
			public String create(){
				return Integer.toString(R.nextInt());
			}
		};

		vertFact = new Factory<Integer>(){
			public Integer create(){
				return R.nextInt();
			}
		};

		graphFact = new Factory<DirectedGraph<Integer, String>>(){
			public DirectedGraph<Integer, String> create(){
				return new DirectedSparseGraph<Integer, String>();
			}
		};

		EppsteinPowerLawGenerator eplg = new EppsteinPowerLawGenerator(graphFact, vertFact, edgeFact, 80, 240, 100);
		g = (DirectedSparseGraph)eplg.create();
		g.addVertex(1);
		g.addVertex(2);
		g.addEdge("ONE", 1,2);

		//TEST EDMONDS-KARP

		EdmondsKarpMaxFlow maxFlow = new EdmondsKarpMaxFlow(g, 1, 2, trans, cart, edgeFact);
		maxFlow.evaluate();
		ed = maxFlow.getFlowGraph();
		System.out.println("---------------------------------------------" + maxFlow.getMaxFlow());
	}
}

