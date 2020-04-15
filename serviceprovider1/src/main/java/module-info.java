module serviceprovider1 {
    requires serviceproviderinterface;
    provides com.test.serviceproviderinterface.spi.ServiceProviderInterface with com.test.serviceprovider1.ServiceProvider1;
}