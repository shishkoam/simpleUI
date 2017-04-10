package com.yo.shishkoam.simpleui.adapter;

import java.util.HashSet;

/**
 * Created by User on 08.04.2017
 */

public enum RecyclerSelectionController {
    INSTANCE;

    private HashSet<Integer> selectedItems = new HashSet<>();

    public boolean isSelected(Integer item) {
        return selectedItems.contains(item);
    }

    public void changeItemSelectionState(Integer item) {
        if (isSelected(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
    }
}
