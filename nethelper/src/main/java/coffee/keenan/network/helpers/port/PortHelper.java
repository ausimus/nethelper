package coffee.keenan.network.helpers.port;

import coffee.keenan.network.config.DefaultConfiguration;
import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.helpers.ErrorTracking;
import coffee.keenan.network.validators.port.IPortValidator;
import coffee.keenan.network.wrappers.upnp.UPNPService;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collection;

public class PortHelper extends ErrorTracking
{
    private final IConfiguration configuration;

    @SuppressWarnings("WeakerAccess")
    public PortHelper()
    {
        configuration = new DefaultConfiguration();
    }

    @SuppressWarnings("WeakerAccess")
    public PortHelper(final IConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public static Port assignPort(@NotNull final Port port)
    {
        return assignPort(port, new DefaultConfiguration());
    }

    @SuppressWarnings("WeakerAccess")
    public static Port assignPort(@NotNull final Port port, @NotNull final IConfiguration configuration)
    {
        final PortHelper portHelper = new PortHelper(configuration);
        for (final int p : port.getPorts())
        {
            if (portHelper.validatePort(port.getAddress(), p, port.getValidators()))
            {
                port.setAssignedPort(p);
                break;
            }
        }
        if (port.isToMap() && port.getAssignedPort() != 0) UPNPService.getInstance().openPort(port);
        return port;
    }

    @SuppressWarnings("WeakerAccess")
    public boolean validatePort(final InetAddress address, final int port, final IPortValidator... validators)
    {
        return validatePort(address, port, Arrays.asList(validators));
    }

    /**
     * Validates a given {@link InetAddress} and port with the specified {@link IPortValidator}
     *
     * @param address    is the given address to validate
     * @param port       is the port to validate
     * @param validators are the validators to use
     * @return true if all validators pass, otherwise false
     */
    @SuppressWarnings("WeakerAccess")
    public boolean validatePort(final InetAddress address, final int port, final Collection<IPortValidator> validators)
    {
        for (final IPortValidator validator : validators)
        {
            if (!validator.validate(address, configuration, port))
            {
                addException("address: " + address.toString() + ", port: " + String.valueOf(port), validator.getException());
                return false;
            }
        }
        return true;
    }
}
