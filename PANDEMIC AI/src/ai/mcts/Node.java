package ai.mcts;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.action.GameAction;



public class Node<T> {
    private T data;
    private Node<T> parent;
    private List<Node<T>> unvisitedChildren;
    private List<Node<T>> visitedChildren;

    public Node(T data) {
    	this.data = data;
    	this.parent = null;
    	this.visitedChildren = new ArrayList<Node<T>>();
    	this.unvisitedChildren = new ArrayList<Node<T>>();
    }
    
    public Node(T data, Node<T> parent) {
    	this.data = data;
    	this.parent = parent;
    	this.visitedChildren = new ArrayList<Node<T>>();
    	this.unvisitedChildren = new ArrayList<Node<T>>();
    }
    
    public Node(T data, Node<T> parent, List<Node<T>> children) {
    	this.data = data;
    	this.parent = parent;
    	this.visitedChildren = new ArrayList<Node<T>>();
    	this.unvisitedChildren = children;
    }
    
    public List<Node<T>> getChildren() {
    	List<Node<T>> children = new ArrayList<Node<T>>(this.unvisitedChildren);
    	children.addAll(this.visitedChildren);
    	return children;
    }
    
	public Node getParent() {
		return this.parent;
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}
	
	protected void addChild(Node node) {
		this.unvisitedChildren.add(node);
	}

	public T getData() {
		return this.data;
	}
	
	public List<Node<T>> getUnvisitedChildren() {
		return this.unvisitedChildren;
	}
	
	public List<Node<T>> getVisitedChildren() {
		return this.visitedChildren;
	}
	
	public void removeUnvisitedChild(Node<T> node) {
		this.unvisitedChildren.remove(node);
	}
	
	public void addUnVisitedChild(Node<T> node) {
		this.unvisitedChildren.add(node);
	}
	
	public void removeVisitedChild(Node<T> node) {
		this.visitedChildren.remove(node);
	}
	
	public void addVisitedChild(Node<T> node) {
		this.visitedChildren.add(node);
	}
}
