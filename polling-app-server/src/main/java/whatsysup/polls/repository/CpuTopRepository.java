package whatsysup.polls.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import whatsysup.polls.model.CpuTop;

@Repository
public interface CpuTopRepository extends CrudRepository<CpuTop, Long>{
	@Query("{'_id': { $gt: ?1, $lt: ?2 }, 'pollId':?0 } ")
	List<CpuTop> findByIdTimeRange(long pollId, long startDate, long endDate);
}
