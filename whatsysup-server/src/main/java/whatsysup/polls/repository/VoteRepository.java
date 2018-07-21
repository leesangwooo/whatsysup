package whatsysup.polls.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import whatsysup.polls.model.ChoiceVoteCount;
import whatsysup.polls.model.Vote;

@Repository
public abstract interface VoteRepository
  extends JpaRepository<Vote, Long>
{
  @Query("SELECT NEW whatsysup.polls.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
  public abstract List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(@Param("pollIds") List<Long> paramList);
  
  @Query("SELECT NEW whatsysup.polls.model.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id = :pollId GROUP BY v.choice.id")
  public abstract List<ChoiceVoteCount> countByPollIdGroupByChoiceId(@Param("pollId") Long paramLong);
  
  @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id in :pollIds")
  public abstract List<Vote> findByUserIdAndPollIdIn(@Param("userId") Long paramLong, @Param("pollIds") List<Long> paramList);
  
  @Query("SELECT v FROM Vote v where v.user.id = :userId and v.poll.id = :pollId")
  public abstract Vote findByUserIdAndPollId(@Param("userId") Long paramLong1, @Param("pollId") Long paramLong2);
  
  @Query("SELECT COUNT(v.id) from Vote v where v.user.id = :userId")
  public abstract long countByUserId(@Param("userId") Long paramLong);
  
  @Query("SELECT v.poll.id FROM Vote v WHERE v.user.id = :userId")
  public abstract Page<Long> findVotedPollIdsByUserId(@Param("userId") Long paramLong, Pageable paramPageable);
}