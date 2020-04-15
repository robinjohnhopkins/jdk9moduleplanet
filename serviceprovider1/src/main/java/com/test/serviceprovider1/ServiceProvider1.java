package com.test.serviceprovider1;

import com.test.serviceproviderinterface.spi.ServiceProviderInterface;

public class ServiceProvider1 implements ServiceProviderInterface {
    @Override
    public void printServiceName() {
        System.out.println("This is Service Provider 1");
    }
}