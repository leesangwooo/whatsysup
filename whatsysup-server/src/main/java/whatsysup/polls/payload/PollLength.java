package whatsysup.polls.payload;

import javax.validation.constraints.NotNull;

public class PollLength {
  @NotNull
  @javax.validation.constraints.Max(7L)
  private Integer days;
  @NotNull
  @javax.validation.constraints.Max(23L)
  private Integer hours;
  
  public PollLength() {}
  
  public int getDays() {
    return days.intValue();
  }
  
  public void setDays(int days) {
    this.days = Integer.valueOf(days);
  }
  
  public int getHours() {
    return hours.intValue();
  }
  
  public void setHours(int hours) {
    this.hours = Integer.valueOf(hours);
  }
}