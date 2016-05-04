package com.grabandgo.gng.gng;

import java.io.Serializable;

/**
 * Category object.
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3331992394077566698L;
    private String parentCategory;
    private String category;
    private boolean selectStatus;

    public Category(String parent, String category) {
        this.parentCategory = parent;
        this.category = category;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }

    public String getParentCategory() {
        return this.parentCategory;
    }

    public String getCategory() {
        return this.category;
    }

    public boolean getStatus() {
        return this.selectStatus;
    }
}
