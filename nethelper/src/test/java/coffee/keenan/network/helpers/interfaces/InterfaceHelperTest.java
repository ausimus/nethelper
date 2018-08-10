package coffee.keenan.network.helpers.interfaces;

import coffee.keenan.network.config.DefaultConfiguration;
import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.validators.interfaces.IInterfaceValidator;
import coffee.keenan.network.validators.interfaces.UpValidator;
import org.junit.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import static org.junit.Assert.*;

public class InterfaceHelperTest
{

    @Test
    public void setInterfaceValidators()
    {
        InterfaceHelper v = new InterfaceHelper(new DefaultConfiguration());
        v.setInterfaceValidators();
        assertEquals(0, v.getInterfaceValidators().length);
    }

    @Test
    public void getInterfaceValidators()
    {
        InterfaceHelper v = new InterfaceHelper();
        v.setInterfaceValidators(new UpValidator());
        assertEquals(1, v.getInterfaceValidators().length);
    }

    @Test
    public void validateInterface()
    {
        InterfaceHelper v = new InterfaceHelper();
        v.setInterfaceValidators(new IInterfaceValidator()
        {
            @Override
            public boolean validate(NetworkInterface networkInterface, IConfiguration configuration)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return new Exception("Intentional failure");
            }
        });
        try
        {
            assertFalse(v.validateInterface(NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress())));
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            fail();
        }
        assertNotEquals(0, v.getExceptions().length);
        v = new InterfaceHelper();
        assertFalse(v.validateInterface(null));
        assertNotEquals(0, v.getExceptions().length);
        v.setInterfaceValidators(new IInterfaceValidator()
        {
            @Override
            public boolean validate(NetworkInterface networkInterface, IConfiguration configuration)
            {
                return true;
            }

            @Override
            public Exception getException()
            {
                return null;
            }
        });
        try
        {
            assertTrue(v.validateInterface(NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress())));
        }
        catch (SocketException e)
        {
            e.printStackTrace();
            fail();
        }
    }
}