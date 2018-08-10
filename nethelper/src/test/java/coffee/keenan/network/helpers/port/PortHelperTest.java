package coffee.keenan.network.helpers.port;

import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.validators.port.IPortValidator;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.*;

public class PortHelperTest
{
    @Test
    public void validatePort()
    {
        PortHelper p = new PortHelper();
        assertFalse(p.validatePort(InetAddress.getLoopbackAddress(), 10, new IPortValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration, int port)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return new Exception("intentional fail");
            }
        }));

        assertTrue(p.validatePort(InetAddress.getLoopbackAddress(), 10, new IPortValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration, int port)
            {
                return true;
            }

            @Override
            public Exception getException()
            {
                return null;
            }
        }));
    }

    @Test
    public void assignPort()
    {
        Port o = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP)
                .setDescription("Test")
                .addPort(80)
                .toMap();
        Port p = PortHelper.assignPort(o);
        assertEquals(o, p);
        assertEquals(80, p.getAssignedPort());
        o = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP)
                .setDescription("Test")
                .addPort(80)
                .toMap()
                .addValidators(new IPortValidator()
                {
                    @Override
                    public boolean validate(InetAddress address, IConfiguration configuration, int port)
                    {
                        return false;
                    }

                    @Override
                    public Exception getException()
                    {
                        return new Exception("failure");
                    }
                });
        p = PortHelper.assignPort(o);
        assertEquals(o, p);
        assertEquals(0, p.getAssignedPort());
    }
}