package com.siasis.dalilahghaisani.siasisver50.menu.item;

public class MaterialDevisor {
    private boolean bottom;

    public MaterialDevisor() {
        this.bottom = false;
    }

    public MaterialDevisor(boolean bottom) {
        this.bottom = bottom;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }
}
