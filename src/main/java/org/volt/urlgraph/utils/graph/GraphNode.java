package org.volt.urlgraph.utils.graph;

public class GraphNode {
    String nodeName;
    int inDegree;
    int outDegree;

    public GraphNode(String name)
    {
        nodeName=new String(name);
        inDegree=0;
        outDegree=0;
    }

    public String getNodeName()
    {
        return nodeName;
    }
}
