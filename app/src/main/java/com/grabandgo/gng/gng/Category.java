package com.grabandgo.gng.gng;

import java.io.Serializable;

/**
 * Category object.
 */
public class Category implements Serializable {

    private static final long serialVersionUID = 3331992394077566698L;
    private String category;
    private String subCategory;
    private boolean selectStatus;

    public Category(String category, String subCategory) {
        this.category = category;
        this.category = subCategory;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setStatus(boolean selectStatus) {
        this.selectStatus = selectStatus;
    }

    public String getCategory() {
        return this.category;
    }

    public String getSubCategory() {
        return this.subCategory;
    }

    public boolean getStatus() {
        return this.selectStatus;
    }
}
