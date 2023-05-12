package org.only.cowork.service;

import org.only.cowork.entity.Team;

import java.util.List;
import java.util.Set;

/**
 * Team-related service interfaces.
 *
 * @author WangYanpeng
 */
public interface TeamService {
    Team save(String teamName, String teamDescription, Long leaderId, Set<String> teamMembersPhoneNumber);

    void deleteTeam(Long id);

    Team updateTeamInfo(Long id, String teamName, String teamDescription);

    Team addTeamMember(Long id, String newMemberPhoneNumber);

    Team removeTeamMember(Long id, String memberPhoneNumber);

    List<Team> findByTeamLeader(Long id);

    Team findById(Long id);

    List<Team> findByTeamMembersContains(Long id);
}
