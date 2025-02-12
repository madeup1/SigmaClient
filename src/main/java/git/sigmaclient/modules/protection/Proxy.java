package git.sigmaclient.modules.protection;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.socket.oio.OioSocketChannel;
import git.sigmaclient.SigmaClient;
import git.sigmaclient.modules.Module;
import git.sigmaclient.settings.ModeSetting;
import git.sigmaclient.settings.StringSetting;

import java.net.*;

public class Proxy extends Module {
    public ModeSetting proxyType;
    public StringSetting address;
    public StringSetting port;
    public StringSetting user;
    public StringSetting pass;
    public Proxy()
    {
        super("Proxy", Category.PROTECTIONS);
        this.proxyType = new ModeSetting("Proxy Type", "SOCKS", "HTTP", "SOCKS");
        this.address = new StringSetting("Address");
        this.port = new StringSetting("Port");
        this.user = new StringSetting("User (optional)");
        this.pass = new StringSetting("Password (optional)");
        addSettings(proxyType, address, user, port, pass);
    }

    @Override
    public void assign()
    {
        SigmaClient.proxy = this;
    }

    public java.net.Proxy getProxy()
    {
        if (!isToggled())
            return null;

        try
        {
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName(address.getValue()), Integer.getInteger(port.getValue()));
            java.net.Proxy proxy;
            switch (proxyType.getSelected())
            {
                case "SOCKS":
                    proxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS, addr);
                    break;
                default:
                    proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addr);
                    break;
            }

            if (user.getValue() != "" && pass.getValue() != "")
            {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user.getValue(), pass.getValue().toCharArray());
                    }
                });
            }

            return proxy;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static class ProxyOioChannelFactory implements ChannelFactory<OioSocketChannel> {
        public java.net.Proxy proxy;
        public ProxyOioChannelFactory(java.net.Proxy proxy)
        {
            this.proxy = proxy;
        }
        @Override
        public OioSocketChannel newChannel() {
            return new OioSocketChannel(new Socket(proxy));
        }
    }
}