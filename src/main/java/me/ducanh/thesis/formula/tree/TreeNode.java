package me.ducanh.thesis.formula.tree;

import me.ducanh.thesis.model.CustomVertex;

public interface TreeNode {

   Boolean evaluate(CustomVertex vertex);

   String toString();


}
