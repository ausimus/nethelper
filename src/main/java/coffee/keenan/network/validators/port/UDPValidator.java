package coffee.keenan.network.validators.port;

import coffee.keenan.network.config.IConfiguration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

public class UDPValidator implements IPortValidator
{
    private Exception exception;

    @Override
    public boolean validate(final InetAddress address, final IConfiguration configuration, final int port)
    {
        try (DatagramChannel datagram = DatagramChannel.open())
        {
            datagram.socket().setSoTimeout(configuration.getTimeout());
            datagram.bind(new InetSocketAddress(address, port));
        }
        catch (Exception e)
        {
            exception = e;
            return false;
        }
        return true;
    }

    @Override
    public Exception getException()
    {
        return exception;
    }
}
