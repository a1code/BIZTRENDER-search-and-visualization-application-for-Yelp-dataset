package com.biztrender.dto;

public class Paging extends AbstractObject {
    private int offset;
    private int limit;

    public Paging() {
        // default constructor
    }

    public Paging(int offset, int limit) {
        super();
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isNotReqd() {
        return offset == -1 && limit == -1;
    }

}
