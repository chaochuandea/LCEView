package com.chaochuandea.lceview.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 */
public abstract class MVC<T> {
    T data;
    List<T> datas;
    List<Controller<T>> allController = new ArrayList<>();
    public MVC(T data){
        this.data = data;
        allController.add(getController());
    }
    public MVC(List<T> datas){
        this.datas = datas;
        allController.add(getController());
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
    public void addController(Controller<T> controller){
        allController.add(controller);
    }

    public List<Controller<T>> getAllController() {
        return allController;
    }
}
