package com.chaochuandea.lceview.lcerecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.chaochuandea.lceview.inner.Error;
import com.chaochuandea.lceview.R;
import com.chaochuandea.lceview.inner.DataSource;

/**
 * Created by xizi on 15/12/28.
 */
public class LCERecyclerView<Result> extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    private FrameLayout loading_container;
    private FrameLayout error_container;
    private FrameLayout empty_container;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int loading_layout_id;
    private int error_layout_id;
    private int footer_layout_id;
    private int empty_layout_id;
    private String url;//网络请求URL
    private boolean should_load_on_page_create = false;
    private boolean had_loadmore = false;
    private boolean had_refresh = false;
    private boolean finishLoadMore = false;

    private DataSourceAdapter adapter;
    private DataSource.RequestDataCallBack<Result> inner_RequestData_callBack;
    private boolean refresh = false;//是否正在刷新
    private boolean requesting = false;//是否正在请求

    private int page = 1;//当前请求页码
    RecyclerView.LayoutManager manager;

    public LCERecyclerView(Context context) {
        super(context);
        init();
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setHad_refresh(boolean had_refresh) {
        this.had_refresh = had_refresh;
        swipeRefreshLayout.setEnabled(had_refresh);
    }

    /**
     * 用于设置是否加载了所有数据的方法，不在执行loadmore方法
     * @param isLoadMore
     */
    public void finishLoadMore(boolean isLoadMore){
        finishLoadMore = isLoadMore;
    }

    public boolean isHad_refresh() {
        return had_refresh;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public boolean isShould_load_on_page_create() {
        return should_load_on_page_create;
    }

    public boolean isHad_loadmore() {
        return had_loadmore;
    }

    public void setHad_loadmore(boolean had_loadmore) {
        this.had_loadmore = had_loadmore;
    }

    public void setShould_load_on_page_create(boolean should_load_on_page_create) {
        this.should_load_on_page_create = should_load_on_page_create;
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lcerecyclerview,this,false);
        addView(view);
        loading_container = (FrameLayout) view.findViewById(R.id.lec_loading_container);
        error_container = (FrameLayout) view.findViewById(R.id.lec_error_container);
        empty_container = (FrameLayout) view.findViewById(R.id.lec_empty_comtainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        if (loading_layout_id !=0){
            this.loading_container.addView(LayoutInflater.from(getContext()).inflate(loading_layout_id, this.loading_container, false));
        }
        if (error_layout_id != 0 ){
            this.error_container.addView(LayoutInflater.from(getContext()).inflate(error_layout_id, this.error_container, false));
        }

        if (empty_layout_id !=0){
            this.empty_container.addView(LayoutInflater.from(getContext()).inflate(empty_layout_id,this.empty_container,false));
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        inner_RequestData_callBack = new DataSource.RequestDataCallBack<Result>() {
            @Override
            public void success(Result result) {
                if (refresh){
                    adapter.refresh();
                }
                adapter.add(result);
                refresh = false;
                requesting = false;
                swipeRefreshLayout.setRefreshing(false);
                if (adapter.getItemCount()>0){
                    showContent();
                }else{
                    showEmpty();
                }
            }

            @Override
            public void error(Error error) {
                shoError();
                refresh = false;
                requesting = false;
                swipeRefreshLayout.setRefreshing(false);
            }



        };
        swipeRefreshLayout.setEnabled(had_refresh);
    }
    protected void handleAttributes(Context context, AttributeSet attrs){
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.lce_recycler);
            loading_layout_id = styledAttrs.getResourceId(R.styleable.lce_recycler_loading_layout,R.layout.loading);
            error_layout_id = styledAttrs.getResourceId(R.styleable.lce_recycler_error_layout,R.layout.error);
            url = styledAttrs.getNonResourceString(R.styleable.lce_recycler_url);
            should_load_on_page_create = styledAttrs.getBoolean(R.styleable.lce_recycler_shouldload_onpage_create, true);
            had_loadmore = styledAttrs.getBoolean(R.styleable.lce_recycler_had_loadmore, true);
            had_refresh = styledAttrs.getBoolean(R.styleable.lce_recycler_had_refresh,true);
            footer_layout_id = styledAttrs.getResourceId(R.styleable.lce_recycler_loading_footer, R.layout.lce_loading_footer);
            empty_layout_id = styledAttrs.getResourceId(R.styleable.lce_recycler_empty_layout,R.layout.empty);
            styledAttrs.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LCERecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
        init();
    }

    public LCERecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(context, attrs);
        init();
    }


    public void setLayoutManager(RecyclerView.LayoutManager manager){
        this.manager = manager;
        recyclerView.setLayoutManager(manager);
    }

    public void setAdapter(DataSourceAdapter<Result> adapter) {
        if (manager==null){
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            setLayoutManager(manager);
        }
        this.adapter = adapter;
        adapter.setRecycle(recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.addCallBacks(inner_RequestData_callBack);
        if (had_loadmore){
            recyclerView.addOnScrollListener(new OnRcvScrollListener() {
                @Override
                public void onBottom() {
                    if (!requesting&& !finishLoadMore) {
                        loadMore();
                    }
                }
            });
            adapter.setFooter(had_loadmore,footer_layout_id);
        }
        if (should_load_on_page_create){
            showLoading();
            refresh();
        }else{
            showContent();
        }
    }


    public void refresh(){
        refresh = true;
        requesting = true;
        finishLoadMore = false;
        adapter.load(true);
    }


    public void loadMore(){
        if (requesting){
            return;
        }
        requesting = true;
        adapter.load(false);
    }

    private void showLoading() {
        this.loading_container.setVisibility(VISIBLE);
        this.error_container.setVisibility(INVISIBLE);
        this.empty_container.setVisibility(INVISIBLE);
    }

    private void shoError() {
        this.loading_container.setVisibility(INVISIBLE);
        this.error_container.setVisibility(VISIBLE);
        this.empty_container.setVisibility(INVISIBLE);
    }

    private void showEmpty(){
        this.loading_container.setVisibility(INVISIBLE);
        this.error_container.setVisibility(INVISIBLE);
        this.empty_container.setVisibility(VISIBLE);
    }

    private void showContent() {
        this.loading_container.setVisibility(INVISIBLE);
        this.error_container.setVisibility(INVISIBLE);
        this.empty_container.setVisibility(INVISIBLE);
    }


    @Override
    public void onRefresh() {
        refresh();
    }
}
