package com.huijiewei.agile.core.until;

import com.huijiewei.agile.core.domain.AbstractTreeEntity;

import java.util.*;

/**
 * @author huijiewei
 */

public class TreeUtils {
    private static <T extends AbstractTreeEntity<T>> T getItemInListById(Integer id, List<T> list) {
        for (T item : list) {
            if (item.getId().equals(id)) {
                return item;
            }
        }

        return null;
    }

    public static <T extends AbstractTreeEntity<T>> List<Integer> getNodeIdsInTree(List<T> tree) {
        List<Integer> ids = new ArrayList<>();

        for (T node : tree) {
            ids.add(node.getId());

            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                ids.addAll(TreeUtils.getNodeIdsInTree(node.getChildren()));
            }
        }

        return ids;
    }

    private static <T extends AbstractTreeEntity<T>> T getNodeInTreeById(Integer id, List<T> tree) {
        T result = null;

        for (T node : tree) {
            if (result != null) {
                break;
            }

            if (node.getId().equals(id)) {
                result = node;
                break;
            } else if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                result = TreeUtils.getNodeInTreeById(id, node.getChildren());
            }
        }

        return result;
    }

    public static <T extends AbstractTreeEntity<T>> List<T> buildTree(List<T> items) {
        Map<Integer, T> map = new HashMap<>(items.size());

        for (T current : items) {
            map.put(current.getId(), current);
        }

        List<T> tree = new ArrayList<>();

        for (T item : items) {
            final Integer parentId = item.getParentId();

            if (parentId > 0) {
                final T child = map.get(item.getId());
                final T parent = map.get(parentId);

                if (parent != null) {
                    parent.addChild(child);
                }
            } else {
                tree.add(item);
            }
        }

        return tree;
    }

    public static <T extends AbstractTreeEntity<T>> List<T> getParents(Integer id, List<T> items) {
        List<T> parents = new ArrayList<>();

        T parent = TreeUtils.getItemInListById(id, items);

        while (parent != null) {
            parents.add(parent);
            parent = TreeUtils.getItemInListById(parent.getParentId(), items);
        }

        Collections.reverse(parents);

        return parents;
    }

    public static <T extends AbstractTreeEntity<T>> List<T> getChildren(Integer id, List<T> tree) {
        T node = TreeUtils.getNodeInTreeById(id, tree);

        if (node != null && node.getChildren() != null) {
            return node.getChildren();
        }

        return new ArrayList<>();
    }

    public static <T extends AbstractTreeEntity<T>> List<Integer> getChildrenIds(Integer id, List<T> tree) {
        List<T> children = TreeUtils.getChildren(id, tree);

        return TreeUtils.getNodeIdsInTree(children);
    }
}
