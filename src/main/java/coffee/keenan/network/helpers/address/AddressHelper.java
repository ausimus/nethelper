package coffee.keenan.network.helpers.address;

import coffee.keenan.network.config.DefaultConfiguration;
import coffee.keenan.network.config.IConfiguration;
import coffee.keenan.network.helpers.ErrorTracking;
import coffee.keenan.network.helpers.interfaces.InterfaceHelper;
import coffee.keenan.network.validators.address.IAddressValidator;
import coffee.keenan.network.validators.address.IP4Validator;
import coffee.keenan.network.validators.address.InternetValidator;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddressHelper extends ErrorTracking
{
    private final IConfiguration configuration;
    private Set<IAddressValidator> addressValidators
            = new HashSet<>(Stream.of(new IP4Validator(), new InternetValidator()).collect(Collectors.toList()));

    @SuppressWarnings("WeakerAccess")
    public AddressHelper()
    {
        this.configuration = new DefaultConfiguration();
    }

    @SuppressWarnings("WeakerAccess")
    public AddressHelper(IConfiguration configuration)
    {
        this.configuration = configuration;
    }

    @Nullable
    public static InetAddress getFirstValidAddress()
    {
        return getFirstValidAddress(new DefaultConfiguration());
    }

    /**
     * Returns the first valid {@link InetAddress} based on the provided addressValidator validators.
     *
     * @return a valid InetAddress or null if one is not found
     * @see IAddressValidator
     */
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public static InetAddress getFirstValidAddress(final IConfiguration configuration)
    {
        InterfaceHelper interfaceHelper = new InterfaceHelper(configuration);
        AddressHelper addressHelper = new AddressHelper(configuration);
        List<NetworkInterface> interfaces = null;
        try
        {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        for (final NetworkInterface networkInterface : Objects.requireNonNull(interfaces))
        {
            if (!interfaceHelper.validateInterface(networkInterface))
                continue;
            for (InetAddress inetAddress : Collections.list(networkInterface.getInetAddresses()))
                if (addressHelper.validateAddress(inetAddress))
                    return inetAddress;
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public void addAddressValidators(@NotNull IAddressValidator... validators)
    {
        addressValidators.addAll(Arrays.asList(validators));
    }

    @SuppressWarnings("WeakerAccess")
    public Collection<IAddressValidator> getAddressValidators()
    {
        return addressValidators;
    }

    @SuppressWarnings("WeakerAccess")
    public void setAddressValidators(@NotNull IAddressValidator... validators)
    {
        addressValidators = new HashSet<>(Arrays.asList(validators));
    }

    /**
     * Validates a given {@link InetAddress} with the specified {@link IAddressValidator}
     *
     * @param address is the given address to validate
     * @return true if all validators pass, otherwise false
     */
    @SuppressWarnings("WeakerAccess")
    public boolean validateAddress(final InetAddress address)
    {
        for (final IAddressValidator validator : getAddressValidators())
        {
            if (!validator.validate(address, configuration))
            {
                addException("address: " + address.toString(), validator.getException());
                return false;
            }
        }
        return true;
    }
}
