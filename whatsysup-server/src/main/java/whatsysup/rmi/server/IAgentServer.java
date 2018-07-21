package whatsysup.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import whatsysup.rmi.client.IAgentClient;

public interface IAgentServer
extends Remote {
    public long registServerBy(String var1, IAgentClient var2) throws RemoteException;

    public int doMonitoringJob(long var1, long var3, long var5, int var7) throws RemoteException;

    public String doSystemsJob(long var1) throws RemoteException;

    public String doCpuJob(long var1) throws RemoteException;

    public int saveJobResult(long var1, long var3, byte[] var5, int var6) throws RemoteException;
}