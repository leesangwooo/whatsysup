package whatsysup.polls.controller;

import java.net.URI;
import java.rmi.RemoteException;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import whatsysup.polls.model.Poll;
import whatsysup.polls.payload.ApiResponse;
import whatsysup.polls.payload.PagedResponse;
import whatsysup.polls.payload.PollRequest;
import whatsysup.polls.payload.PollResponse;
import whatsysup.polls.payload.VoteRequest;
import whatsysup.polls.repository.PollRepository;
import whatsysup.polls.repository.UserRepository;
import whatsysup.polls.repository.VoteRepository;
import whatsysup.polls.security.CurrentUser;
import whatsysup.polls.security.UserPrincipal;
import whatsysup.polls.service.PollService;
import whatsysup.polls.util.AppConstants;
import whatsysup.rmi.server.IAgentServer;

/**
 * Created by rajeevkumarsingh on 20/11/17.
 */

@RestController
@RequestMapping("/api/polls")
public class PollController {

	@Autowired
	private PollRepository pollRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PollService pollService;

	@Resource
	private IAgentServer iAgentServer;

	private static final Logger logger = LoggerFactory.getLogger(PollController.class);

	@GetMapping
	public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getAllPolls(currentUser, page, size);
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) throws RemoteException {
		System.err.println(pollRequest.getTags());
		Poll poll = pollService.createPoll(pollRequest);
		String msg;
		int rmiResultFlag = iAgentServer.doMonitoringJob(poll.getCreatedBy(), poll.getId(),
				poll.getExpirationDateTime().getEpochSecond(), poll.getCheck_interval());
		if (rmiResultFlag > 0) {
			msg = "Poll Created Successfully";
		} else {
			msg = "Poll Created but Agent is disconnected";
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{pollId}").buildAndExpand(poll.getId())
				.toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, msg));
	}

	@GetMapping("/{pollId}")
	public PollResponse getPollById(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId) {
		return pollService.getPollById(pollId, currentUser);
	}

	@PostMapping("/{pollId}/votes")
	@PreAuthorize("hasRole('USER')")
	public PollResponse castVote(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId,
			@Valid @RequestBody VoteRequest voteRequest) {
		return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
	}


}
