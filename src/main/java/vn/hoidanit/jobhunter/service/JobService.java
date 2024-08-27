package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.repository.JobRepository;

@Service
public class JobService {
     private final JobRepository jobRepository;

     public JobService(JobRepository jobRepository) {
          this.jobRepository = jobRepository;
     }

     public Job handleCreateJob(Job reqJob) {
          Job job = this.jobRepository.findByName(reqJob.getName());
          if (job == null) {
               return this.jobRepository.save(reqJob);
          }
          return null;
     }
}
