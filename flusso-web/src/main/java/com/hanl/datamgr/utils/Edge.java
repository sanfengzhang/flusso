package com.hanl.datamgr.utils;

/**
 * @author: Hanl
 * @date :2019/8/9
 * @desc:
 */
public class Edge<T> {

    Node<T> srcNode;

    Node<T> dstNode;

    public Edge(T srcData, T dstData) {
        this.srcNode = new Node<T>(srcData);
        this.dstNode = new Node<T>(dstData);
    }
}
