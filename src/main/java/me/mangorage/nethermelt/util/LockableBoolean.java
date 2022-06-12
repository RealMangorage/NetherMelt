package me.mangorage.nethermelt.util;

public class LockableBoolean {
    private Boolean value;
    private Boolean locked = false;

    public LockableBoolean(Boolean initial) {

    }

    public void set(Boolean value) {
        if (locked)
            throw new IllegalStateException("Cant set value for LockableBoolean, its been locked");
        this.value = value;
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
    }



}
