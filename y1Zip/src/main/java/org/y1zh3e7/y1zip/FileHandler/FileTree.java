package org.y1zh3e7.y1zip.FileHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

class TreeNode {
    private String name;
    private TreeNode parent;
    private List<TreeNode> children;

    public TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public void addChild(TreeNode child) {
        child.setParent(this);
        this.children.add(child);
    }

    public String getName() {
        return name;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
}

public class FileTree {
    public static TreeNode buildDirectoryTree(String path) {
        File directory = new File(path);
        if (!directory.isDirectory()) {
            return null;
        }

        TreeNode root = new TreeNode(directory.getName());
        buildTree(root, directory);
        return root;
    }

    private static void buildTree(TreeNode parentNode, File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                TreeNode childNode = new TreeNode(file.getName());
                parentNode.addChild(childNode);

                if (file.isDirectory()) {
                    buildTree(childNode, file);
                }
            }
        }
    }

    public static void printTree(TreeNode node, String indent) {
        System.out.println(indent + node.getName());
        for (TreeNode child : node.getChildren()) {
            printTree(child, indent + "  ");
        }
    }

    public static void main(String[] args) {
        String directoryPath = "/Users/y1zh3e7/CodeWorkSpace/Java/NetTalking";
        TreeNode root = buildDirectoryTree(directoryPath);
        if (root != null) {
            printTree(root, "");
        } else {
            System.out.println("Invalid directory path");
        }
    }
}

