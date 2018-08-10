package coffee.keenan.network.validators.interfaces;

import coffee.keenan.network.config.IConfiguration;

import java.net.NetworkInterface;

public class LoopbackValidator implements IInterfaceValidator
{
    private Exception exception;

    @Override
    public boolean validate(final NetworkInterface networkInterface, final IConfiguration configuration)
    {
        try
        {
            return networkInterface.isLoopback();
        }
        catch (Exception e)
        {
            exception = e;
        }
        return false;
    }

    @Override
    public Exception getException()
    {
        return exception == null ? new Exception("interface is not loopback") : exception;
    }
}
