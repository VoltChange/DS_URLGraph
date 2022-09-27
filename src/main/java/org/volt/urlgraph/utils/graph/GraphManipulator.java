package org.volt.urlgraph.utils.graph;

import org.volt.urlgraph.utils.net.UrlFetcher;

import java.util.LinkedList;
import java.util.Queue;

public class GraphManipulator {

    Graph graph;
    public GraphManipulator(){
        graph=new Graph();
    }
    public class nodeWithLevel{
        String nodeName;
        int level;
        public nodeWithLevel(String nodeName,int level)
        {
            this.nodeName=nodeName;
            this.level=level;
        }
    }
    public LinkedList<GraphNode> getNodes()
    {
        return graph.nodes;
    }
    public LinkedList<GraphEdge> getEdges()
    {
        return graph.edges;
    }
    public void clearGraph(){
        graph=new Graph();
    }
    public Graph getGraph(){
        return  graph;
    }
    public void generateGraph(String seedDomain){
        Queue<nodeWithLevel> queue = new LinkedList<nodeWithLevel>();//广搜用队列
        LinkedList<String> searchedNodes = new LinkedList<String>();//记录已经搜索过的节点
        nodeWithLevel seed = new nodeWithLevel(seedDomain,0);
        graph.insertNode(seedDomain);
        queue.offer(seed);
        while(!queue.isEmpty())
        {
            nodeWithLevel presentNode = queue.poll();
            if(!searchedNodes.stream().anyMatch(n->n.equals(presentNode.nodeName))&&presentNode.level<4)
            {
                //已经搜索过的节点中不含有该节点并且level小于4
                LinkedList<String> relatedDomains =UrlFetcher.getRelatedDomain(presentNode.nodeName,6);
                //System.out.print(relatedDomains);
                for (String rdomain:relatedDomains) {
                    graph.insertNode(rdomain);//加点
                    graph.insertEdge(presentNode.nodeName,rdomain);//加边
                    queue.offer(new nodeWithLevel(rdomain, presentNode.level+1));//加入广搜队列
                }
                searchedNodes.add(presentNode.nodeName);//加入已经搜索过的节点队列
            }
            //System.out.println("@"+presentNode.nodeName);
        }
    }
    public static void main(String[] args){
        GraphManipulator gm=new GraphManipulator();
        gm.generateGraph("www.fudan.edu.cn");
        for (GraphNode n: gm.getNodes()) {
            System.out.print(n.nodeName+", ");
        }
        System.out.println(gm.getEdges());
    }
}
