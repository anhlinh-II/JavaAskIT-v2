package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/skill")
public class SkillController {

     private final SkillService skillService;

     public SkillController(SkillService skillService) {
          this.skillService = skillService;
     }

     @PostMapping
     @ApiMessage("create a skill successfully!")
     public ResponseEntity<Skill> createSkill(@RequestBody @Valid Skill reqSkill) throws IdInvalidException {
          Skill skill = this.skillService.handleCreateSkill(reqSkill);
          return ResponseEntity.ok(skill);
     }

     @PutMapping
     @ApiMessage("update a skill successfully!")
     public ResponseEntity<Skill> updateSkill(@RequestBody @Valid Skill reqSkill) throws IdInvalidException {
          Skill skill = this.skillService.handleUpdateSkill(reqSkill);
          return ResponseEntity.ok(skill);
     }

     @GetMapping
     @ApiMessage("get a skill successfully!")
     public ResponseEntity<ResultPaginationDTO> getAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.skillService.fetchAllSkill(spec, pageable));
    }
}
