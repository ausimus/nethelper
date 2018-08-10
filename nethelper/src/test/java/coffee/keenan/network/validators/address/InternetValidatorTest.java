package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class InternetValidatorTest
{

    @Test
    public void validate()
    {
        InternetValidator v = new InternetValidator();
        try
        {
            InetAddress address = InetAddress.getByName("0.0.0.0");
            assertTrue(v.validate(address, new DefaultConfiguration()));
            address = InetAddress.getLoopbackAddress();
            assertFalse(v.validate(address, new DefaultConfiguration()));
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
        InternetValidator v = new InternetValidator();
        assertFalse(v.validate(InetAddress.getLoopbackAddress(), new DefaultConfiguration()));
        assertNotNull(v.getException());
    }
}