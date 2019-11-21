package com.hanl.datamgr.utils;

import com.hanl.datamgr.entity.CanvasCommandInstanceEntity;
import com.hanl.datamgr.entity.FlowLineEntity;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author: Hanl
 * @date :2019/10/23
 * @desc: TODO 流程处理的垃圾代码都放在这里,目前没想到比较好的处理方式，
 * 就将整个处理逻辑放在这个类中。
 */

public class FlowUtils {

    public static List<CanvasCommandInstanceEntity> fromFlowLineEntityToNodeList(Set<FlowLineEntity> flowLineEntitySet) {
        List<CanvasCommandInstanceEntity> nodeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(flowLineEntitySet)) {
            Map<String, CanvasCommandInstanceEntity> idInstance = new HashMap<>();
            for (FlowLineEntity flowLineEntity : flowLineEntitySet) {
                String fromId = flowLineEntity.getStart().getId();
                String toId = null;
                if (null != flowLineEntity.getEnd()) {
                    toId = flowLineEntity.getEnd().getId();
                }
                if (!idInstance.containsKey(fromId)) {
                    idInstance.put(fromId, flowLineEntity.getStart());
                }
                if (null != toId && !idInstance.containsKey(toId)) {
                    idInstance.put(toId, flowLineEntity.getEnd());
                }
            }
            nodeList.addAll(idInstance.values());
        }
        return nodeList;
    }

    public static List<Map<String, String>> fromFlowLineEntityToId(Set<FlowLineEntity> flowLineEntitySet) {
        List<Map<String, String>> subFlowLineMap = new ArrayList<>();
        flowLineEntitySet.forEach(e -> {
            Map<String, String> lineMap = new HashMap<>();
            String from = e.getStart().getId();
            String to = e.getEnd() == null ? null : e.getEnd().getId();
            lineMap.put("from", from);
            lineMap.put("to", to);
            subFlowLineMap.add(lineMap);
        });
        return subFlowLineMap;
    }


    public static List<String> findMainFlowLine(List<Map<String, String>> flowLine, String start, String end) {
        List<Edge> flowLineEge = new ArrayList<>();
        for (Map<String, String> line : flowLine) {
            Edge<String> edge = new Edge<String>(line.get("from"), line.get("to"));
            flowLineEge.add(edge);
        }
        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new ArrayList<>();
        DAG.findPath(flowLineEge, new Node(start), new Node(end), path, allPaths);
        List<Node> list = allPaths.get(0);
        System.out.println(list);
        List<String> result = new ArrayList<>();
        for (Node node : list) {
            if (null == node.getData()) {
                continue;
            }
            result.add((String) node.getData());
        }

        return result;
    }


    public static Map<Node, List<List<Node>>> findAllSubFlowByNode(List<Edge> flowLine, Node start, Node end) {
        Map<Node, Node> startAndEnd = findAllSubFlow(flowLine, start, end);
        if (null == startAndEnd || startAndEnd.size() == 0) {
            return null;
        }
        Iterator<Map.Entry<Node, Node>> it = startAndEnd.entrySet().iterator();
        Map<Node, List<List<Node>>> result = new HashMap<>();
        while (it.hasNext()) {
            Map.Entry<Node, Node> en = it.next();
            List<Node> path = new ArrayList<>();
            List<List<Node>> allPaths = new LinkedList<>();
            DAG.findPath(flowLine, en.getKey(), en.getValue(), path, allPaths);
            result.put(en.getKey(), allPaths);
        }

        return result;
    }


    /**
     * 获取流程途中的分支流程
     *
     * @param flowLine
     * @param start
     * @param end
     * @return
     */
    public static Map<Node, Node> findAllSubFlow(List<Edge> flowLine, Node start, Node end) {
        if (flowLine.size() == 0) {
            return null;
        }
        Map<Node, Node> flowMap = new HashMap<>();
        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        DAG.findPath(flowLine, start, end, path, allPaths);
        if (allPaths.size() <= 1) {//只有一条数据的时候就是没有分支直接返回
            return null;
        }
        //-------找到存在于所有的额path中的节点
        List<Node> mainFlowNode = new ArrayList<>();
        List<Node> first = allPaths.get(0);
        for (Node node : first) {
            boolean flag = true;
            for (int i = 1; i < allPaths.size(); i++) {
                if (!allPaths.get(i).contains(node)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                mainFlowNode.add(node);
            }
        }

        //----------获取所有的from子流程节点,出边是大于1的
        Map<Node, Integer> startSubFlowNode = new HashMap<>();
        Map<Node, Integer> endSubFlowNode = new HashMap<>();
        flowLine.forEach(line -> {
            Node src = line.srcNode;
            Node dst = line.dstNode;
            int startCount = startSubFlowNode.get(src) == null ? 0 : startSubFlowNode.get(src);
            startCount++;
            int endCount = endSubFlowNode.get(dst) == null ? 0 : endSubFlowNode.get(dst);
            endCount++;
            startSubFlowNode.put(src, startCount);
            endSubFlowNode.put(dst, endCount);
        });
        //--------------------------------

        Iterator<Map.Entry<Node, Integer>> from = startSubFlowNode.entrySet().iterator();
        Iterator<Map.Entry<Node, Integer>> to = endSubFlowNode.entrySet().iterator();
        while (from.hasNext()) {
            if (from.next().getValue() < 2) {
                from.remove();
            }
        }
        while (to.hasNext()) {
            if (to.next().getValue() < 2) {
                to.remove();
            }
        }

        //--------------------
        int size = mainFlowNode.size();
        Set<Node> startNodeSet = startSubFlowNode.keySet();
        Set<Node> endNodeSet = endSubFlowNode.keySet();
        for (Node node : startNodeSet) {
            int index = mainFlowNode.indexOf(node);
            for (int j = index + 1; j < size; j++) {
                if (endNodeSet.contains(mainFlowNode.get(j))) {
                    flowMap.put(node, mainFlowNode.get(j));
                    break;
                }
            }
        }
        return flowMap;
    }

    public static void main(String[] args) {
        List<Edge> edgeList = new ArrayList<>();
        Edge edge = new Edge(1, 2);
        Edge edge2 = new Edge(1, 3);
        Edge edge8 = new Edge(1, 10);
        Edge edge9 = new Edge(10, 11);
        Edge edge10 = new Edge(11, 4);
        Edge edge3 = new Edge(2, 4);
        Edge edge4 = new Edge(3, 4);
        Edge edge7 = new Edge(4, 5);
        edgeList.add(edge);
        edgeList.add(edge2);
        edgeList.add(edge3);
        edgeList.add(edge4);
        edgeList.add(edge7);
        edgeList.add(edge8);
        edgeList.add(edge9);
        edgeList.add(edge10);
        System.out.println(findAllSubFlow(edgeList, edge.srcNode, edge7.dstNode));

        List<Node> path = new ArrayList<>();
        List<List<Node>> allPaths = new LinkedList<>();
        DAG.findPath(edgeList, edge.srcNode, edge3.dstNode, path, allPaths);
        System.out.println(allPaths);

        System.out.println(findAllSubFlowByNode(edgeList, edge.srcNode, edge7.dstNode));
    }
}
