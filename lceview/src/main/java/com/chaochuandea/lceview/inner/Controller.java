package com.chaochuandea.lceview.inner;

import com.chaochuandea.lceview.lcerecyclerview.BindingHolder;

/**
 * Created by Administrator on 2016/1/4.
 */
public abstract class Controller<T> {
   public abstract void bind(BindingHolder holder,T data, Class<T> data_type, int position);
}
