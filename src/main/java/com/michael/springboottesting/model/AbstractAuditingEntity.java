package com.michael.springboottesting.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // Your class implementation here

    @CreatedBy
    @Column(name = "created_by",  length = 50, updatable = false)
    @JsonProperty
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    @JsonProperty
    private Instant createdDate = Instant.now();


    @LastModifiedBy
    @Column(name = "last_modify_by", length = 50, updatable = false)
    @JsonProperty
    private String lastModifyBy;

    @LastModifiedDate
    @Column(name = "last_modify_date")
    @JsonProperty
    private Instant modifiedDate = Instant.now();




}
