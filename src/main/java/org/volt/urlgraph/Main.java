package org.volt.urlgraph;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphVertex;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.volt.urlgraph.utils.graph.GraphEdge;
import org.volt.urlgraph.utils.graph.GraphManipulator;
import org.volt.urlgraph.utils.graph.GraphNode;

import java.util.LinkedList;
import java.util.Scanner;


/**
 *
 * @author brunomnsilva
 */
public class Main extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) {

        System.out.println("please input seed site");
        String seed;
        Scanner scanner =new Scanner(System.in);
        seed =scanner.nextLine();
        Graph<String, String> g = build_url_digraph(seed);
        //System.out.println(g);
        
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

       if (g.numVertices() > 0) {
            graphView.getStylableVertex(seed).setStyle("-fx-fill: gold; -fx-stroke: brown;");
        }

        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 900, 640);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        graphView.init();


        graphView.setVertexDoubleClickAction((SmartGraphVertex<String> graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
                      
            //toggle different styling
            if( !graphVertex.removeStyleClass("myVertex") ) {
                
                graphVertex.addStyleClass("myVertex");
            }

        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            //dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");

        });

        graphView.setAutomaticLayout(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Graph<String,String> build_url_digraph(String seed)
    {
        Digraph<String, String> g = new DigraphEdgeList<>();
        GraphManipulator gm=new GraphManipulator();
        long startTime = System.currentTimeMillis();
        gm.generateGraph(seed);
        long endTime = System.currentTimeMillis();
        long usedTime = endTime - startTime;
        System.out.println("used time : "+usedTime+"ms");
        int biggestInDegree=-1;
        String nodeWithBiggestInDegree="";
        for (GraphNode n: gm.getNodes()) {
            g.insertVertex(n.getNodeName());
            if(n.getInDegree()>biggestInDegree)
            {
                biggestInDegree=n.getInDegree();
                nodeWithBiggestInDegree=n.getNodeName();
            }
        }
        for (GraphEdge e: gm.getEdges()) {
            g.insertEdge(e.getOrigin().getNodeName(),e.getTerminal().getNodeName(),e.getOrigin().getNodeName()+"-"+e.getTerminal().getNodeName());
        }
        System.out.println("nodes number: "+gm.getNodes().size());
        System.out.println("edges number: "+gm.getEdges().size());
        System.out.println("node with biggest in-degree("+biggestInDegree+"): "+nodeWithBiggestInDegree);
        return g;
    }
}
