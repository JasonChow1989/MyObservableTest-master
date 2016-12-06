package com.jason.www.myobservabletest_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class Rxjava1_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //基本使用；
        initComplite();
        //基本使用简化版；
        initSimple();
        //基本使用缩略版；
        initSimple_shortfor();
        //操作符map，对observable对象进行修改
        map();
        //操作符map，对observable对象进行修改(也可以修改类型)
        map_changeType1();
        map_changeType2();
    }

    private void map_changeType2()
    {
        Observable.just("JasonBourne").map(new Func1<String, Integer>()
        {
            @Override
            public Integer call(String s)
            {
                String ss = s + "Central Intelligence Agency";
                return ss.hashCode();
            }
        }).map(new Func1<Integer, String>()
        {
            @Override
            public String call(Integer integer)
            {
                return Integer.toString(integer);
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("------map--changeType2---:" + s);
            }
        });
    }

    private void map_changeType1()
    {
        Observable.just("JasonBourne").map(new Func1<String, Integer>()
        {
            @Override
            public Integer call(String s)
            {
                String ss = s + "Central Intelligence Agency";
                return ss.hashCode();
            }
        }).subscribe(new Action1<Integer>()
        {
            @Override
            public void call(Integer integer)
            {
                System.out.println("------map--changeType1---:" + Integer.toString(integer));
            }
        });
    }

    private void map()
    {
        Observable.just("JasonBourne").map(new Func1<String, String>()
        {
            @Override
            public String call(String s)
            {
                return s + "- CIA";
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("------map-----" + s);
            }
        });
    }

    private void initSimple_shortfor()
    {
        Observable.just("Hellow World").subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("--------call--shortfor------:" + s);
            }
        });
    }

    private void initSimple()
    {
        Observable<String> observable = Observable.just("Hellow World!!");
        Action1<String> action1 = new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("--------call--------:" + s);
            }
        };

        observable.subscribe(action1);
    }

    private void initComplite()
    {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>()
        {
            @Override
            public void call(Subscriber<? super String> subscriber)
            {
                subscriber.onNext("Hellow World!!");
                subscriber.onCompleted();
            }
        });


        Subscriber<String> subscriber = new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(String s)
            {
                System.out.println("-------onNext-------:" + s);
            }
        };

        observable.subscribe(subscriber);
    }
}
