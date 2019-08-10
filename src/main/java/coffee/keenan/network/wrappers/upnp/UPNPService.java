package coffee.keenan.network.wrappers.upnp;

import coffee.keenan.network.helpers.port.Port;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.igd.callback.PortMappingAdd;
import org.fourthline.cling.support.model.PortMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public enum UPNPService
{
    INSTANCE;

    private static final Logger logger = Logger.getLogger(String.valueOf(UPNPService.class));
    private final Object o = new Object();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private boolean initialized = false;
    private UpnpService upnpService;
    private RemoteDevice router;
    private Service wanService;

    UPNPService()
    {
    }

    public static void initialize()
    {
        if (getInstance().initialized) return;
        getInstance().upnpService = new UpnpServiceImpl();
        getInstance().upnpService.getRegistry().addListener(new FindRouterListener());
        getInstance().initialized = true;
        getInstance().refresh();
        Runtime.getRuntime().addShutdownHook(new Thread(getInstance().upnpService::shutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(getInstance()::shutdownExecutor));
    }

    public static void shutdown()
    {
        if (!getInstance().initialized) return;
        getInstance().shutdownExecutor();
        getInstance().upnpService.shutdown();
    }

    public static UPNPService getInstance()
    {
        return INSTANCE;
    }

    static RemoteDevice getRouterDevice()
    {
        return getInstance().router;
    }

    static Service getWanService()
    {
        return getInstance().wanService;
    }

    private void shutdownExecutor()
    {
        if (!initialized) return;
        try
        {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            executorService.shutdownNow();
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void refresh()
    {
        if (!initialized) return;
        synchronized (o)
        {
            wanService = null;
            router = null;
            upnpService.getControlPoint().search();
        }
    }

    public synchronized void setRouterAndService(final RemoteDevice device, final Service service)
    {
        if (!initialized) return;
        synchronized (o)
        {
            this.wanService = service;
            this.router = device;
            o.notifyAll();
        }
    }

    public void openPort(final Port port)
    {
        if (!initialized)
        {
            logger.info("uPNP support not initialized, skipping mapping for " + port.toString());
            return;
        }
        for (final PortMapping portMapping : port.getMappings())
        {
            executorService.submit(() -> {
                // If wanService is null, we're probably waiting on a refresh
                if (wanService == null)
                {
                    synchronized (o)
                    {
                        try
                        {
                            o.wait(1000 * 5);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                            port.addException(port.toString(), e);
                            return;
                        }
                    }
                }
                upnpService.getControlPoint().execute(new PortMappingAdd(wanService, portMapping)
                {
                    @Override
                    public void success(ActionInvocation actionInvocation)
                    {
                        port.setMapped(true);
                        logger.info("port mapped for " + port.toString());
                    }

                    @Override
                    public void failure(ActionInvocation actionInvocation, UpnpResponse upnpResponse, String s)
                    {
                        logger.warning("unable to map port for " + port.toString());
                        port.addException(port.toString(), new Exception(s));
                    }
                });
            });
        }
    }
}
