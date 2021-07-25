package ru.anarcom.octopus.entity.basic;

import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Data
@EqualsAndHashCode
public class BaseEntity {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  protected Long id;

  @Enumerated(EnumType.STRING)
  EntityStatus status;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
