package org.only.cowork.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Team entity class.
 *
 * @author WangYanpeng
 */
@Component
@Entity
@Table(name = "T_TEAM")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Team extends BaseEntity implements Serializable {

    private String teamName;

    private String teamDescription;

    @ManyToOne(targetEntity = User.class)
    private User teamLeader;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
    private Set<User> teamMembers;

    private boolean isDeleted;
}
