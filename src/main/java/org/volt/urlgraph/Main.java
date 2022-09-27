package org.volt.urlgraph;/*
 * The MIT License
 *
 * Copyright 2019 brunomnsilva@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

import static java.lang.Thread.sleep;

/**
 *
 * @author brunomnsilva
 */
public class Main extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) {

        //Graph<String, String> g = build_sample_digraph();
        Graph<String, String> g = build_url_digraph("www.fudan.edu.cn");
        System.out.println(g);
        
        SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        //SmartPlacementStrategy strategy = new SmartRandomPlacementStrategy();
        SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);

        /*
        After creating, you can change the styling of some element.
        This can be done at any time afterwards.
        */
       // if (g.numVertices() > 0) {
       //     graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
       // }

        /*
        Basic usage:            
        Use SmartGraphDemoContainer if you want zoom capabilities and automatic layout toggling
        */
        //Scene scene = new Scene(graphView, 1024, 768);
        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        /*
        IMPORTANT: Must call init() after scene is displayed so we can have width and height values
        to initially place the vertices according to the placement strategy
        */
        graphView.init();

        /*
        Bellow you can see how to attach actions for when vertices and edges are double clicked
         */        
        graphView.setVertexDoubleClickAction((SmartGraphVertex<String> graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
                      
            //toggle different styling
            if( !graphVertex.removeStyleClass("myVertex") ) {
                /* for the golden vertex, this is necessary to clear the inline
                css class. Otherwise, it has priority. Test and uncomment. */
                //graphVertex.setStyle(null);
                
                graphVertex.addStyleClass("myVertex");
            }
            
            //want fun? uncomment below with automatic layout
            //g.removeVertex(graphVertex.getUnderlyingVertex());
            //graphView.update();
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            //dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            //uncomment to see edges being removed after click
            //Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            //g.removeEdge(underlyingEdge);
            //graphView.update();
        });

        /*
        Should proceed with automatic layout or keep original placement?
        If using SmartGraphDemoContainer you can toggle this in the UI 
         */
        //graphView.setAutomaticLayout(true);

        /* 
        Uncomment lines to org.example.test adding of new elements
         */
        //continuously_test_adding_elements(g, graphView);
        //stage.setOnCloseRequest(event -> {
        //    running = false;
        //});
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
        gm.generateGraph(seed);
        for (GraphNode n: gm.getNodes()) {
            g.insertVertex(n.getNodeName());
        }
        for (GraphEdge e: gm.getEdges()) {
            g.insertEdge(e.getOrigin().getNodeName(),e.getTerminal().getNodeName(),e.getOrigin().getNodeName()+"-"+e.getTerminal().getNodeName());
        }
        return g;
    }
    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("X");
        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        return g;
    }
}
