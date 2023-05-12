package org.only.cowork.repository;

import org.only.cowork.entity.Team;
import org.only.cowork.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Team-related Repository interface.
 *
 * @author WangYanpeng
 */
@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {
    List<Team> findByTeamLeader(User leader);

    List<Team> findByTeamMembersContains(User user);
}
