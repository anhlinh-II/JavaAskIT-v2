package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
     private final CompanyRepository companyRepository;

     public CompanyService(CompanyRepository companyRepository) {
          this.companyRepository = companyRepository;
     }

     public Company handleCreateCompany(Company company) {
          return this.companyRepository.save(company);
     }

     public List<Company> handleFetchListCompany() {
          return this.companyRepository.findAll();
     }

     public Company fetchCompanyById(Long id) {
          Optional<Company> optionalCompany = this.companyRepository.findById(id);
          if (optionalCompany.isPresent()) {
               return optionalCompany.get();
          }
          return null;
     }

     public Company handleUpdateCompany(Company company) {
          Long id = company.getId();
          Company currentCompany = this.companyRepository.findById(id).get();

          if (currentCompany != null) {
               currentCompany.setAddress(company.getAddress());
               currentCompany.setDescription(company.getDescription());
               currentCompany.setName(company.getName());
               currentCompany.setId(company.getId());
               currentCompany.setLogo(company.getLogo());
          }
          return this.companyRepository.save(currentCompany);
     }

     public void handleDeleteCompany(Long id) {
          this.companyRepository.deleteById(id);
     }
}
