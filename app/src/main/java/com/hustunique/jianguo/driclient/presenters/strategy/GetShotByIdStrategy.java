package com.hustunique.jianguo.driclient.presenters.strategy;

import com.hustunique.jianguo.driclient.models.Shots;
import com.hustunique.jianguo.driclient.service.DribbbleUserService;
import com.hustunique.jianguo.driclient.service.factories.ApiServiceFactory;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by JianGuo on 5/5/16.
 * Strategy for loading shots by user id.
 */
public class GetShotByIdStrategy implements ILoadDataStrategy<Shots> {
    private final String id;

    public GetShotByIdStrategy(String id) {
        this.id = id;
    }


    @Override
    public Observable<List<Shots>> loadData(Map<String, String> params) {
        return ApiServiceFactory.createService(DribbbleUserService.class)
                .getShots(id ,params);
    }
}
