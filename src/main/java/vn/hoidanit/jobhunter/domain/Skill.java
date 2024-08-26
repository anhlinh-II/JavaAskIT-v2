package vn.hoidanit.jobhunter.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Table(name = "skills")
@Entity
@Getter
@Setter
public class Skill {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @NotBlank(message = "Name mustn't be blank")
     private String name;

     private Instant createdAt;
     private Instant updatedAt;
     private String createdBy;
     private String updatedBy;

     @ManyToMany(fetch = FetchType.LAZY, mappedBy = "skills")
     @JsonIgnore
     private List<Job> jobs;

     @PrePersist
     public void handleBeforeCreate() {
          this.createdAt = Instant.now();
          this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";
     }

     @PreUpdate
     public void handleBeforeUpdate() {
          this.updatedAt = Instant.now();
          this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";
     }
}
