package com.hanl.datamgr.utils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
public class DAG<T> {

    List<Edge<T>> edges = null;

    private Set<Node<T>> vertices;

    private List<List<Node<T>>> dataFlow = new ArrayList<>();

    public DAG() {
        this.edges = new LinkedList<>();
        vertices = new HashSet<>();
    }

    /**
     * @param edgeList 所有的有向边
     * @param source   起始节点
     * @param target   目的节点
     * @param path     起始节点到目的节点的路径
     * @param allPaths 所有的路径集合
     */
    public static void findPath(List<Edge> edgeList, Node source, Node target, List<Node> path, List<List<Node>> allPaths) {
        if (path.indexOf(source) > -1) {
            return;
        }
        path = cleanPath(path);
        for (int i = 0; i < edgeList.size(); i++) {
            Edge edge = edgeList.get(i);
            if (edge.srcNode.equals(source)) {
                //如果相等则找到路径
                if (edge.dstNode.equals(target)) {
                    path.add(edge.srcNode);
                    path.add(edge.dstNode);
                    List<Node> tmpList = new ArrayList<>();
                    tmpList = cleanPath(path);
                    allPaths.add(tmpList);
                    path.clear();
                    return;
                }
                path.add(edge.srcNode);
                findPath(edgeList, edge.dstNode, target, path, allPaths);
            }
        }
    }

    public static List<Node> cleanPath(List<Node> path) {
        List<Node> tmp = new ArrayList<>();
        for (Node node : path) {
            if (tmp.indexOf(node) < 0) {
                tmp.add(node);
            }
        }
        return tmp;
    }

    public static void main(String[] args) {
        List<Edge> edgeList = new ArrayList<>();
        Edge edge = new Edge(1, 2);
        Edge edge2 = new Edge(1, 6);
        Edge edge3 = new Edge(2, 4);
        Edge edge4 = new Edge(2, 5);
        Edge edge7 = new Edge(4, 3);
        Edge edge1 = new Edge(1, 4);
        Edge edge8 = new Edge(5, 7);
        Edge edge9 = new Edge(6, 7);
        Edge edge6 = new Edge(3, 7);
        Edge edge5 = new Edge(3, 2);
        edgeList.add(edge);
        edgeList.add(edge1);
        edgeList.add(edge2);
        edgeList.add(edge3);
        edgeList.add(edge4);
        edgeList.add(edge5);
        edgeList.add(edge6);
        edgeList.add(edge7);
        edgeList.add(edge8);
        edgeList.add(edge9);
        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        findPath(edgeList, edge.srcNode, edge9.dstNode, path, allPaths);

        System.out.println(allPaths);
        System.out.println();
    }


}



