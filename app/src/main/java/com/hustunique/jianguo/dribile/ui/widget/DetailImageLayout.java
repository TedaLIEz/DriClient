/*
 * Copyright (c) 2016.  TedaLIEz <aliezted@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hustunique.jianguo.dribile.ui.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;
import com.hustunique.jianguo.dribile.models.Shots;
import com.hustunique.jianguo.dribile.service.GifImageLoader;
import com.hustunique.jianguo.dribile.utils.Utils;
import com.hustunique.jianguo.dribile.utils.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by JianGuo on 4/11/16.
 * FrameLayout for full image of a shot, gif is supported.
 * Deprecated, see {@link com.hustunique.jianguo.dribile.presenters.GifPresenter} for more information
 */
//TODO: not in mvp design!
    @Deprecated
public class DetailImageLayout extends FrameLayout {

    private static final String TAG = "DetailImageLayout";
    private Context ctx;
    private ProgressBar mProgressBar;
    private GifImageView mGif;
    private GifImageLoader loader;

    public DetailImageLayout(Context context) {
        this(context, null);
    }

    public DetailImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
        initView();
    }

    private void initView() {
        loader = new GifImageLoader(ctx);
        mGif = new GifImageView(ctx);
        mProgressBar = new ProgressBar(ctx);
        FrameLayout.LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams progressParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.gravity = Gravity.CENTER;
        imageParams.gravity = Gravity.CENTER;
        mGif.setLayoutParams(imageParams);
        mProgressBar.setLayoutParams(progressParams);
        mGif.setFitsSystemWindows(true);
        addView(mGif);
        addView(mProgressBar);
    }

    public void load(@NonNull Shots shots) {
        Logger.i(TAG, "loading url " + shots.getJson());
        if (Utils.isGif(shots)) {
            loader.display(shots.getImages().getHidpi() == null ? shots.getImages().getNormal() : shots.getImages().getHidpi()).callback(new GifImageLoader.Callback() {
                @Override
                public void onCompleted(byte[] bytes) {
                    mProgressBar.setVisibility(GONE);
                }


                //TODO: Replace with a placeholder
                @Override
                public void onFailed() {
                    mProgressBar.setVisibility(GONE);
                }
            });
        } else {
            Picasso.with(ctx).load(Uri.parse(shots.getImages().getNormal())).into(mGif, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(GONE);
                }

                @Override
                public void onError() {
                    mProgressBar.setVisibility(GONE);
                }
            });
        }

    }



    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mGif.isAnimating()) {
            mGif.stopAnimation();
        }
    }

}
