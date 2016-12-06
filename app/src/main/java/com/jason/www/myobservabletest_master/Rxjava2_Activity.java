package com.jason.www.myobservabletest_master;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 根据输入的字符串返回一个网站的url列表
 * 1.粗暴的方法
 * 2.利用from()操作符 稍加改动 Observable.from()方法，它接收一个集合作为输入，然后每次输出一个元素给subscriber
 * 3.flatMap() 操作符 改进 Observable.flatMap()接收一个Observable的输出作为输入，同时输出另外一个Observable
 * 4.flatMap() 操作符  接着前面的例子，现在我不想打印URL了，而是要打印收到的每个网站的标题。
 * 问题来了，我的方法每次只能传入一个URL，并且返回值不是一个String，而是一个输出String的Observabl对象。
 * 使用flatMap()可以简单的解决这个问题
 */
public class Rxjava2_Activity extends AppCompatActivity
{

    ArrayList<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_2);

        urls = new ArrayList<>();

        /**
         *  1.不能容忍的使用方法,要for each遍历，很蛋疼
         */
       /* query("hellow world").subscribe(new Action1<ArrayList<String>>()
        {
            @Override
            public void call(ArrayList<String> strings)
            {
                for (String url : strings)
                {
                    System.out.println("-------query------:" + url);
                }
            }
        });*/

        /**
         *   2.虽然去掉了for each循环，但是代码依然看起来很乱。多个嵌套的subscription不仅看起来很丑，难以修改，更严重的是它会破坏某些我们现在还没有讲到的RxJava的特性
         */
       /* query("hellow world").subscribe(new Action1<ArrayList<String>>()
        {
            @Override
            public void call(ArrayList<String> strings)
            {
                Observable.from(strings).subscribe(new Action1<String>()
                {
                    @Override
                    public void call(String s)
                    {
                        System.out.println("-------from------:" + s);
                    }
                });
            }
        });*/

        /**
         *   3
         *
         *   flatMap()是不是看起来很奇怪？为什么它要返回另外一个Observable呢？
         *   理解flatMap的关键点在于，flatMap输出的新的Observable正是我们在Subscriber想要接收的。
         *   现在Subscriber不再收到List<String>，而是收到一些列单个的字符串，就像Observable.from()的输出一样。
         *
         */

      /*  query("Hello, world!")
                .flatMap(new Func1<List<String>, Observable<String>>()
                {
                    @Override
                    public Observable<String> call(List<String> urls)
                    {
                        return Observable.from(urls);
                    }
                })
                .subscribe(new Action1<String>()
                {
                    @Override
                    public void call(String s)
                    {
                        System.out.println("--------flatMap-------:" + s);
                    }
                });*/


        /**
         *
         *   4
         *
         *  是不是感觉很不可思议？我竟然能将多个独立的返回Observable对象的方法组合在一起！帅呆了！
         *  不止这些，我还将两个API的调用组合到一个链式调用中了。我们可以将任意多个API调用链接起来。
         *  大家应该都应该知道同步所有的API调用，然后将所有API调用的回调结果组合成需要展示的数据是一件多么蛋疼的事情。
         *  这里我们成功的避免了callback hell（多层嵌套的回调，导致代码难以阅读维护）。
         *  现在所有的逻辑都包装成了这种简单的响应式调用。
         *
         */

        query("Hellow World , Jason ,this is not training !").flatMap(new Func1<ArrayList<String>, Observable<String>>()
        {
            @Override
            public Observable<String> call(ArrayList<String> urls)
            {
                return Observable.from(urls);
            }
        }).flatMap(new Func1<String, Observable<String>>()
        {
            @Override
            public Observable<String> call(String s)
            {
                return getTitle(s);
            }
        }).subscribe(new Action1<String>()
        {
            @Override
            public void call(String s)
            {
                System.out.println("-------flatMap---getTitle-----:" + s);
            }
        });


        /**
         *  5.如果url不存在,getTitle()返回null,我们不想输出"null"，那么我们可以从返回的title列表中过滤掉null值！
         */

        query("Hellow World , Jason ,this is not training !").flatMap(new Func1<ArrayList<String>, Observable<String>>()
        {
            @Override
            public Observable<String> call(ArrayList<String> strings)
            {
                return Observable.from(urls);
            }
        }).flatMap(new Func1<String, Observable<String>>()
        {
            @Override
            public Observable<String> call(String s)
            {
                return getTitle(s);
            }
            // filter()输出和输入相同的元素，并且会过滤掉那些不满足检查条件的
        }).filter(new Func1<String, Boolean>()
        {
            @Override
            public Boolean call(String title)
            {
                return title != null;
            }
            // take()如果我们只想要最多5个结果
            // doOnNext()允许我们在每次输出一个元素之前做一些额外的事情，比如保存标题到磁盘。
        }).take(5)
                .subscribe(new Action1<String>()
                {
                    @Override
                    public void call(String s)
                    {
                        System.out.println("-------flatMap---filter---getTitle-----:");
                    }
                });
    }

    /**
     * 这个方法根据输入的字符串返回一个网站的url列表
     *
     * @param text
     * @return
     */
    public Observable<ArrayList<String>> query(String text)
    {
        for (int i = 0; i < text.length(); i++)
        {
            String s = text.substring(i, i + 1);
            urls.add(s);
        }
        return Observable.just(urls);
    }

    /**
     * 得到url的title
     *
     * @param url
     * @return
     */
    public Observable<String> getTitle(String url)
    {
        String title = url + "哈哈" + Math.random() * 10;
        return Observable.just(title);
    }
}
