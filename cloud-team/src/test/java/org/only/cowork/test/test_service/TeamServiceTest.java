package org.only.cowork.test.test_service;

import org.junit.jupiter.api.Test;
import org.only.cowork.entity.Team;
import org.only.cowork.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Test
    public void saveTest() {
        String teamName = "gold team 2";
        String teamDescription = "This is a gold team.";
        Long leaderId = 1L;
        Set<String> teamMembersPhoneNumber = new HashSet<>();
        teamMembersPhoneNumber.add("13399450113");
        teamMembersPhoneNumber.add("13399450112");

        Team team = teamService.save(teamName, teamDescription, leaderId, teamMembersPhoneNumber);
        System.out.println(team);
    }

    @Test
    public void addTeamMemberTest() {
        Team team = teamService.addTeamMember(3L, "13399450113");
        System.out.println(team);
    }

    @Test
    public void removeTeamMemberTest() {
        Team team = teamService.removeTeamMember(1L, "13399450113");
        System.out.println(team);
    }
}
