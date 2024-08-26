package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class SkillService {
     private final SkillRepository skillRepository;

     public SkillService(SkillRepository skillRepository) {
          this.skillRepository = skillRepository;
     }

     public Skill handleCreateSkill(Skill createSkill) throws IdInvalidException {
          Skill currentSkill = this.skillRepository.findByName(createSkill.getName());
          if (currentSkill != null) {
               throw new IdInvalidException("This skill is existed");
          }
          return this.skillRepository.save(createSkill);
     }

     public Skill findSkillById(Skill skill) throws IdInvalidException {
          if (skill.getId() == null) {
               throw new IdInvalidException("Id is being null");
          }
          Optional<Skill> optionalSkill = this.skillRepository.findById(skill.getId());
          if (optionalSkill.isPresent()) {
               return optionalSkill.get();
          }
          return null;
     }

     public Skill handleUpdateSkill(Skill updateSkill) throws IdInvalidException {
          Skill skill = this.findSkillById(updateSkill);
          if (updateSkill != null) {
               skill.setId(updateSkill.getId());
               skill.setName(updateSkill.getName());
               return this.skillRepository.save(skill);
          }
          return skill;
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

          res.setResult(pageSkill);
          return res;
     }
}
