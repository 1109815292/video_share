package com.juheshi.video.util;

import java.util.List;

public class Page<T> {
    private long total;
    private List<T> list;

    public Page() {
    }

    public Page(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
