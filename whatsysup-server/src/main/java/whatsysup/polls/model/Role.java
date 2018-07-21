package whatsysup.polls.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.NaturalId;

@Entity
@javax.persistence.Table(name="roles")
public class Role
{
  @javax.persistence.Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;
  @javax.persistence.Enumerated(EnumType.STRING)
  @NaturalId
  @Column(length=60)
  private RoleName name;
  
  public Role() {}
  
  public Role(RoleName name)
  {
    this.name = name;
  }
  
  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public RoleName getName() {
    return name;
  }
  
  public void setName(RoleName name) {
    this.name = name;
  }
}