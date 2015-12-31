package com.chaochuandea.lceview.inner;

import java.util.HashMap;

/**
 * Created by xizi on 15/12/28.
 */
public interface  DataSource<T> {

    public  void load(String url, HashMap<String, Object> params, RequestDataCallBack<T> requestDataCallBack);

    public interface RequestDataCallBack<T>{
        void success(T t);
        void error(com.chaochuandea.lceview.inner.Error error);


    }

    public interface FooterCallBack{
        /**
         * footer t 状态，请处理
         */
        void onFooterLoading();
        void onFooterError();
        void onFooterSuceese();
    }
}
