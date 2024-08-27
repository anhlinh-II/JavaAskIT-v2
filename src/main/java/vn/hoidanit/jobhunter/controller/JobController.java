package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.service.JobService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

     private final JobService jobService;

     public JobController(JobService jobService) {
          this.jobService = jobService;
     }

     public ResponseEntity<Job> createJob(@RequestBody @Valid Job postmanJob) {
          Job resJob = this.jobService.handleCreateJob(postmanJob);
          return ResponseEntity.ok(resJob);
     }
     
}
