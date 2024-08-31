package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

     private final AuthenticationManagerBuilder authenticationManagerBuilder;
     private final SecurityUtil securityUtil;
     private final UserService userService;

     @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
     private Long refreshTokenExpiration;

     @PostMapping("/auth/login")
     public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
          // Nạp input gồm username/password vào Security
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword());
          // xác thực người dùng => cần viết hàm loadUserByUsername
          Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

          // create a token
          // nạp thông tin (nếu xử lý thành công) vào SecurityContext
          SecurityContextHolder.getContext().setAuthentication(authentication);

          ResLoginDTO res = new ResLoginDTO();

          User currentUserDB = this.userService
                    .handleGetUserByUsername(loginDTO.getUsername());
          if (currentUserDB != null) {
               ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                         currentUserDB.getId(),
                         currentUserDB.getEmail(),
                         currentUserDB.getName(),
                         currentUserDB.getRole());
               res.setUser(userLogin);
          }

          String access_token = this.securityUtil.createAccessToken(authentication.getName(), res);

          res.setAccess_token(access_token);

          // create refresh token
          String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

          // update refreshToken for user
          this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

          // set cookies
          ResponseCookie resCookie = ResponseCookie
                    .from("refresh_token", refresh_token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();

          return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                    .body(res);

     }

     @GetMapping("/auth/account")
     @ApiMessage("fetch account")
     public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
          String email = SecurityUtil.getCurrentUserLogin().isPresent()
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";

          User currentUserDB = this.userService.handleGetUserByUsername(email);
          ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
          ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
          if (currentUserDB != null) {
               userLogin.setId(currentUserDB.getId());
               userLogin.setEmail(currentUserDB.getEmail());
               userLogin.setName(currentUserDB.getName());
               userLogin.setRole(currentUserDB.getRole());

               userGetAccount.setUser(userLogin);
          }

          return ResponseEntity.ok().body(userGetAccount);
     }

     @GetMapping("/auth/refresh")
     @ApiMessage("get user by refresh token")
     public ResponseEntity<ResLoginDTO> getRefreshToken(
               @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
               throws IdInvalidException {
          if (refresh_token.equals("abc")) {
               throw new IdInvalidException("you don't have refresh token in cookies");
          }
          // check valid
          Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
          String email = decodedToken.getSubject();

          // check user by token + email
          User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
          if (currentUser == null) {
               throw new IdInvalidException("Refresh Token is not valid");
          }
          // issue new token / set refresh token as cookies
          ResLoginDTO res = new ResLoginDTO();

          User currentUserDB = this.userService
                    .handleGetUserByUsername(email);
          if (currentUserDB != null) {
               ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(),
                         currentUserDB.getEmail(), currentUserDB.getName(), currentUserDB.getRole());
               res.setUser(userLogin);
          }

          String access_token = this.securityUtil.createAccessToken(email, res);

          res.setAccess_token(access_token);

          // create refresh token
          String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

          // update refreshToken for user
          this.userService.updateUserToken(new_refresh_token, email);

          // set cookies
          ResponseCookie resCookie = ResponseCookie
                    .from("refresh_token", new_refresh_token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(refreshTokenExpiration)
                    .build();

          return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                    .body(res);
     }

     @PostMapping("/auth/logout")
     @ApiMessage("Logout success")
     public ResponseEntity<Void> logout() throws IdInvalidException {
          String email = SecurityUtil.getCurrentUserLogin().isPresent()
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";

          if (email.equals("")) {
               throw new IdInvalidException("Access token is not valid");
          }

          // update refresh token = null
          this.userService.updateUserToken(null, email);

          // remove fresh token from cookie`
          ResponseCookie deleteSpringCookie = ResponseCookie
                    .from("refresh_token", null)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)
                    .build();

          return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                    .build();

     }

}
