package coffee.keenan.network.validators.address;

import coffee.keenan.network.config.IConfiguration;

import java.net.Inet4Address;
import java.net.InetAddress;

public class IP4Validator implements IAddressValidator
{
    @Override
    public boolean validate(InetAddress address, IConfiguration configuration)
    {
        return address instanceof Inet4Address;
    }

    @Override
    public Exception getException()
    {
        return new Exception("address is not an instance of Inet4Address");
    }
}
