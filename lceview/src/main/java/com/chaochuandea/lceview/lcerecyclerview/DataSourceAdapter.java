package com.chaochuandea.lceview.lcerecyclerview;

/**
 * Created by xizi on 15/12/25.
 */

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chaochuandea.lceview.inner.FooterEntity;
import com.chaochuandea.lceview.R;
import com.chaochuandea.lceview.databinding.LceLoadingFooterBinding;
import com.chaochuandea.lceview.inner.*;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by maibenben on 2015/11/9.
 */
public abstract  class DataSourceAdapter<ResponseFromNetWork> extends RecyclerView.Adapter<BindingHolder> {


    MV<Class,Integer> mv = MV.init();
    List<Object> deals = new ArrayList<>();
    List<DataSource.RequestDataCallBack<ResponseFromNetWork>> requestDataCallBacks = new ArrayList<>();
    RecyclerView recycle;

    public void setRecycle(RecyclerView recycle) {
        this.recycle = recycle;
    }

    private int footer_layout_id;
    private boolean had_footer = false;
    private FooterEntity footer_entity = new FooterEntity();

    public void addCallBacks(DataSource.RequestDataCallBack<ResponseFromNetWork> requestDataCallBack){
        this.requestDataCallBacks.add(requestDataCallBack);
    }

    List<ResponseFromNetWork> responseFromNetWorkList = new ArrayList<>();//接口返回数据集合

    public List<ResponseFromNetWork> getResponseFromNetWorkList() {
        return responseFromNetWorkList;
    }

    public DataSourceAdapter(){
        getViewTypeAndLayout(mv);
    }

    public void refresh(){
        responseFromNetWorkList.clear();
        deals = onDataChange(responseFromNetWorkList);
        notifyDataSetChanged();
    }
    private Class<ResponseFromNetWork> getResult(){
        Class<ResponseFromNetWork> mEntityClass = null;
        try {
            mEntityClass = (Class<ResponseFromNetWork>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }catch (Exception e) {
            e.printStackTrace();
        }
        return mEntityClass;
    }
    public void load(){
        footerCallBack.onFooterLoading();
        getData(getUrl(), getParams(), getResult(), requestDataCallBack);
    }

    DataSource.RequestDataCallBack<ResponseFromNetWork> requestDataCallBack = new DataSource.RequestDataCallBack<ResponseFromNetWork>() {
        @Override
        public void success(ResponseFromNetWork responseFromNetWork) {
            for (int i = 0; i < requestDataCallBacks.size(); i++) {
                requestDataCallBacks.get(i).success(responseFromNetWork);
            }
            footerCallBack.onFooterSuceese();
        }

        @Override
        public void error(com.chaochuandea.lceview.inner.Error error) {
            for (int i = 0; i < requestDataCallBacks.size(); i++) {
                requestDataCallBacks.get(i).error(error);
            }
            footerCallBack.onFooterError();
        }
    };

     DataSource.FooterCallBack footerCallBack = new DataSource.FooterCallBack() {
        @Override
        public void onFooterLoading() {
            if (footer_layout_id == R.layout.lce_loading_footer){
                footer_entity.update(true, false, false);
            }
        }

        @Override
        public void onFooterError() {
            if (footer_layout_id == R.layout.lce_loading_footer){
                footer_entity.update(false, true, false);
            }
        }

        @Override
        public void onFooterSuceese() {
            if (footer_layout_id == R.layout.lce_loading_footer){
                footer_entity.update(false,false,true);
            }
        }
    };

    public abstract void getData(String url,HashMap<String,Object> params,Class<ResponseFromNetWork> clazz, DataSource.RequestDataCallBack<ResponseFromNetWork> requestDataCallBack);

    public abstract String getUrl();

    public abstract HashMap<String,Object> getParams();

    public void add(ResponseFromNetWork responseFromNetWork){
        responseFromNetWorkList.add(responseFromNetWork);
        deals = onDataChange(responseFromNetWorkList);
        if (had_footer&&deals.size()>0){
            deals.add(deals.size(),footer_entity);
        }
        notifyDataSetChanged();
    }

    public void add(List<ResponseFromNetWork> list){
        responseFromNetWorkList.addAll(list);
        deals = onDataChange(responseFromNetWorkList);
        if (had_footer&&deals.size()>0){
            deals.add(deals.size(),footer_entity);
        }
        notifyDataSetChanged();
    }

    public abstract void getViewTypeAndLayout(MV<Class,Integer> mv);

    public abstract List<Object> onDataChange(List<ResponseFromNetWork> responseFromNetWorkList);


    public void setFooter(boolean had_footer,int footer_layout_id){
        if (had_footer){
            mv.with(footer_entity.getClass(), footer_layout_id);
        }
        this.had_footer = had_footer;
        this.footer_layout_id = footer_layout_id;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layout = mv.getLayout(viewType);

        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                layout,
                viewGroup,
                false);
        BindingHolder holder = new BindingHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return deals.get(position).getClass().getCanonicalName().hashCode();
    }




    @Override
    public void onBindViewHolder(BindingHolder holder,final int position) {
        try {
            try {
                holder.getBinding().setVariable(getBRClass().getDeclaredField("data").getInt(getBRClass().newInstance()), deals.get(position));;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        holder.getBinding().executePendingBindings();

        //add footer
        if (holder.getBinding() instanceof LceLoadingFooterBinding && recycle!=null){
            RecyclerView.LayoutManager manager =  recycle.getLayoutManager();
            if (manager instanceof StaggeredGridLayoutManager){
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.setFullSpan(true);
                holder.getBinding().getRoot().setLayoutParams(layoutParams);
            }else if(manager instanceof LinearLayoutManager){
            }else if(manager instanceof GridLayoutManager){
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
                final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int inner_position) {
                        if (position == inner_position){
                            return gridLayoutManager.getSpanCount();
                        }else{
                            return lookup.getSpanSize(inner_position);
                        }
                    }
                });
            }
        }else{
            onBind(holder,deals.get(position),mv.getModel(getItemViewType(position)),position);
        }

    }

    public void onBind(BindingHolder holder,Object data,Class data_type,int position){

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public  Class getBRClass(){
        return com.chaochuandea.lceview.BR.class;
    };
}



