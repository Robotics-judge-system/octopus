package ru.anarcom.octopus.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.anarcom.octopus.entity.basic.BaseEntity;

/*
CREATE TABLE users
(
    id             bigserial primary key,
    name           varchar(50)                         not null,
    email          varchar(20)                         not null,
    created_at     timestamp default CURRENT_TIMESTAMP not null,
    updated_at     timestamp default CURRENT_TIMESTAMP not null,
    status         varchar(10)                         not null,
    password_hash  varchar(255)                        not null,
    last_online_at timestamp default CURRENT_TIMESTAMP not null
);
*/

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Email
  private String email;

  private String passwordHash;

  private Date lastOnlineAt;
}
