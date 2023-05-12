package org.only.cowork.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User entity class.
 *
 * @author WangYanpeng
 */
@Component
@Entity
@Table(name = "T_USER")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity implements Serializable {

    private String username;

    private String phoneNumber;

    private String password;

    private Boolean isAccountNonLocked;
}
