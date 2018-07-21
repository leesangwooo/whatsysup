package whatsysup.polls.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import whatsysup.polls.model.audit.DateAudit;

@Entity
@Table(name="systems")
public class System
  extends DateAudit
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  private Long user_id;
  private String kernel;
  private String os;
  private String machine;
  private String processor;
  private String hardware;
  private String osReliese;
  private String uptime;
  private String loadAvg;
  private String path;
  private String lang;
  
  public System() {}
}