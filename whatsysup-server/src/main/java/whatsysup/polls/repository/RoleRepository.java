package whatsysup.polls.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsysup.polls.model.Role;
import whatsysup.polls.model.RoleName;

@Repository
public abstract interface RoleRepository
  extends JpaRepository<Role, Long>
{
  public abstract Optional<Role> findByName(RoleName paramRoleName);
}