package me.ducanh.thesis.command.tree;

import me.ducanh.thesis.model.Vertex;

public interface TreeNode {

   Boolean evaluate(Vertex vertex);

   String toString();


}
