package com.example.shopweb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseObject implements Serializable {

    @Id
    @Column(
            name = "id",
            unique = true,
            nullable = false
    )
    private UUID id;

    @Column(
            name = "uuid_key"
    )
    private UUID uuidKey;

    @Column(
            name = "voided"
    )
    private Boolean voided;

    @Column(
            name = "create_date",
            nullable = false
    )
    private LocalDateTime createDate;

    @Column(
            name = "created_by",
            length = 100
    )
    private String createdBy;

    @Column(
            name = "modify_date",
            nullable = false
    )
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @Column(
            name = "modified_by",
            length = 100
    )
    @LastModifiedBy
    private String modifiedBy;

    public BaseObject() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }

        this.uuidKey = UUID.randomUUID();
    }

    public BaseObject(BaseObject object) {
        super();
        if (object != null) {
            this.uuidKey = UUID.randomUUID();
            this.id = object.getId();
            this.modifiedBy = object.modifiedBy;
            this.modifyDate = object.modifyDate;
            this.createdBy = object.createdBy;
            this.createDate = object.createDate;
        }

    }

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
        this.modifyDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.modifyDate = LocalDateTime.now();
    }
}