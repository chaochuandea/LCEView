package com.chaochuandea.lceview.lcerecyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xizi on 15/12/25.
 */
public  class BindingHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    public BindingHolder(View itemView) {
        super(itemView);
    }

    public ViewDataBinding getBinding() {
        return binding;
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }
}
