package coffee.keenan.network.helpers.interfaces;

import coffee.keenan.network.config.DefaultConfiguration;
import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.helpers.ErrorTracking;
import coffee.keenan.network.validators.interfaces.IInterfaceValidator;
import coffee.keenan.network.validators.interfaces.NotLoopbackValidator;
import coffee.keenan.network.validators.interfaces.UpValidator;
import com.sun.istack.internal.NotNull;

import java.net.NetworkInterface;

public class InterfaceHelper extends ErrorTracking
{
    private final IConfiguration configuration;

    private IInterfaceValidator[] interfaceValidators = new IInterfaceValidator[]{
            new NotLoopbackValidator(), new UpValidator()
    };

    @SuppressWarnings("WeakerAccess")
    public InterfaceHelper()
    {
        configuration = new DefaultConfiguration();
    }

    public InterfaceHelper(final IConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @SuppressWarnings("WeakerAccess")
    public IInterfaceValidator[] getInterfaceValidators()
    {
        return interfaceValidators;
    }

    @SuppressWarnings("WeakerAccess")
    public void setInterfaceValidators(@NotNull final IInterfaceValidator... validators)
    {
        interfaceValidators = validators;
    }

    /**
     * Validates a given {@link NetworkInterface} with the specified {@link IInterfaceValidator}
     *
     * @param networkInterface is the given interface to validate
     * @return true if all validators pass, otherwise false
     */
    public boolean validateInterface(final NetworkInterface networkInterface)
    {
        if (networkInterface == null)
        {
            addException("null interface", new Exception("given interface was null"));
            return false;
        }

        for (final IInterfaceValidator validator : getInterfaceValidators())
        {
            if (!validator.validate(networkInterface, configuration))
            {
                addException("interface: " + networkInterface.getDisplayName() + "(" + networkInterface.getName() + ")", validator.getException());
                return false;
            }
        }
        return true;
    }
}
