package com.jason.www.myobservabletest_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Rxjava3_Activity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_3);

        /**
         * 1.错误处理
         onComplete()和onError()函数,这两个函数用来通知订阅者，被观察的对象将停止发送数据以及为什么停止（成功的完成或者出错了）
         这种错误处理方式比传统的错误处理更简单。传统的错误处理中，通常是在每个回调中处理错误。这不仅导致了重复的代码，并且意味着每个回调都必须知道如何处理错误，你的回调代码将和调用者紧耦合在一起。
         使用RxJava，Observable对象根本不需要知道如何处理错误！操作符也不需要处理错误状态-一旦发生错误，就会跳过当前和后续的操作符。所有的错误处理都交给订阅者来做
         */
        Observable.just("Hellow World !!").map(new Func1<String, String>()
        {
            @Override
            public String call(String s)
            {
                return potentialException(s);
            }
        }).map(new Func1<String, String>()
        {
            @Override
            public String call(String s)
            {
                return anotherPotentialException(s);
            }
        }).subscribe(new Subscriber<String>()
        {
            @Override
            public void onCompleted()
            {
                System.out.println("Completed!");
            }

            @Override
            public void onError(Throwable e)
            {
                System.out.println("Ouch!");
            }

            @Override
            public void onNext(String s)
            {
                System.out.println(s);
            }
        });

        /**
         *  2 调度器
         *  假设你编写的Android app需要从网络请求数据（感觉这是必备的了，还有单机么？）。网络请求需要花费较长的时间，因此你打算在另外一个线程中加载数据。那么问题来了！
            编写多线程的Android应用程序是很难的，因为你必须确保代码在正确的线程中运行，否则的话可能会导致app崩溃。最常见的就是在非主线程更新UI。
            使用RxJava，你可以使用subscribeOn()指定观察者代码运行的线程，使用observerOn()指定订阅者运行的线程.
         *
         */



    }

    /**
     * 可能会抛出异常的两个方法
     */
    public String potentialException(String s)
    {
        return s;
    }

    public String anotherPotentialException(String s)
    {
        return s;
    }
}
