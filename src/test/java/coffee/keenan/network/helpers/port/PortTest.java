package coffee.keenan.network.helpers.port;

import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.validators.port.IPortValidator;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class PortTest
{

    @Test
    public void addPort()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getPorts().size());
        p.addPort(10);
        p.addPort(20);
        assertEquals(2, p.getPorts().size());
    }

    @Test
    public void addPorts()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getPorts().size());
        Port p2 = p.addPorts(Stream.of(10, 20, 30).collect(Collectors.toList()));
        assertEquals(p, p2);
        assertEquals(3, p.getPorts().size());
        Port p3 = p2.addPorts(50, 60, 70);
        assertEquals(6, p.getPorts().size());
        assertEquals(p2, p3);
    }

    @Test
    public void addPortRange()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getPorts().size());
        Port p2 = p.addPortRange(10, 20);
        assertEquals(p, p2);
        assertEquals(11, p2.getPorts().size());
    }

    @Test
    public void toMap()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertFalse(p.isToMap());
        p.toMap();
        assertTrue(p.isToMap());
        p.toMap(false);
        assertFalse(p.isToMap());
    }

    @Test
    public void getPorts()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getPorts().size());
        p.addPort(10);
        assertTrue(p.getPorts() instanceof List);
        assertNotNull(p.getPorts().get(0));
        assertEquals(1, p.getPorts().size());
    }

    @Test
    public void getProtocols()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP);
        assertEquals(Protocol.UDP, p.getProtocol());
    }

    @Test
    public void getDescription()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals("", p.getDescription());
    }

    @Test
    public void setDescription()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP).setDescription("Test");
        assertEquals("Test", p.getDescription());
    }

    @Test
    public void isToMap()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP).toMap();
        assertTrue(p.isToMap());
    }

    @Test
    public void setMapped()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP);
        assertFalse(p.isMapped());
        Port p2 = p.setMapped(true);
        assertEquals(p, p2);
        assertTrue(p.isMapped());
        p.setMapped(false);
        assertFalse(p.isMapped());
    }

    @Test
    public void isMapped()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertFalse(p.isMapped());
    }

    @Test
    public void getAssignedPort()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getAssignedPort());
    }

    @Test
    public void setAssignedPort()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(0, p.getAssignedPort());
        Port p2 = p.setAssignedPort(99);
        assertEquals(p, p2);
        assertEquals(99, p.getAssignedPort());
    }

    @Test
    public void getAddress()
    {
        InetAddress address = InetAddress.getLoopbackAddress();
        Port p = new Port(address, Protocol.TCP);
        assertEquals(address, p.getAddress());
    }

    @Test
    public void getMappings()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP).addPort(10);
        assertEquals(0, p.getMappings().length);
        p.toMap();
        assertEquals(1, p.getMappings().length);
        assertNotNull(p.getMappings()[0]);
        p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP).addPort(10);
        assertEquals(0, p.getMappings().length);
        p.toMap();
        assertEquals(1, p.getMappings().length);
        assertNotNull(p.getMappings()[0]);
        p = new Port(InetAddress.getLoopbackAddress(), Protocol.Both).addPort(10);
        assertEquals(0, p.getMappings().length);
        p.toMap();
        assertEquals(2, p.getMappings().length);
        assertNotNull(p.getMappings()[0]);
    }

    @Test
    public void getValidators()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(1, p.getValidators().size());
        p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP);
        assertEquals(1, p.getValidators().size());
        p = new Port(InetAddress.getLoopbackAddress(), Protocol.Both);
        assertEquals(2, p.getValidators().size());
    }

    @Test
    public void setValidators()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.TCP);
        assertEquals(1, p.getValidators().size());
        Port p2 = p.addValidators(new IPortValidator()
        {
            @Override
            public boolean validate(InetAddress address, IConfiguration configuration, int port)
            {
                return false;
            }

            @Override
            public Exception getException()
            {
                return null;
            }
        });
        assertEquals(p, p2);
        assertEquals(2, p.getValidators().size());
    }

    @Test
    public void toString_Test()
    {
        Port p = new Port(InetAddress.getLoopbackAddress(), Protocol.UDP).setDescription("TestPort1");
        assertTrue(p.toString().contains("TestPort1"));
    }
}