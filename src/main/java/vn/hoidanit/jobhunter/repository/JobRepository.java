package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.jobhunter.domain.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

     Job findByName(String name);
     
}
