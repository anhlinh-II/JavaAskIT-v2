package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
     private final CompanyService companyService;

     CompanyController(CompanyService companyService) {
          this.companyService = companyService;
     }

     @PostMapping("/companies")
     public ResponseEntity<Company> createCompany(@RequestBody @Valid Company postManCompany) {
          Company reqCompany = this.companyService.handleCreateCompany(postManCompany);
          return ResponseEntity.status(HttpStatus.CREATED).body(reqCompany);
     }

     @GetMapping("/companies")
     @ApiMessage("fetch companies")
     public ResponseEntity<ResultPaginationDTO> getCompany(
          @Filter Specification<Company> spec,
          Pageable pageable
     ) {
          return ResponseEntity.ok(this.companyService.handleGetCompany(spec, pageable));
     }

     @PutMapping("/companies")
     public ResponseEntity<Company> updateCompany(@RequestBody @Valid Company postmanCompany) {
          Company updatedCompany = this.companyService.handleUpdateCompany(postmanCompany);
          return ResponseEntity.ok().body(updatedCompany);
     }

     @DeleteMapping("/companies/{id}")
     public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
          this.companyService.handleDeleteCompany(id);
          return ResponseEntity.noContent().build();
     }

}
