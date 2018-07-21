package whatsysup.polls.payload;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PollRequest
{
  @NotBlank
  @Size(max=10000)
  private String question;
  @NotNull
  @Size(min=2, max=6)
  @Valid
  private List<ChoiceRequest> choices;
  @NotNull
  @Valid
  private PollLength pollLength;
  @Valid
  private Integer check_interval;
  @Valid
  private String tags;
  
  public PollRequest() {}
  
  public String getQuestion()
  {
    return question;
  }
  
  public void setQuestion(String question) {
    this.question = question;
  }
  
  public List<ChoiceRequest> getChoices() {
    return choices;
  }
  
  public void setChoices(List<ChoiceRequest> choices) {
    this.choices = choices;
  }
  
  public PollLength getPollLength() {
    return pollLength;
  }
  
  public void setPollLength(PollLength pollLength) {
    this.pollLength = pollLength;
  }
  
  public Integer getCheck_interval()
  {
    return check_interval;
  }
  
  public void setCheck_interval(Integer check_interval) {
    this.check_interval = check_interval;
  }
  
  public String getTags() {
    return tags;
  }
  
  public void setTags(String tags) {
    this.tags = tags;
  }
}