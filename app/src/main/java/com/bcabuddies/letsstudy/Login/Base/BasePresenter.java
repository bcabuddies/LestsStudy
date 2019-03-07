package com.bcabuddies.letsstudy.Login.Base;

public interface BasePresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}
