package com.chaochuandea.lceview.inner;

/**
 * Created by Administrator on 2016/1/12.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 处理中心，把需要的数据和模型以及视图都放到需要的位置
 */
public class DealCenter {
    MV mv;
    public DealCenter(MV mv){
        this.mv = mv;
    }
    List<ModelName> deals = new ArrayList<>();

    public List<ModelName> getDeals() {
        return deals;
    }

    public void add(ModelName object,Integer layout){
        mv.with(object, layout);
        if (null!=object.getExtra()){
            deals.add(object);
        }
        if (null != object.getExtras()){
            for (int i = 0; i < object.getExtras().size(); i++) {
                ModelName modelName = new ModelName(object.getName(),object.getExtras().get(i));
                deals.add(modelName);
            }
        }
    }

    public <T> void add(ModelName<T> object,Integer layout,Controller<T> controller){
        mv.with(object, layout,controller);
        if (null!=object.getExtra()){
            deals.add(object);
        }
        if (null != object.getExtras()){
            for (int i = 0; i < object.getExtras().size(); i++) {
                ModelName modelName = new ModelName(object.getName(),object.getExtras().get(i));
                deals.add(modelName);
            }
        }
    }

    public <T> void add(MVC<T> mvc){
        mv.with(mvc.getModelAndName(), mvc.getView(),mvc.getController());
        if (null!=mvc.getModelAndName().getExtra()){
            deals.add(mvc.getModelAndName());
        }
        if (null != mvc.getModelAndName().getExtras()){
            for (int i = 0; i < mvc.getModelAndName().getExtras().size(); i++) {
                ModelName modelName = new ModelName(mvc.getModelAndName().getName(),mvc.getModelAndName().getExtras().get(i));
                deals.add(modelName);
            }
        }
    }
}
