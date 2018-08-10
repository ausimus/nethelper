package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.IConfiguration;

import java.net.InetAddress;

public interface IAddressValidator
{
    boolean validate(final InetAddress address, final IConfiguration configuration);

    Exception getException();
}
