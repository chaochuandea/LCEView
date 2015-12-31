package com.chaochuandea.lceview.inner;

/**
 * Created by xizi on 15/12/28.
 */
public class Error{
    private int code;
    private String text;


    public void setText(String text) {
        this.text = text;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}