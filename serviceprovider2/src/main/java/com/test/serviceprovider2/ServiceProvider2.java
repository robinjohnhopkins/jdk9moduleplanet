package com.test.serviceprovider2;

import com.test.serviceproviderinterface.spi.ServiceProviderInterface;

public class ServiceProvider2 implements ServiceProviderInterface {
    @Override
    public void printServiceName() {
        System.out.println("This is Service Provider 2");
    }
}