package whatsysup.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;
import whatsysup.rmi.impl.AgentServerImpl;
import whatsysup.rmi.server.IAgentServer;

@Configuration
public class CreateRMIServer {
    public static final String WHATSYSUP_RMI_AGENT = "whatsysup-rmi-agent";
    public static IAgentServer rmiServer;
    private static Registry registry;

    @Bean
    public IAgentServer bindRmiServer() throws RemoteException {
        rmiServer = new AgentServerImpl();
        return rmiServer;
    }

    @Bean
    public RmiServiceExporter exporter() throws RemoteException {
        RmiServiceExporter rse = new RmiServiceExporter();
        rse.setServiceName("whatsysup-rmi-agent");
        rse.setService((Object)this.bindRmiServer());
        rse.setServiceInterface(IAgentServer.class);
        rse.setRegistryPort(2099);
        return rse;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static IAgentServer getRmiServer() {
        if (rmiServer != null) return rmiServer;
        Class<CreateRMIServer> class_ = CreateRMIServer.class;
        synchronized (CreateRMIServer.class) {
            if (rmiServer != null) return rmiServer;
            {
                try {
                    registry = LocateRegistry.createRegistry(2099);
                    rmiServer = new AgentServerImpl();
                    registry.rebind("whatsysup-rmi-agent", (Remote)rmiServer);
                }
                catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            // ** MonitorExit[var0] (shouldn't be in output)
            return rmiServer;
        }
    }
}