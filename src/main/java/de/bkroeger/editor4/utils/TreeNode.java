package de.bkroeger.editor4.utils;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    private List<TreeNode<T>> children = new ArrayList<TreeNode<T>>();
    private TreeNode<T> parent = null;
    private T data = null;

    public TreeNode() { };
    
    public TreeNode(T data) {	
        this.data = data;
    }

    public TreeNode(T data, TreeNode<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setParent(TreeNode<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public void addChild(T data) {
        TreeNode<T> child = new TreeNode<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(TreeNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }
}
