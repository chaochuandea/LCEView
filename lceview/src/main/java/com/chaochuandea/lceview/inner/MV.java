package com.chaochuandea.lceview.inner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xizi on 15/12/25.
 */
public class MV<Model,Layout>{

    private  List<MODEL_LAYOUT> modelAndViews = new ArrayList<>();

    private HashMap<Integer,MODEL_LAYOUT> viewTypeMap = new HashMap<>();

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

    public MV<Class,Integer> with(Class model,Integer layout){
        MODEL_LAYOUT model_layout = new MODEL_LAYOUT();
        model_layout.setLayout(layout);
        model_layout.setModel(model);
        modelAndViews.add(model_layout);
        int viewtype = model.getCanonicalName().hashCode();
        viewTypeMap.put(viewtype,model_layout);
        return (MV<Class, Integer>) this;
    }

    public  Class getModel(int viewtype){
        return    viewTypeMap.get(viewtype).getModel();
    }

    public Integer getLayout(int viewtype){
        return viewTypeMap.get(viewtype).getLayout();
    }


}