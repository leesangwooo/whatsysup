package whatsysup.rmi.impl;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import whatsysup.polls.exception.ResourceNotFoundException;
import whatsysup.polls.model.User;
import whatsysup.polls.repository.CpuTopRepository;
import whatsysup.polls.repository.JStackRepository;
import whatsysup.polls.repository.UserRepository;
import whatsysup.polls.util.ModelMapper;
import whatsysup.rmi.client.IAgentClient;
import whatsysup.rmi.server.IAgentServer;

public class AgentServerImpl
implements IAgentServer,
Serializable {
    @Autowired
    private JStackRepository jStackRepository;
    @Autowired
    private CpuTopRepository cpuTopRepository;
    @Autowired
    public UserRepository userRepository;
    public Map<Long, IAgentClient> clientsMap = new HashMap<Long, IAgentClient>();

    public long registServerBy(String email, IAgentClient client) throws RemoteException {
        if (this.userRepository == null) {
            System.err.println("hell no");
            return 0;
        }
        User user = (User)this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", (Object)email));
        this.clientsMap.put((long)user.getId(), client);
        return user.getId();
    }

    public int doMonitoringJob(long userId, long pollId, long expiration, int check_interval) throws RemoteException {
        IAgentClient currentClient = null;
        if (this.clientsMap.get(userId) == null) {
            return 0;
        }
        currentClient = this.clientsMap.get(userId);
        currentClient.execMonitorJob(userId, pollId, expiration, check_interval);
        return 1;
    }

    public String doSystemsJob(long id) throws RemoteException {
        IAgentClient currentClient = null;
        String jsonResult = "";
        if (this.clientsMap.get(id) != null) {
            currentClient = this.clientsMap.get(id);
            jsonResult = currentClient.scanSystemsJob(id);
        }
        return jsonResult;
    }

    public String doCpuJob(long id) throws RemoteException {
        IAgentClient currentClient = null;
        String jsonResult = "";
        if (this.clientsMap.get(id) != null) {
            currentClient = this.clientsMap.get(id);
            jsonResult = currentClient.scanCpuJob(id);
        }
        return jsonResult;
    }

    public String doCpuTailsJob(long id) throws RemoteException {
        IAgentClient currentClient = null;
        String jsonResult = "";
        if (this.clientsMap.get(id) != null) {
            currentClient = this.clientsMap.get(id);
            jsonResult = currentClient.scanCpuTailsJob(id);
        }
        return jsonResult;
    }

    public int saveJobResult(long userId, long pollId, byte[] input, int flag) throws RemoteException {
        int status;
        status = 1;
        try {
            String jobResult = new String(input, StandardCharsets.UTF_8);
            switch (flag) {
                case 1: {
                    this.jStackRepository.save(ModelMapper.jstackResponseMapper((Long)userId, (Long)pollId, (String)jobResult));
                    break;
                }
                case 2: {
                    this.cpuTopRepository.save(ModelMapper.cpuTopResponseMapper((Long)userId, (Long)pollId, (String)jobResult));
                    break;
                }
                default: {
                    return 0;
                }
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            status = 0;
        }
        return status;
    }
}