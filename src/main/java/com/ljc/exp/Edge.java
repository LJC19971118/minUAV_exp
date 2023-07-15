package com.ljc.exp;

public class Edge{
    public Node start, end;
    public final double weight;

    public Edge(Node start, Node end, double weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public double getWeight() {
        return weight;
    }
    @Override
    public String toString() {
        return String.format("Edge{(%d,%d) %f}", start.getNo(), end.getNo(), weight);
    }

}
