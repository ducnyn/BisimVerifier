package me.ducanh.thesis.formula;

import me.ducanh.thesis.Edge;
import me.ducanh.thesis.Model;
import me.ducanh.thesis.Vertex;

import java.util.Map;
import java.util.Set;

public interface TreeNode {

   Boolean evaluate(Vertex vertex, Model model);

   String toString();


}
