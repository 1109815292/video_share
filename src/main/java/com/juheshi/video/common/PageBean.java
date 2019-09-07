package com.juheshi.video.common;

import java.util.List;

public class PageBean<T> {
    private List<T> lists;// 存放  需要显示的 实体类数据
    private Integer limit = 1;// 当前页码数（默认给1）
    private Integer offset = 10; // 每页显示的行数
    private Integer totalPage;// 总页数
    private Integer rows;// 总行数

    //对私有属性的封装
    // 不需要对外提供totalPage总页数的set方法   因为totalPage是根据  总行数  和  每页显示的行数求出来的
    public List<T> getLists() {
        return lists;
    }
    public void setLists(List<T> lists) {
        this.lists = lists;
    }
    public Integer getLimit() {
        return limit;
    }
    public Integer getOffset() {
        return offset;
    }
    public void setOffset(Integer pageSize) {
        this.offset = pageSize;
    }
    public Integer getTotalPage() {
        return totalPage;
    }
    public Integer getRows() {
        return rows;
    }

    //设置有行数据 并求出页数
    public void setRows(Integer rows) {
        this.rows = rows;
        //页数      根据传入的 总行数 以及 每页显示的行数 求出总页数
        this.totalPage=rows % offset==0 ? rows/offset : (rows/offset+1);
    }
    public void setLimit(Integer limit){
        this.limit = limit;
    }
    //设置页码
    public void setPageNo(Integer pageNo) {
        //如果传入的页码为空 或者小于0  就默认给 1
        if (null == pageNo || pageNo < 0)
            this.limit = 1;
            //如果当前页码数>总页码数    就让 当前页码数 等于 最大页码数
        else if (pageNo > this.totalPage && this.totalPage> 0)
            this.limit = this.totalPage;
            //都符合条件 就让 当前页码数 等于 传入的页码数
        else
            this.limit = pageNo;
    }
}
