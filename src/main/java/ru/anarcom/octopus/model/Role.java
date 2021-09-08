package ru.anarcom.octopus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple domain object that represents application user's role - ADMIN, USER, etc.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends BaseEntity {

    private String name;
//    private List<User> users;

}
