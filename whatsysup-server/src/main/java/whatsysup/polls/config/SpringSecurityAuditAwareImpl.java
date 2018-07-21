package whatsysup.polls.config;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import whatsysup.polls.security.UserPrincipal;












class SpringSecurityAuditAwareImpl
  implements AuditorAware<Long>
{
  SpringSecurityAuditAwareImpl() {}
  
  public Optional<Long> getCurrentAuditor()
  {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
    if ((authentication == null) || 
      (!authentication.isAuthenticated()) || 
      ((authentication instanceof AnonymousAuthenticationToken))) {
      return Optional.empty();
    }
    
    UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
    
    return Optional.ofNullable(userPrincipal.getId());
  }
}