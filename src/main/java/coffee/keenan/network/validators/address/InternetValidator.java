package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.IConfiguration;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class InternetValidator implements IAddressValidator
{
    private Exception exception;

    @Override
    public boolean validate(InetAddress address, IConfiguration configuration)
    {
        try (SocketChannel socket = SocketChannel.open())
        {
            socket.socket().setSoTimeout(configuration.getTimeout());
            socket.bind(new InetSocketAddress(address, 0));
            socket.connect(new InetSocketAddress(configuration.getTestUrl(), configuration.getTestPort()));
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
