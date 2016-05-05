package com.hustunique.jianguo.driclient.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.hustunique.jianguo.driclient.models.Shots;
import com.hustunique.jianguo.driclient.presenters.strategy.ILoadDataStrategy;
import com.hustunique.jianguo.driclient.presenters.strategy.LoadDataDelegate;
import com.hustunique.jianguo.driclient.views.ILoadListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import rx.Subscriber;

/**
 * Created by JianGuo on 5/4/16.
 * Base Presenter for loading list of data M
 */
public abstract class BaseListPresenter<M, V extends ILoadListView<M>> extends BasePresenter<List<M>, V> {
    protected LoadDataDelegate<M> mLoadDel;

    protected boolean isLoadingData = false;

    public BaseListPresenter() {
        mLoadDel = new LoadDataDelegate<>();
    }

    public void setLoadStrategy(ILoadDataStrategy<M> loadStrategy) {
        mLoadDel.setLoadStrategy(loadStrategy);
    }

    @Override
    protected void updateView() {
        if (model.size() == 0) view().showEmpty();
        else view().showData(model);
    }

    @Override
    public void bindView(@NonNull V view) {
        super.bindView(view);
        if (model == null && !isLoadingData) {
            view.showLoading();
            loadData();
        }
    }





    protected abstract void loadData();


    @Override
    protected void resetState() {
        super.resetState();
        isLoadingData = !isLoadingData;
    }


    public abstract class LoadingListSubscriber extends Subscriber<List<M>> {

        @Override
        public void onCompleted() {
            isLoadingData = false;
        }

        @Override
        public void onError(Throwable e) {
            view().onError(e);
        }
    }
}
