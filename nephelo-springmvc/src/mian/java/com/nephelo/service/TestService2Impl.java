package com.nephelo.service;

import com.nephelo.annotation.MyService;

@MyService
public class TestService2Impl implements TestService2 {
    @Override
    public void doServiceTest() {
        System.out.println("业务层执行方法了2222222222222");
    }
}