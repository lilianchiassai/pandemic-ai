package ai.mcts;

import java.util.ArrayList;
import java.util.List;

import gameStatus.Game;



public class Node<T> {
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
    
    public Node(T data) {
    	this.data = data;
    	this.parent = null;
    	this.children = new ArrayList<Node<T>>();
    }
    
    public Node(T data, Node<T> parent) {
    	this.data = data;
    	this.parent = parent;
    	this.children = new ArrayList<Node<T>>();
    }
    
    public Node(T data, Node<T> parent, List<Node<T>> children) {
    	this.data = data;
    	this.parent = parent;
    	this.children = children;
    }
    
    public List<Node<T>> getChildren() {
    	return this.children;
    }
    
	public Node getParent() {
		return this.parent;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	protected void addChild(Node node) {
		this.children.add(node);
	}

	public T getData() {
		return this.data;
	}
}
