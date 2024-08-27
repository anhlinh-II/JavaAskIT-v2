package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

     private final SkillService skillService;

     public SkillController(SkillService skillService) {
          this.skillService = skillService;
     }

     @PostMapping
     @ApiMessage("create a skill successfully!")
     public ResponseEntity<Skill> create(@RequestBody @Valid Skill s) throws IdInvalidException {
          // check name
          if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
               throw new IdInvalidException("Skill name = " + s.getName() + " is existed");
          }
          Skill skill = this.skillService.handleCreateSkill(s);
          return ResponseEntity.ok(skill);
     }

     @PutMapping
     @ApiMessage("update a skill successfully!")
     public ResponseEntity<Skill> updateSkill(@RequestBody @Valid Skill s) throws IdInvalidException {
          // check name
          Skill currentSkill = this.skillService.fetchSkillById(s.getId());
          if (currentSkill == null) {
               throw new IdInvalidException("Skill id = " + s.getId() + " is not existed");
          }
          // check name
          if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
               throw new IdInvalidException("Skill name = " + s.getName() + " is existed");
          }
          currentSkill.setName(s.getName());
          return ResponseEntity.ok(this.skillService.updateSkill(currentSkill));
     }

     @GetMapping
     @ApiMessage("get a skill successfully!")
     public ResponseEntity<ResultPaginationDTO> getAllSkill(
               @Filter Specification<Skill> spec,
               Pageable pageable) {

          return ResponseEntity.status(HttpStatus.OK).body(
                    this.skillService.fetchAllSkill(spec, pageable));
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
          // check id
          if (this.skillService.fetchSkillById(id) == null) {
               throw new IdInvalidException("Skill id " + id + " is not existed");
          }
          this.skillService.deleteSkill(id);
          return ResponseEntity.ok().body(null);
     }
}
