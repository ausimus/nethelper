package coffee.keenan.network.helpers.port;

import coffee.keenan.network.helpers.ErrorTracking;
import coffee.keenan.network.validators.port.IPortValidator;
import coffee.keenan.network.validators.port.TCPValidator;
import coffee.keenan.network.validators.port.UDPValidator;
import org.fourthline.cling.support.model.PortMapping;

import java.net.InetAddress;
import java.util.*;

public class Port extends ErrorTracking
{
    private final List<Integer> ports = new ArrayList<>();
    private final Protocol protocols;
    private final InetAddress address;
    private final Set<IPortValidator> validators = new HashSet<>();
    private String description = "";
    private boolean toMap;
    private int assignedPort;
    private boolean isMapped;

    public Port(final InetAddress address, final Protocol protocol)
    {
        this.address = address;
        this.protocols = protocol;
        switch (getProtocol())
        {
            case TCP:
                validators.add(new TCPValidator());
                break;
            case UDP:
                validators.add(new UDPValidator());
                break;
            case Both:
                validators.add(new TCPValidator());
                validators.add(new UDPValidator());
                break;
        }
    }

    public Port addPort(final int port)
    {
        this.ports.add(port);
        return this;
    }

    public Port addPortRange(final int start, final int end)
    {
        for (int i = start; i <= end; i++)
            this.ports.add(i);
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public Port addPorts(final Integer... ports)
    {
        Collections.addAll(this.ports, ports);
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public Port addPorts(final List<Integer> ports)
    {
        this.ports.addAll(ports);
        return this;
    }

    public Port toMap()
    {
        return this.toMap(true);
    }

    @SuppressWarnings("WeakerAccess")
    public Port toMap(final boolean toMap)
    {
        this.toMap = toMap;
        return this;
    }

    @SuppressWarnings("WeakerAccess")
    public List<Integer> getPorts()
    {
        return ports;
    }

    @SuppressWarnings("WeakerAccess")
    public Protocol getProtocol()
    {
        return protocols;
    }

    @SuppressWarnings("WeakerAccess")
    public String getDescription()
    {
        return description;
    }

    public Port setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public boolean isToMap()
    {
        return toMap;
    }

    public boolean isMapped()
    {
        return isMapped;
    }

    public Port setMapped(final boolean value)
    {
        isMapped = value;
        return this;
    }

    public int getAssignedPort()
    {
        return assignedPort;
    }

    @SuppressWarnings("WeakerAccess")
    public Port setAssignedPort(int assignedPort)
    {
        this.assignedPort = assignedPort;
        return this;
    }

    public InetAddress getAddress()
    {
        return address;
    }

    public PortMapping[] getMappings()
    {
        if (!isToMap()) return new PortMapping[0];

        List<PortMapping> mappings = new ArrayList<>();
        switch (getProtocol())
        {

            case TCP:
                mappings.add(new PortMapping(getAssignedPort(), address.getHostAddress(), PortMapping.Protocol.TCP, getDescription()));
                break;
            case UDP:
                mappings.add(new PortMapping(getAssignedPort(), address.getHostAddress(), PortMapping.Protocol.UDP, getDescription()));
                break;
            case Both:
                mappings.add(new PortMapping(getAssignedPort(), address.getHostAddress(), PortMapping.Protocol.UDP, getDescription()));
                mappings.add(new PortMapping(getAssignedPort(), address.getHostAddress(), PortMapping.Protocol.TCP, getDescription()));
                break;
        }
        return mappings.toArray(new PortMapping[0]);
    }

    public Collection<IPortValidator> getValidators()
    {
        return this.validators;
    }

    @SuppressWarnings("WeakerAccess")
    public Port addValidators(IPortValidator... validators)
    {
        this.validators.addAll(Arrays.asList(validators));
        return this;
    }

    @Override
    public String toString()
    {
        return getDescription() + " (" + getAddress().getHostAddress() + ":" + getAssignedPort() + ")";
    }
}
