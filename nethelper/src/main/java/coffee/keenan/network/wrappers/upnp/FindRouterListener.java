package coffee.keenan.network.wrappers.upnp;

import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.support.igd.PortMappingListener;

class FindRouterListener extends DefaultRegistryListener
{
    private final ServiceType[] searchServices = new ServiceType[]{
            PortMappingListener.IP_SERVICE_TYPE,
            PortMappingListener.PPP_SERVICE_TYPE
    };

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device)
    {
        super.remoteDeviceAdded(registry, device);
        if (UPNPService.getRouterDevice() != null && UPNPService.getWanService() != null) return;
        for (ServiceType serviceType : searchServices)
        {
            Service service = device.findService(serviceType);
            if (service != null)
            {
                UPNPService.getInstance().setRouterAndService(device, service);
                return;
            }
        }
    }
}
