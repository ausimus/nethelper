package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import static org.junit.Assert.*;

public class UpValidatorTest
{

    @Test
    public void validate()
    {
        UpValidator v = new UpValidator();
        try
        {
            assertTrue(v.validate(NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress()), new DefaultConfiguration()));
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void getException()
    {
        UpValidator v = new UpValidator();
        v.validate(null, new DefaultConfiguration());
        assertNotNull(v.getException());
    }
}