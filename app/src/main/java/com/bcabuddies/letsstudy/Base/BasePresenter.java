package com.bcabuddies.letsstudy.Base;

//this is the basePresenter interface which will handle Context received from BaseView of any parent activity, eg of Login.class
public interface BasePresenter<V extends BaseView> {
    void attachView(V view);
    //V is generic
    void detachView();
}
