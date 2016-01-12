package com.chaochuandea.lceview.inner;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public abstract class MVC<T> {
    T data;
    List<T> datas;
    public MVC(T data){
        this.data = data;
    }
    public MVC(List<T> datas){
        this.datas = datas;
    }
    public  ModelName<T> getModelAndName(){
        if (data !=null){
            return new ModelName<T>(getName(),data);
        }else{
            return new ModelName<T>(getName(),datas);
        }
    };

    public abstract String getName();
    public abstract Integer getView();
    public abstract Controller<T> getController();
}
