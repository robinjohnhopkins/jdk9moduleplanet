import com.test.serviceproviderinterface.spi.ServiceProviderInterface;
import com.test.serviceprovider2.ServiceProvider2;

module serviceprovider2 {
    requires serviceproviderinterface;
    provides ServiceProviderInterface with ServiceProvider2;
}