package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.experimental.var;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.service.CompanyService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
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

     @GetMapping("/companies/all")
     public ResponseEntity<List<Company>> getAllCompany() {
          List<Company> listCompany = companyService.handleFetchListCompany();
          return ResponseEntity.ok().body(listCompany);
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
