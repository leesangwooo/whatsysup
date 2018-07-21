package whatsysup.polls.payload;

import javax.validation.constraints.NotBlank;

public class ChoiceRequest {
  @NotBlank
  @javax.validation.constraints.Size(max=1000)
  private String text;
  
  public ChoiceRequest() {}
  
  public String getText() { return text; }
  
  public void setText(String text)
  {
    this.text = text;
  }
}