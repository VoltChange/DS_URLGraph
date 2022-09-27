package org.volt.urlgraph.utils.graph;

public class GraphEdge {
    GraphNode origin;
    GraphNode terminal;

    public GraphEdge(GraphNode originNode,GraphNode terminalNode)
    {
        origin=originNode;
        terminal=terminalNode;
    }

    public GraphNode getOrigin(){return  origin;}
    public GraphNode getTerminal(){return  terminal;}
}
