package com.hustunique.jianguo.driclient.presenters.strategy;

import com.hustunique.jianguo.driclient.models.Likes;
import com.hustunique.jianguo.driclient.models.Shots;
import com.hustunique.jianguo.driclient.service.DribbbleUserService;
import com.hustunique.jianguo.driclient.service.factories.ApiServiceFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by JianGuo on 5/5/16.
 * Strategy for loading my likes
 */
public class GetMyLikesStrategy implements ILoadShotStrategy {

    @Override
    public Observable<List<Shots>> loadData() {
        return ApiServiceFactory.createService(DribbbleUserService.class)
                .getAuthLikeShots()
                .map(new Func1<List<Likes>, List<Shots>>() {
                    @Override
                    public List<Shots> call(List<Likes> likes) {
                        List<Shots> rst = new ArrayList<Shots>();
                        for (Likes like : likes) {
                            rst.add(like.getShot());
                        }
                        return rst;
                    }
                });
    }

    @Override
    public Observable<List<Shots>> loadMore() {
        return null;
    }

    @Override
    public int getLoadingCount() {
        return 0;
    }
}
