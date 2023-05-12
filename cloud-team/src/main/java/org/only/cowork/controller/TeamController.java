package org.only.cowork.controller;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.only.cowork.entity.Team;
import org.only.cowork.service.TeamService;
import org.only.cowork.utils.ResponseTemplate;
import org.only.cowork.utils.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The Controller of the team service, which provides a series of user related interfaces.
 *
 * @author WangYanpeng
 */
@RestController
@Slf4j
@CrossOrigin
public class TeamController {

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ResponseTemplate<String> stringResponse;

    @Autowired
    private ResponseTemplate<List<Team>> listResponse;

    /**
     * @param params The Json format data.
     * @return a response template instantiated by the String.
     */
    @PostMapping("/team")
    public ResponseTemplate<String> createTeam(@RequestBody JSONObject params) {
        String teamName = String.valueOf(params.getString("teamName"));
        String teamDescription = params.getString("teamDescription");
        String token = params.getString("token");
        Set<String> teamMembersPhoneNumber = new HashSet<>(params.getJSONArray("teamMembersPhoneNumber").toJavaList(String.class));

        Map<String, Object> userInfo = userFeign.verifyUserByToken(token);
        try {
            Assert.notNull(userInfo);
        } catch (IllegalArgumentException e) {
            log.warn("The token is expired.");
            stringResponse.setCode(1302);
            stringResponse.setData("The token is expired.");
            return stringResponse;
        }
        Long leaderId = Long.parseLong(userInfo.get("id").toString());

        try {
            teamService.save(teamName, teamDescription, leaderId, teamMembersPhoneNumber);
        } catch (IllegalArgumentException e) {
            log.warn("The leader is not exist.");
            stringResponse.setCode(2002);
            stringResponse.setData("The leader is not exist.");
            return stringResponse;
        }

        stringResponse.setCode(2001);
        stringResponse.setData("Create team successfully.");
        return stringResponse;
    }

    /**
     * @param id The id of the team to be deleted.
     * @return a response template instantiated by the String.
     */
    @DeleteMapping("/team/{id}")
    public ResponseTemplate<String> deleteTeam(@PathVariable String id,
                                               @RequestParam String token) {
        try {
            this.verifyPermission(id, token);
        } catch (Exception e) {
            return stringResponse;
        }

        teamService.deleteTeam(Long.parseLong(id));

        stringResponse.setCode(2101);
        stringResponse.setData("Delete team successfully.");
        return stringResponse;
    }

    /**
     * @param id              Team id.
     * @param teamName        New team name.
     * @param teamDescription New team description.
     * @return a response template instantiated by the String.
     */
    @PatchMapping("/team/{id}")
    public ResponseTemplate<String> updateTeamInfo(@PathVariable String id,
                                                   String teamName,
                                                   String teamDescription,
                                                   @RequestParam String token) {
        try {
            this.verifyPermission(id, token);
        } catch (Exception e) {
            return stringResponse;
        }

        teamService.updateTeamInfo(Long.parseLong(id), teamName, teamDescription);

        stringResponse.setCode(2201);
        stringResponse.setData("Update team information successfully.");
        return stringResponse;
    }

    /**
     * @param id                   Team id.
     * @param newMemberPhoneNumber The phone number of the member to be added.
     * @return a response template instantiated by the String.
     */
    @PostMapping("/members/{id}")
    public ResponseTemplate<String> addTeamMember(@PathVariable String id,
                                                  @RequestParam String newMemberPhoneNumber,
                                                  @RequestParam String token) {
        try {
            this.verifyPermission(id, token);
        } catch (Exception e) {
            return stringResponse;
        }

        try {
            teamService.addTeamMember(Long.parseLong(id), newMemberPhoneNumber);
        } catch (IllegalArgumentException e) {
            log.warn("Team not exist or new team member not exist.");
            stringResponse.setCode(2302);
            stringResponse.setData("Team not exist or new team member not exist.");
            return stringResponse;
        }

        stringResponse.setCode(2301);
        stringResponse.setData("Add team member successfully.");
        return stringResponse;
    }

    /**
     * @param id                Team id.
     * @param memberPhoneNumber The phone number of the member to be removed.
     * @return a response template instantiated by the String.
     */
    @DeleteMapping("/members/{id}")
    public ResponseTemplate<String> removeTeamMember(@PathVariable String id,
                                                     @RequestParam String memberPhoneNumber,
                                                     @RequestParam String token) {
        try {
            this.verifyPermission(id, token);
        } catch (Exception e) {
            return stringResponse;
        }

        try {
            teamService.removeTeamMember(Long.parseLong(id), memberPhoneNumber);
        } catch (IllegalArgumentException e) {
            log.warn("Team not exist or new team member not exist or this member is not in the team.");
            stringResponse.setCode(2402);
            stringResponse.setData("Team not exist or new team member not exist or this member is not in the team.");
            return stringResponse;
        }

        stringResponse.setCode(2401);
        stringResponse.setData("Remove team member successfully.");
        return stringResponse;
    }

    /**
     * @param token User's token.
     * @return a response template instantiated by the String or List.
     */
    @GetMapping("/team")
    public ResponseTemplate<String> findByTeamLeader(@RequestParam String token) {
        Map<String, Object> userInfo = userFeign.verifyUserByToken(token);
        try {
            Assert.notNull(userInfo);
        } catch (IllegalArgumentException e) {
            log.warn("The token is expired.");
            stringResponse.setCode(1302);
            stringResponse.setData("The token is expired.");
            return stringResponse;
        }
        List<Team> teams = teamService.findByTeamLeader(Long.parseLong(userInfo.get("id").toString()));

        listResponse.setCode(2501);
        listResponse.setData(teams);

        return stringResponse;
    }

    /**
     * @param token User's token.
     * @return the teams joined by the user.
     */
    @GetMapping("/team/joined")
    public ResponseTemplate<String> findByTeamMembersContains(@RequestParam String token) {
        Map<String, Object> userInfo = userFeign.verifyUserByToken(token);
        try {
            Assert.notNull(userInfo);
        } catch (IllegalArgumentException e) {
            log.warn("The token is expired.");
            stringResponse.setCode(1302);
            stringResponse.setData("The token is expired.");
            return stringResponse;
        }
        List<Team> teams = teamService.findByTeamMembersContains(Long.parseLong(userInfo.get("id").toString()));

        listResponse.setCode(2601);
        listResponse.setData(teams);

        return stringResponse;
    }

    /**
     * @param id    Team id.
     * @param token User's token.
     */
    public void verifyPermission(String id, String token) {
        Map<String, Object> userInfo = userFeign.verifyUserByToken(token);
        try {
            Assert.notNull(userInfo);
        } catch (IllegalArgumentException e) {
            log.warn("The token is expired.");
            stringResponse.setCode(1302);
            stringResponse.setData("The token is expired.");
            throw e;
        }

        Long userId = Long.parseLong(userInfo.get("id").toString());
        Team team;
        try {
            team = teamService.findById(Long.parseLong(id));
        } catch (NoSuchElementException e) {
            log.warn("Team is not exist.");
            stringResponse.setCode(2502);
            stringResponse.setData("Team is not exist.");
            throw e;
        }
        try {
            Assert.isTrue(team.getTeamLeader().getId().equals(userId));
        } catch (IllegalArgumentException e) {
            log.warn("You have no permission");
            stringResponse.setCode(1303);
            stringResponse.setData("You have no permission.");
            throw e;
        }
    }
}
