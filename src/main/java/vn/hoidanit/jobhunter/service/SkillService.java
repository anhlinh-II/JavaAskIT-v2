package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class SkillService {
     private final SkillRepository skillRepository;

     public SkillService(SkillRepository skillRepository) {
          this.skillRepository = skillRepository;
     }

     public Skill fetchSkillById(long id) {
          Optional<Skill> optionalSkill = this.skillRepository.findById(id);
          if (optionalSkill.isPresent()) {
               return optionalSkill.get();
          }
          return null;
     }

     public Skill handleCreateSkill(Skill createSkill) throws IdInvalidException {
          return this.skillRepository.save(createSkill);
     }

     public Skill updateSkill(Skill s) throws IdInvalidException {
          return this.skillRepository.save(s);
     }

     public ResultPaginationDTO fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
          Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
          ResultPaginationDTO res = new ResultPaginationDTO();
          ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

          meta.setPage(pageable.getPageNumber() + 1);
          meta.setPageSize(pageable.getPageSize());

          meta.setPages(pageSkill.getTotalPages());
          meta.setTotal(pageSkill.getTotalElements());

          res.setMeta(meta);

          res.setResult(pageSkill.getContent());
          return res;
     }

     public boolean isNameExist(String name) {
          return this.skillRepository.existsByName(name);
     }

     public void deleteSkill(long id) {
          Optional<Skill> optionalSkill = this.skillRepository.findById(id);
          Skill currentSkill = optionalSkill.get();
          currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

          // delete skill
          this.skillRepository.delete(currentSkill);
     }
}
