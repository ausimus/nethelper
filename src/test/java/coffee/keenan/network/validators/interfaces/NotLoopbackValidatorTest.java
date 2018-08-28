package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import static org.junit.Assert.*;

public class NotLoopbackValidatorTest
{

    @Test
    public void validate()
    {
        NotLoopbackValidator v = new NotLoopbackValidator();
        try
        {
            assertFalse(v.validate(NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress()), new DefaultConfiguration()));
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
        NotLoopbackValidator v = new NotLoopbackValidator();
        assertFalse(v.validate(null, new DefaultConfiguration()));
        assertNotNull(v.getException());
    }
}