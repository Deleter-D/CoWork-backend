package org.only.cowork.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.only.cowork.entity.Team;
import org.only.cowork.entity.User;
import org.only.cowork.repository.TeamRepository;
import org.only.cowork.repository.UserRepository;
import org.only.cowork.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Implementation of team-related service interfaces.
 *
 * @author WangYanpeng
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    /**
     * @param teamName               The name of the team.
     * @param teamDescription        The description of the team.
     * @param leaderId               The leader's id.
     * @param teamMembersPhoneNumber a Set, the members' phone number.
     * @return a team that was created and saved.
     */
    @Override
    public Team save(String teamName, String teamDescription, Long leaderId, Set<String> teamMembersPhoneNumber) {
        User teamLeader = userRepository.findById(leaderId).get();
        try {
            Assert.notNull(teamLeader);
        } catch (IllegalArgumentException e) {
            log.warn("The leader is not exist.");
            throw e;
        }

        HashSet<User> teamMembers = new HashSet<>();
        for (String memPhoneNumber : teamMembersPhoneNumber) {
            User user = userRepository.findByPhoneNumber(memPhoneNumber);
            try {
                Assert.notNull(user);
                teamMembers.add(user);
            } catch (IllegalArgumentException e) {
                log.warn("The phone number " + memPhoneNumber + " is not exist.");
            }
        }
        Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Team team = Team.builder()
                .teamName(teamName)
                .teamDescription(teamDescription)
                .teamLeader(teamLeader)
                .teamMembers(teamMembers)
                .isDeleted(false)
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();

        return teamRepository.save(team);
    }

    /**
     * @param id Team id.
     */
    @Override
    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id).get();
        try {
            Assert.notNull(team);
        } catch (IllegalArgumentException e) {
            log.warn("The team is not exist.");
            throw e;
        }
        team.setDeleted(true);
        teamRepository.save(team);
    }

    /**
     * @param id              Team id.
     * @param teamName        New team name.
     * @param teamDescription New team description.
     * @return the modified team.
     */
    @Override
    public Team updateTeamInfo(Long id, String teamName, String teamDescription) {
        Team team = teamRepository.findById(id).get();
        try {
            Assert.notNull(team);
        } catch (IllegalArgumentException e) {
            log.warn("The team is not exist.");
            throw e;
        }

        if (teamName != null && !teamName.equals("")) {
            team.setTeamName(teamName);
        }
        if (teamDescription != null && !teamDescription.equals("")) {
            team.setTeamDescription(teamDescription);
        }

        return teamRepository.save(team);
    }

    /**
     * Add team member by using the team id and new team member's phone number.
     *
     * @param id                   Team id.
     * @param newMemberPhoneNumber New team member's phone number.
     * @return the updated team.
     */
    @Override
    public Team addTeamMember(Long id, String newMemberPhoneNumber) {
        Team team = teamRepository.findById(id).get();
        try {
            Assert.notNull(team);
        } catch (IllegalArgumentException e) {
            log.warn("The team is not exist.");
            throw e;
        }

        User newMember = userRepository.findByPhoneNumber(newMemberPhoneNumber);
        try {
            Assert.notNull(newMember);
        } catch (IllegalArgumentException e) {
            log.warn("The new member is not exist.");
            throw e;
        }

        Set<User> teamMembers = team.getTeamMembers();
        teamMembers.add(newMember);

        return teamRepository.save(team);
    }

    /**
     * @param id                Team id.
     * @param memberPhoneNumber Phone number of the team member to be removed.
     * @return the updated team.
     */
    @Override
    public Team removeTeamMember(Long id, String memberPhoneNumber) {
        Team team = teamRepository.findById(id).get();
        try {
            Assert.notNull(team);
        } catch (IllegalArgumentException e) {
            log.warn("The team is not exist.");
            throw e;
        }

        User member = userRepository.findByPhoneNumber(memberPhoneNumber);
        try {
            Assert.notNull(member);
        } catch (IllegalArgumentException e) {
            log.warn("The member is not exist.");
            throw e;
        }

        Set<User> teamMembers = team.getTeamMembers();
        try {
            Assert.isTrue(teamMembers.contains(member));
            teamMembers.remove(member);
        } catch (IllegalArgumentException e) {
            log.warn("The member is not in the team.");
            throw e;
        }

        return teamRepository.save(team);
    }

    /**
     * @param id Team leader's id.
     * @return the teams created by this user.
     */
    @Override
    public List<Team> findByTeamLeader(Long id) {
        User leader = userRepository.findById(id).get();
        try {
            Assert.notNull(leader);
        } catch (IllegalArgumentException e) {
            log.warn("Team leader is not exist.");
            throw e;
        }

        return teamRepository.findByTeamLeader(leader);
    }

    /**
     * @param id Team id.
     * @return the team.
     */
    @Override
    public Team findById(Long id) {
        Team team;
        try {
            team = teamRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            log.warn("Team is not exist.");
            throw e;
        }

        return team;
    }

    /**
     * @param id User id.
     * @return the teams joined by the user.
     */
    @Override
    public List<Team> findByTeamMembersContains(Long id) {
        User user;
        try {
            user = userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            log.warn("User is not exist.");
            throw e;
        }
        return teamRepository.findByTeamMembersContains(user);
    }
}
