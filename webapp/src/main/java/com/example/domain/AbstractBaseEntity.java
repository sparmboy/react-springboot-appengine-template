package com.example.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base entity for all entities to extend from so that they benefit
 * from automatic createdBy, modifiedBy, createdDate and modifiedDate being set
 * on any interaction with data store
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseEntity {

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Getter
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
