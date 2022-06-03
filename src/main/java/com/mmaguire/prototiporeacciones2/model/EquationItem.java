package com.mmaguire.prototiporeacciones2.model;

public class EquationItem {
    private String item;
    private EquationItemType type;

    public EquationItem() {
    }

    public EquationItem(String item, EquationItemType type) {
        this.item = item;
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public EquationItemType getType() {
        return type;
    }

    public void setType(EquationItemType type) {
        this.type = type;
    }
    @Override
    public EquationItem clone() {
        return new EquationItem(
                this.item,
                this.type
        );
    }
}
