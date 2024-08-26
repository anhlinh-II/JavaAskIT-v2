package vn.hoidanit.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO.CompanyRes;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUpdateUserDTO {
     private long id;
     private String name;
     private GenderEnum gender;
     private String address;
     private int age;
     private Instant updatedAt;

     private CompanyRes company;
}
