package com.chaochuandea.lceview.inner;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xizi on 15/12/25.
 */
public class MV<Model,Layout>{

    private List<MODEL_LAYOUT> modelAndViews = new ArrayList<>();

    private HashMap<Integer,MODEL_LAYOUT> viewTypeMap = new HashMap<>();
    private HashMap<Integer,List<Controller>> controlerHashMap = new HashMap<>();
    private List<Class> classes = new ArrayList<>();
    public static MV<Class,Integer> init(){
        return new MV<>();
    }
    private MV(){

    }

    private class MODEL_LAYOUT{
        Class model;
        Integer layout;

        public Class getModel() {
            return model;
        }

        public void setModel(Class model) {
            this.model = model;
        }

        public Integer getLayout() {
            return layout;
        }

        public void setLayout(Integer layout) {
            this.layout = layout;
        }
    }


    public  MV<Class,Integer> with(ModelName model,Integer layout){
        MODEL_LAYOUT model_layout = new MODEL_LAYOUT();
        model_layout.setLayout(layout);
        model_layout.setModel(model.getClass());
        modelAndViews.add(model_layout);
        int viewtype = model.getName().hashCode();
        viewTypeMap.put(viewtype,model_layout);
        classes.add(model.getClass());
        controlerHashMap.put(viewtype, null);
        return (MV<Class, Integer>) this;
    }

    public <T>  MV<Class,Integer> with(ModelName<T> model,Integer layout,Controller<T> controler){
        MODEL_LAYOUT model_layout = new MODEL_LAYOUT();
        model_layout.setLayout(layout);
        model_layout.setModel(model.getClass());
        modelAndViews.add(model_layout);
        int viewtype = model.getName().hashCode();
        viewTypeMap.put(viewtype,model_layout);
        List<Controller> controllers = new ArrayList<>();
        controllers.add(controler);
        controlerHashMap.put(viewtype, controllers);
        classes.add(model.getClass());
        return (MV<Class, Integer>) this;
    }
    public <T>  MV<Class,Integer> with(ModelName<T> model,Integer layout,List<Controller> controler){
        MODEL_LAYOUT model_layout = new MODEL_LAYOUT();
        model_layout.setLayout(layout);
        model_layout.setModel(model.getClass());
        modelAndViews.add(model_layout);
        int viewtype = model.getName().hashCode();
        viewTypeMap.put(viewtype,model_layout);
        controlerHashMap.put(viewtype,controler);
        classes.add(model.getClass());
        return (MV<Class, Integer>) this;
    }

    public  Class getModel(int viewtype){
        return    viewTypeMap.get(viewtype).getModel();
    }

    public Integer getLayout(int viewtype){
        if (viewTypeMap.get(viewtype) == null){
            Log.e("LCE", "all class-----" + classes.toString());
        }
        return viewTypeMap.get(viewtype).getLayout();
    }
    public List<Controller> getController(int viewtype){
        return controlerHashMap.get(viewtype);
    }

}