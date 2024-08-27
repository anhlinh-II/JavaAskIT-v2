package vn.hoidanit.jobhunter.service;

import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
@RequiredArgsConstructor
public class JobService {
     private final JobRepository jobRepository;
     private final SkillRepository skillRepository;

     public ResCreateJobDTO createJob(Job j) {
          // check skill
          if (j.getSkills() != null) {
               List<Long> reqSkills = j.getSkills().stream()
                         .map(skill -> skill.getId())
                         .collect(Collectors.toList());

               List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
               j.setSkills(dbSkills);
          }

          // create job
          Job currentJob = this.jobRepository.save(j);

          ResCreateJobDTO dto = new ResCreateJobDTO();
          dto.setId(currentJob.getId());
          dto.setName(currentJob.getName());
          dto.setSalary(currentJob.getSalary());
          dto.setQuantity(currentJob.getQuantity());
          dto.setLocation(currentJob.getLocation());
          dto.setLevel(currentJob.getLevel());
          dto.setStartDate(currentJob.getStartDate());
          dto.setEndDate(currentJob.getEndDate());
          dto.setActive(currentJob.isActive());
          dto.setCreatedAt(currentJob.getCreatedAt());
          dto.setCreatedBy(currentJob.getCreatedBy());

          if (currentJob.getSkills() != null) {
               List<String> skills = currentJob.getSkills().stream()
                         .map(item -> item.getName()).collect(Collectors.toList());
               dto.setSkills(skills);
          }

          return dto;

     }

     public Optional<Job> finJobById(long id) {
          return this.jobRepository.findById(id);
     }

     public ResUpdateJobDTO updateJob(Job j) {
          // check skill
          if (j.getSkills() != null) {
               List<Long> reqSkills = j.getSkills().stream()
                         .map(skill -> skill.getId())
                         .collect(Collectors.toList());

               List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
               j.setSkills(dbSkills);
          }

          // update job
          Job currentJob = this.jobRepository.save(j);

          ResUpdateJobDTO dto = new ResUpdateJobDTO();
          dto.setId(currentJob.getId());
          dto.setName(currentJob.getName());
          dto.setSalary(currentJob.getSalary());
          dto.setQuantity(currentJob.getQuantity());
          dto.setLocation(currentJob.getLocation());
          dto.setLevel(currentJob.getLevel());
          dto.setStartDate(currentJob.getStartDate());
          dto.setEndDate(currentJob.getEndDate());
          dto.setActive(currentJob.isActive());
          dto.setUpdatedAt(currentJob.getUpdatedAt());
          dto.setUpdatedBy(currentJob.getUpdatedBy());

          if (currentJob.getSkills() != null) {
               List<String> skills = currentJob.getSkills().stream()
                         .map(item -> item.getName()).collect(Collectors.toList());
               dto.setSkills(skills);
          }

          return dto;
     }

     public void deleteJob(Long id) {
          this.jobRepository.deleteById(id);
     }

     public ResultPaginationDTO fetchAllUser(Specification<Job> spec, Pageable pageable) {
          Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageJob.getContent());

        return rs;
     }
}
