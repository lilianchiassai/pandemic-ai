package tree;

import java.util.ArrayList;
import java.util.List;

import game.action.GameAction;

public class Node<T> {
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
    private List<T> ancestorDataList;
    
	public Node(Node<T> parent, T data) {
		this.parent=parent;
		this.data=data;
		this.children=new ArrayList<Node<T>>();
		this.ancestorDataList=new ArrayList<T>();
		if(parent!=null) {
			this.ancestorDataList.addAll(parent.getAncestorDataList());
		}
		this.ancestorDataList.add(this.data);
	}

	public T getData() {
		return this.data;
	}

	public List<Node<T>> getChildren() {
		return this.getChildren();
	}
	
	public void expand(List<T> dataList) {
		for(T data : dataList) {
			this.children.add(new Node(this,data));
		}
	}

	public List<T> getAncestorDataList() {
		return this.ancestorDataList;		
	}

	public Node<T> getParent() {
		return this.parent;
	}

	public void removeChild(Node<GameAction> node) {
		this.children.remove(node);
	}

	public boolean isLeaf() {
		return this.children.size()==0;
	}
}