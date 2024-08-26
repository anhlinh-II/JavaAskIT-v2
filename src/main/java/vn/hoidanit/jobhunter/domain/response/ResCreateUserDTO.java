package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {

     private long id;

     private String name;
     private String email;
     
     private int age;
     private GenderEnum gender;
     private String address;
     private Instant createdAt;

     private CompanyRes company;

     @Getter
     @Setter
     @NoArgsConstructor
     @AllArgsConstructor
     public static class CompanyRes {
          private long id;
          private String name;
     }
}
