package com.huige.kthttp;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 所属系统: K信移动Android端
 * 所属模块: 线程工具模块
 * 功能描述: 自定义创建线程的名字
 * 创建时间: 2021/1/28 16:40
 * 维护人: 李主辉
 * Copyright @ Jerrisoft 2020. All rights reserved.
 * ┌──────────────────────────────────────────────────────────────┐
 * │ 此技术信息为本公司机密信息，未经本公司书面同意禁止向第三方披露．   │
 * │ 版权所有：深圳市杨梅红艺术教育集团有限公司                                │
 * └──────────────────────────────────────────────────────────────┘
 */
public class CustomNamedThreadFactory implements ThreadFactory {
    private final ThreadFactory mDefaultThreadFactory;
    private final String mBaseName;
    private final AtomicInteger mCount = new AtomicInteger(0);

    public CustomNamedThreadFactory(final String baseName) {
        mDefaultThreadFactory = Executors.defaultThreadFactory();
        mBaseName = baseName;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = mDefaultThreadFactory.newThread(runnable);
        thread.setName(mBaseName + "-" + mCount.getAndIncrement());
        return thread;
    }
}
