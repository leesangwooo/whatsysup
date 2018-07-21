package whatsysup.rmi.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgentClient
extends Remote {
    public void execMonitorJob(long var1, long var3, long var5, int var7) throws RemoteException;

    public String scanSystemsJob(long var1) throws RemoteException;

    public String scanCpuJob(long var1) throws RemoteException;

    public String scanCpuTailsJob(long var1) throws RemoteException;
}