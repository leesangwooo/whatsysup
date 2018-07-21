package whatsysup.polls.payload;

import javax.validation.constraints.NotNull;


public class VoteRequest {
  public VoteRequest() {}
  
  public Long getChoiceId() { return choiceId; }
  
  @NotNull
  private Long choiceId;
  public void setChoiceId(Long choiceId) { this.choiceId = choiceId; }
}