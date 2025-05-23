package me.ducanh.thesis;

import java.util.Collection;
import java.util.Set;

public interface Graph<V,E> {
    Collection<V> getVertices();
    Collection<E> getEdges();
    Collection<E> getEdges(V vertices);
    Set<V> getTargets(V vertex, String action);
}
