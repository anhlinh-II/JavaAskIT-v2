package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "companies")
@Getter
@Setter
public class Company {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @NotBlank(message = "Name is mandatory")
     private String name;

     @Column(columnDefinition = "MEDIUMTEXT")
     private String description;

     private String address;

     private String logo;

     // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
     private Instant createAt;

     private Instant updateAt;

     private String createBy;

     private String updateBy;

     @PrePersist
     public void handleBeforeCreatedAt() {
          this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
               ? SecurityUtil.getCurrentUserLogin().get()
               : "";
          this.createAt = Instant.now();
     }

     @PreUpdate
     public void handleBeforeUpdate() {
          this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
          ? SecurityUtil.getCurrentUserLogin().get()
          : "";
          this.createAt = Instant.now();
     }
}
