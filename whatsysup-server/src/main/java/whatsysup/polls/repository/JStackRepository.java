package whatsysup.polls.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import whatsysup.polls.model.JStack;

@Repository
public abstract interface JStackRepository
  extends CrudRepository<JStack, Long>
{
  @Query("{'_id': { $gt: ?1, $lt: ?2 }, 'pollId':?0 } ")
  public abstract List<JStack> findByIdTimeRange(long paramLong1, long paramLong2, long paramLong3);
}