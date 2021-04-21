package me.ducanh.thesis.model;

import java.util.Set;

public interface NodeInt {
    Set<String> getActs();
    Set<NodeInt> getTargets(String act);
}

