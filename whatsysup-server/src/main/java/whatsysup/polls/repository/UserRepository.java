package whatsysup.polls.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsysup.polls.model.User;

@Repository
public abstract interface UserRepository
  extends JpaRepository<User, Long>
{
  public abstract Optional<User> findByEmail(String paramString);
  
  public abstract Optional<User> findByUsernameOrEmail(String paramString1, String paramString2);
  
  public abstract List<User> findByIdIn(List<Long> paramList);
  
  public abstract Optional<User> findByUsername(String paramString);
  
  public abstract Boolean existsByUsername(String paramString);
  
  public abstract Boolean existsByEmail(String paramString);
}