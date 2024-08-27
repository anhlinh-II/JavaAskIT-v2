package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

     private final JobService jobService;

     public JobController(JobService jobService) {
          this.jobService = jobService;
     }

     @PostMapping
     @ApiMessage("create a job")
     public ResponseEntity<ResCreateJobDTO> create(@RequestBody @Valid Job postmanJob) {
          ResCreateJobDTO resJob = this.jobService.createJob(postmanJob);
          return ResponseEntity.status(HttpStatus.CREATED).body(resJob);
     }

     @PutMapping
     @ApiMessage("update a job")
     public ResponseEntity<ResUpdateJobDTO> update(@RequestBody @Valid Job postmanJob) throws IdInvalidException {
          Optional<Job> currentJob = this.jobService.finJobById(postmanJob.getId());
          if (!currentJob.isPresent()) {
               throw new IdInvalidException("job not found");
          }
          return ResponseEntity.ok(this.jobService.updateJob(postmanJob));
     }

     @DeleteMapping("/{id}")
     @ApiMessage("delete a job")
     public ResponseEntity<Void> delete(@RequestParam("id") Long id) throws IdInvalidException {
          Optional<Job> currentJob = this.jobService.finJobById(id);
          if (!currentJob.isPresent()) {
               throw new IdInvalidException("id" + id + " is not found");
          }
          this.jobService.deleteJob(id);
          return ResponseEntity.ok(null);
     }

     @GetMapping("/{id}")
     @ApiMessage("get job by id")
     public ResponseEntity<Job> getJob(@RequestParam("id") long id) throws IdInvalidException {
          Optional<Job> optionalJob = this.jobService.finJobById(id);
          if (!optionalJob.isPresent()) {
               throw new IdInvalidException("id is not found");
          }
          return ResponseEntity.ok(optionalJob.get());
     }

     @GetMapping()
     @ApiMessage("fetch all job")
     public ResponseEntity<ResultPaginationDTO> getAllJob(
               @Filter Specification<Job> spec,
               Pageable pageable) {

          return ResponseEntity.status(HttpStatus.OK).body(
                    this.jobService.fetchAllUser(spec, pageable));
     }

}
