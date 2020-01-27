package com.miro.widget.utils;

import com.miro.widget.model.Widget;

import java.util.ArrayList;
import java.util.List;


public class TwoDimensionalTree {

    Node root;

    class Node {
        Widget widget;
        Node left;
        Node right;

        public Node(Widget widget) {
            this.widget = widget;
        }

    }

    public void insert(Widget widget) {
        this.root = insertRec(root, widget, 0);
    }

    private Node insertRec(Node node, Widget widget, int depth) {
        if (node == null) {
            return new Node(widget);
        }
        //current dimension
        int cd = depth % 2;

        if (widget.pointValueByDimension(cd) < node.widget.pointValueByDimension(cd)) {
            node.left = insertRec(node.left, widget, depth + 1);
        }
        else{
            node.right = insertRec(node.right, widget, depth + 1);
        }
        return node;
    }

    public Node findMin(Node node, int dimension) {
        return findMinRec(node, dimension, 0);
    }

    private Node findMinRec(Node node, int dimension, int depth) {

        if (node == null) {
            return null;
        }

        int cd = depth % 2;

        if (cd == dimension) {
            if (node.left == null)
                return node;
            return findMinRec(node.left, dimension, depth+1);
        }

        return minNode(node,
                findMinRec(node.left, dimension, depth+1),
                findMinRec(node.right, dimension, depth+1), dimension);

    }

    private Node minNode(Node x, Node y, Node z, int dimension) {
        Node result = x;
        if (y != null && y.widget.pointValueByDimension(dimension) < result.widget.pointValueByDimension(dimension))
            result = y;
        if (z != null && z.widget.pointValueByDimension(dimension) < result.widget.pointValueByDimension(dimension))
            result = z;

        return result;

    }

    public void deleteNode(Widget widget) {
        deleteRec(root, widget, 0);
    }

    private Node deleteRec(Node node, Widget widget, int depth) {
        if (node == null) {
            return null;
        }

        int cd = depth % 2;

        if (node.widget.getId() == widget.getId()) {

            if (node.right != null) {
                Node min = findMin(node.right, cd);
                node.widget = min.widget;

                node.right = deleteRec(node.right, min.widget, depth + 1);
            }
            else if (node.left != null) {
                Node min = findMin(node.left, cd);
                node.widget = min.widget;

                node.left = deleteRec(node.left, min.widget, depth + 1);
            }
            else
                return null;
            return node;
        }
        if(widget.pointValueByDimension(cd) < node.widget.pointValueByDimension(cd)){
            node.left = deleteRec(node.left, widget, depth + 1);
        }
        else{
            node.right = deleteRec(node.right, widget, depth + 1);
        }
        return node;

    }

    public List<Widget> rangeSearch(Double x1, Double x2, Double y1, Double y2) {
        List<Widget> widgets = new ArrayList<>();
        rangeSearchRec(root,0, x1, x2, y1, y2, widgets);
        return widgets;
    }

    private void rangeSearchRec(Node node, int depth, Double x1, Double x2, Double y1, Double y2, List<Widget> result) {
        if (node == null) {
            return;
        }

        NodePosition position = getNodePosition(node, depth, x1, x2, y1, y2);
        if (position == NodePosition.INSIDE) {

            if(isNodeInside(node,x1, x2, y1, y2))
                result.add(node.widget);

            rangeSearchRec(node.left, depth+1, x1, x2, y1, y2, result);
            rangeSearchRec(node.right, depth+1, x1, x2, y1, y2, result);
        }
        else if(position == NodePosition.LEFT){
            rangeSearchRec(node.left, depth+1, x1, x2, y1, y2, result);
        }
        else{
            rangeSearchRec(node.right, depth+1, x1, x2, y1, y2, result);
        }
    }

    private boolean isNodeInside(Node node, Double x1, Double x2, Double y1, Double y2) {
        return  x1 <= node.widget.getCoordinates().getX() - node.widget.getWidth() / 2 &&
                x2 >= node.widget.getCoordinates().getX() + node.widget.getWidth() / 2 &&
                y1 <= node.widget.getCoordinates().getY() - node.widget.getHeight() / 2 &&
                y2 >= node.widget.getCoordinates().getY() + node.widget.getHeight() / 2;
    }

    private NodePosition getNodePosition(Node node, int depth, Double x1, Double x2, Double y1, Double y2) {

        int cd = depth % 2;
        if (cd == 0) {
            if (x1 <= node.widget.getCoordinates().getX() && x2 >= node.widget.getCoordinates().getX()) {
                return NodePosition.INSIDE;
            } else if (x2 < node.widget.getCoordinates().getX()) {
                return NodePosition.LEFT;
            }
            return NodePosition.RIGHT;
        }

        if (y1 <= node.widget.getCoordinates().getY() && y2 >= node.widget.getCoordinates().getY()) {
            return NodePosition.INSIDE;
        } else if (y2 < node.widget.getCoordinates().getY()) {
            return NodePosition.LEFT;
        }
        return NodePosition.RIGHT;

    }

    private enum NodePosition {
        INSIDE,
        LEFT,
        RIGHT
    }


}
