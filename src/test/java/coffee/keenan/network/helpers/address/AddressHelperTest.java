package coffee.keenan.network.helpers.address;

import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.validators.address.IAddressValidator;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;

import static org.junit.Assert.*;

public class AddressHelperTest
{
    @Test
    public void setAddressValidators()
    {
        AddressHelper v = new AddressHelper();
        v.setAddressValidators(new IAddressValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return new Exception("intentional failure");
            }
        });
        assertEquals(1, v.getAddressValidators().size());
    }

    @Test
    public void addAddressValidators()
    {
        AddressHelper v = new AddressHelper();
        v.addAddressValidators(new IAddressValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return new Exception("intentional failure");
            }
        });
        assertEquals(3, v.getAddressValidators().size());
    }

    @Test
    public void getAddressValidators()
    {
        AddressHelper v = new AddressHelper();
        v.setAddressValidators();
        assertEquals(0, v.getAddressValidators().size());
    }

    @Test
    public void validateAddress()
    {
        AddressHelper v = new AddressHelper();
        v.setAddressValidators(new IAddressValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return new Exception("intentional failure");
            }
        });
        assertFalse(v.validateAddress(InetAddress.getLoopbackAddress()));
        assertNotEquals(0, v.getExceptions().length);
    }

    @Test
    public void getFirstValidAddress()
    {
        try
        {
            InetAddress address = AddressHelper.getFirstValidAddress();
            if (address != null) assertTrue(address instanceof Inet4Address);
            address = AddressHelper.getFirstValidAddress(new IConfiguration()
            {
                @Override
                public int getTimeout()
                {
                    return 100;
                }

                @Override
                public String getTestUrl()
                {
                    return "failure.is.an.option";
                }

                @Override
                public int getTestPort()
                {
                    return 80;
                }
            });
            assertNull(address);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}