package me.mangorage.nethermelt.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class ResistantArrayList<X> extends ArrayList {

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }
}
