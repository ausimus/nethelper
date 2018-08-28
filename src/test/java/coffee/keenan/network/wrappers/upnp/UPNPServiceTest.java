package coffee.keenan.network.wrappers.upnp;

import coffee.keenan.network.helpers.port.Port;
import coffee.keenan.network.helpers.port.Protocol;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertNull;

public class UPNPServiceTest
{

    @Test
    public void openPort()
    {
        assertNull(UPNPService.getWanService());
        assertNull(UPNPService.getRouterDevice());
        UPNPService.getInstance().openPort(new Port(InetAddress.getLoopbackAddress(), Protocol.TCP).toMap());
        assertNull(UPNPService.getWanService());
        assertNull(UPNPService.getRouterDevice());
    }
}