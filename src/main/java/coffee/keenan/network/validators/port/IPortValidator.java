package coffee.keenan.network.validators.port;

import coffee.keenan.network.config.IConfiguration;

import java.net.InetAddress;

public interface IPortValidator
{
    boolean validate(final InetAddress address, final IConfiguration configuration, final int port);

    Exception getException();
}
