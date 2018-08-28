package coffee.keenan.network.validators.port;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class TCPValidatorTest
{

    @Test
    public void validate()
    {
        TCPValidator v = new TCPValidator();
        assertTrue(v.validate(InetAddress.getLoopbackAddress(), new DefaultConfiguration(), 1234));
        assertFalse(v.validate(InetAddress.getLoopbackAddress(), new DefaultConfiguration(), -1));
    }

    @Test
    public void getException()
    {
        TCPValidator v = new TCPValidator();
        v.validate(null, new DefaultConfiguration(), -1);
        assertNotNull(v.getException());
    }
}