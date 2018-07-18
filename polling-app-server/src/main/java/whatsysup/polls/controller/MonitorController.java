package whatsysup.polls.controller;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import whatsysup.polls.model.CpuTop;
import whatsysup.polls.model.JStack;
import whatsysup.polls.repository.CpuTopRepository;
import whatsysup.polls.repository.JStackRepository;
import whatsysup.polls.repository.UserRepository;
import whatsysup.polls.security.CurrentUser;
import whatsysup.polls.security.UserPrincipal;
import whatsysup.polls.util.ModelMapper;
import whatsysup.rmi.server.IAgentServer;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JStackRepository JStackRepository;

	@Autowired
	private CpuTopRepository cpuTopRepository;

	@Resource
	private IAgentServer iAgentServer;

	@RequestMapping("{username}/systems")
	@PreAuthorize("hasRole('USER')")
	public Map<String, Object> getUserSystemInfo(@PathVariable String username,
			@CurrentUser UserPrincipal currentUser) {

		String execResult = "";
		String message = "Failed... try again!";
		if (userRepository.existsByUsername(username)) {
			try {
				execResult = iAgentServer.doSystemsJob(currentUser.getId());
				message = "Success Systems scanning!";
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return ModelMapper.gsonResponseMapper(execResult, message);
	}

	@RequestMapping("{username}/cpu")
	@PreAuthorize("hasRole('USER')")
	public Map<String, Object> getUserCpu(@PathVariable String username, @CurrentUser UserPrincipal currentUser) {
		
		String execResult = "";
		String message = "Failed... try again!";
		if (userRepository.existsByUsername(username)) {
			try {
				
				execResult = iAgentServer.doCpuJob(currentUser.getId());
				System.err.println("exec : "+execResult);
				message = "Success Systems scanning!";
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		return ModelMapper.gsonResponseMapper(execResult, message);
	}

	@RequestMapping("cpu/{pollId}/top")
	@PreAuthorize("hasRole('USER')")
	public List<CpuTop> getUserCpuTops(@PathVariable Long pollId,
			@RequestParam(name = "from", required = false) Long startDate,
			@RequestParam(name = "to", required = false) Long endDate) {
		if (pollId != null) {
			if (startDate == null)
				startDate = 0l;
			if (endDate == null)
				endDate = new Date().getTime();
		} else {
			throw new NullPointerException("check poll id, please.");
		}
		return cpuTopRepository.findByIdTimeRange(pollId, startDate, endDate);
	}

	@RequestMapping("stack/{pollId}")
	@PreAuthorize("hasRole('USER')")
	public List<JStack> getStackTrace(@PathVariable Long pollId,
			@RequestParam(name = "from", required = false) Long startDate,
			@RequestParam(name = "to", required = false) Long endDate) {
		if (pollId != null) {
			if (startDate == null)
				startDate = 0l;
			if (endDate == null)
				endDate = new Date().getTime();
		} else {
			throw new NullPointerException("check poll id, please.");
		}
		
		
		return JStackRepository.findByIdTimeRange(pollId, startDate, endDate);
	}

}
