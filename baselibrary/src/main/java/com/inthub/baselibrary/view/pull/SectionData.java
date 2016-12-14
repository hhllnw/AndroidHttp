package com.inthub.baselibrary.view.pull;

/**
 * Created by hhl on 2016/7/5.
 */
public class SectionData<T> {
    public boolean isHeader;
    public int headerIndex;//用于索引ABC...的index定位
    public T t;
    public String header;
    public Object headerObject;

    public SectionData(boolean isHeader, int headerIndex, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.headerIndex = headerIndex;
        this.t = null;
    }

    public SectionData(boolean isHeader, int headerIndex, Object headerObject) {
        this.isHeader = isHeader;
        this.headerObject = headerObject;
        this.headerIndex = headerIndex;
        this.t = null;
    }

    public SectionData(T t) {
        this.isHeader = false;
        this.header = null;
        this.headerObject = null;
        this.t = t;
    }
}
