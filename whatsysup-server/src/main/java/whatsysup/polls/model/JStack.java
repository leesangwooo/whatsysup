package whatsysup.polls.model;

import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;








@Document(collection="jstack")
public class JStack
{
  @Id
  private Long timeKey;
  private Long userId;
  private Long pollId;
  private Map<String, String> values;
  
  public JStack() {}
  
  public JStack(Long timeKey, Long userId, Long pollId, Map<String, String> values)
  {
    this.timeKey = timeKey;
    this.userId = userId;
    this.pollId = pollId;
    this.values = values;
  }
  
  public Long getTimeKey() {
    return timeKey;
  }
  
  public void setTimeKey(Long timeKey) {
    this.timeKey = timeKey;
  }
  
  public Long getUserId() {
    return userId;
  }
  
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  
  public Long getPollId() {
    return pollId;
  }
  
  public void setPollId(Long pollId) {
    this.pollId = pollId;
  }
  
  public Map<String, String> getValues() {
    return values;
  }
  
  public void setValues(Map<String, String> values) {
    this.values = values;
  }
}