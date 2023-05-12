package org.only.cowork.test.test_repository;

import org.junit.jupiter.api.Test;
import org.only.cowork.entity.Team;
import org.only.cowork.entity.User;
import org.only.cowork.repository.TeamRepository;
import org.only.cowork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TeamRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void saveTest() {
        User leader = userRepository.findById(1L).get();
        User mem1 = userRepository.findById(6L).get();
        User mem2 = userRepository.findById(7L).get();


        Set<User> members = new HashSet<>();
        members.add(mem1);
        members.add(mem2);

        Date currentTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Team team = Team.builder()
                .teamName("Team")
                .teamDescription("This is a team.")
                .teamLeader(leader)
                .teamMembers(members)
                .isDeleted(false)
                .createTime(currentTime)
                .updateTime(currentTime)
                .build();

        Team save = teamRepository.save(team);
        System.out.println(save);
    }

    @Test
    public void findByIdTest() {
        Team team = teamRepository.findById(2L).get();
        Set<User> teamMembers = team.getTeamMembers();
        for (User user : teamMembers) {
            System.out.println(user);
        }
    }

    @Test
    public void findByTeamLeader() {
        User leader = userRepository.findById(5L).get();
        List<Team> myTeam = teamRepository.findByTeamLeader(leader);
        for (Team team : myTeam) {
            System.out.println(team);
        }
    }

    @Test
    public void findByTeamMembersContainsTest() {
        User user = userRepository.findById(5L).get();
        List<Team> teams = teamRepository.findByTeamMembersContains(user);
        for (Team team : teams) {
            System.out.println(team);
        }
    }
}
