package com.tech.alldownloadersaver.api;

import android.app.Activity;

import com.tech.alldownloadersaver.model.TwitterResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;
    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }
    public void callTwitterApi(final DisposableObserver observer, String URL, String twitterModel) {
        RestClient.getInstance(mActivity).getService().callTwitter(URL,twitterModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TwitterResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(TwitterResponse o) {
                        observer.onNext(o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }
                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

}