package com.chaochuandea.lceview.inner;

/**
 * Created by Administrator on 2016/1/12.
 */

import java.util.List;

/**
 * 这是用于设置模型名称的一个快捷类
 * 比如：当我们遇到model是List<User> 那我们就会很容易的和List<Other>这两个类的类名是一样的，这就要用一个类来包裹他们
 */
public class ModelName<T> {
    String name;
    public ModelName(String name){
        this.name = name;
    }
    T extra;
    List<T> extras;
    public ModelName(String name, T extra){
        this.name = name;
        this.extra = extra;
    }

    public ModelName(String name, List<T> extra){
        this.name = name;
        this.extras = extra;
    }

    public T getExtra() {
        return extra;
    }

    public List<T> getExtras() {
        return extras;
    }

    public String getName() {
        return name;
    }
}
