package com.example.xlm.mydrawerdemo.model;

import com.example.xlm.mydrawerdemo.bean.ChildFourm;

import java.util.List;

/**
 * Created by xlm on 2015/11/19.
 */
public class gitmodel {
    private String id;
    private String sort;
    private String name;
    private String status;
    private List<ChildFourm> forums;

    public gitmodel(String id, String sort, String name, String status, List<ChildFourm> forums) {
        this.id = id;
        this.sort = sort;
        this.name = name;
        this.status = status;
        this.forums = forums;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ChildFourm> getForums() {
        return forums;
    }

    public void setForums(List<ChildFourm> forums) {
        this.forums = forums;
    }
}
