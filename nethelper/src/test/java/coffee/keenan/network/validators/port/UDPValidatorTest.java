package coffee.keenan.network.validators.port;

import coffee.keenan.network.config.DefaultConfiguration;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class UDPValidatorTest
{

    @Test
    public void validate()
    {
        UDPValidator v = new UDPValidator();
        assertTrue(v.validate(InetAddress.getLoopbackAddress(), new DefaultConfiguration(), 1234));
        assertFalse(v.validate(InetAddress.getLoopbackAddress(), new DefaultConfiguration(), -1));
    }

    @Test
    public void getException()
    {
        UDPValidator v = new UDPValidator();
        v.validate(null, new DefaultConfiguration(), -1);
        assertNotNull(v.getException());
    }
}