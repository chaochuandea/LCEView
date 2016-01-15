package com.chaochuandea.lceview.inner;

import com.chaochuandea.lceview.lcerecyclerview.BindingHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public abstract class Controller<T> {
   public abstract void bind(BindingHolder holder,T data, Class<T> data_type, int position);
   List<Controller<T>> controllerList = new ArrayList<>();

   public List<Controller<T>> getControllerList() {
      return controllerList;
   }

   public void addController( Controller<T> controller){
      controllerList.add(controller);
   }
}
