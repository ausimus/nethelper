package coffee.keenan.network.config;

public interface IConfiguration
{
    int getTimeout();

    String getTestUrl();

    @SuppressWarnings("SameReturnValue")
    int getTestPort();
}
