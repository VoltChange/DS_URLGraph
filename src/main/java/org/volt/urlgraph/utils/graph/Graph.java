package org.volt.urlgraph.utils.graph;

import java.util.LinkedList;
import java.util.Optional;

public class Graph {
    LinkedList<GraphNode> nodes;
    LinkedList<GraphEdge> edges;

    public Graph()
    {
        nodes= new LinkedList<GraphNode>();
        edges= new LinkedList<GraphEdge>();
    }

    public boolean containNode(String nodeName)
    {
        return nodes.stream().anyMatch(n->n.nodeName.equals(nodeName));
    }
    public Optional<GraphNode> getNode(String nodeName)
    {
         Optional<GraphNode> node=nodes.stream().filter(n->n.nodeName.equals(nodeName)).findFirst();
         return node;
    }
    public void insertNode(String nodeName)
    {
        if(!containNode(nodeName))
        {
            nodes.add(new GraphNode(nodeName));
        }
    }
    public boolean containEdge(String originNodeName,String terminalNodeName)
    {
        return edges.stream().anyMatch(e->(e.origin.getNodeName().equals(originNodeName)
                                        &&e.terminal.getNodeName().equals(terminalNodeName)));
    }
    public void insertEdge(String originNodeName,String terminalNodeName)
    {
        if(!containEdge(originNodeName,terminalNodeName))
        {
            Optional<GraphNode> origin= getNode(originNodeName);
            Optional<GraphNode> terminal= getNode(terminalNodeName);

            if(!origin.isEmpty()&&!terminal.isEmpty()){
                GraphNode oriNode = origin.get();
                GraphNode terNode = terminal.get();
                edges.add(new GraphEdge(oriNode,terNode));
                oriNode.outDegree++;
                terNode.inDegree++;

            }
        }
    }
}
