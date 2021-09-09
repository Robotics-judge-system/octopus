package ru.anarcom.octopus.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Simple Role object. In future it will be base for ABAC access model.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Role extends BaseEntity {
    private String name;
}
