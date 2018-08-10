package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.IConfiguration;

import java.net.NetworkInterface;

public interface IInterfaceValidator
{
    boolean validate(final NetworkInterface networkInterface, @SuppressWarnings("unused") final IConfiguration configuration);

    Exception getException();
}
