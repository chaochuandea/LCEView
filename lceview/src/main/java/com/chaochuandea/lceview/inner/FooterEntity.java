package com.chaochuandea.lceview.inner;

import android.databinding.ObservableBoolean;

/**
 * Created by xizi on 15/12/28.
 */
public class FooterEntity {
    ObservableBoolean isLoading = new ObservableBoolean(true);
    ObservableBoolean isError = new ObservableBoolean(false);
    ObservableBoolean isSuccess = new ObservableBoolean(false);


    public void update(boolean isLoading ,
            boolean isEoor ,
            boolean isSuccess ){
        this.isError.set(isEoor);
        this.isLoading .set(isLoading);
        this.isSuccess.set(isSuccess);
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(ObservableBoolean isLoading) {
        this.isLoading = isLoading;
    }

    public ObservableBoolean getIsError() {
        return isError;
    }

    public void setIsError(ObservableBoolean isError) {
        this.isError = isError;
    }

    public ObservableBoolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(ObservableBoolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
