package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import static org.junit.Assert.*;

public class LoopbackValidatorTest
{

    @Test
    public void validate()
    {
        LoopbackValidator v = new LoopbackValidator();
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
        LoopbackValidator v = new LoopbackValidator();
        assertFalse(v.validate(null, new DefaultConfiguration()));
        assertNotNull(v.getException());
    }
}