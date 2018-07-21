package whatsysup.polls.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import whatsysup.polls.model.Poll;

@Repository
public abstract interface PollRepository
  extends JpaRepository<Poll, Long>
{
  public abstract Optional<Poll> findById(Long paramLong);
  
  public abstract Page<Poll> findByCreatedBy(Long paramLong, Pageable paramPageable);
  
  public abstract long countByCreatedBy(Long paramLong);
  
  public abstract List<Poll> findByIdIn(List<Long> paramList);
  
  public abstract List<Poll> findByIdIn(List<Long> paramList, Sort paramSort);
}