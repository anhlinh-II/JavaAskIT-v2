package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.jobhunter.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

     Skill findByName(String name);

     Page<Skill> findAll(Specification<Skill> spec, Pageable pageable);

     boolean existsByName(String name);

     List<Skill> findByIdIn(List<Long> reqSkills);
     
}
