package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class IP4ValidatorTest
{

    @Test
    public void validate()
    {
        IP4Validator v = new IP4Validator();
        try
        {
            InetAddress address = InetAddress.getByName("::1");
            assertFalse(v.validate(address, new DefaultConfiguration()));
            address = InetAddress.getByName("127.0.0.1");
            assertTrue(v.validate(address, new DefaultConfiguration()));
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getException()
    {
        IP4Validator v = new IP4Validator();
        try
        {
            InetAddress address = InetAddress.getByName("::1");
            v.validate(address, new DefaultConfiguration());
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            fail();
        }
        assertNotNull(v.getException());
    }
}