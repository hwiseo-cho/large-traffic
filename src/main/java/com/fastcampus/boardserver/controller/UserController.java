package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.aop.LoginCheck;
import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.dto.request.UserDeleteId;
import com.fastcampus.boardserver.dto.request.UserLoginRequest;
import com.fastcampus.boardserver.dto.request.UserUpdatePasswordRequest;
import com.fastcampus.boardserver.dto.response.LoginResponse;
import com.fastcampus.boardserver.dto.response.UserInfoResponse;
import com.fastcampus.boardserver.service.Impl.UserServiceImpl;
import com.fastcampus.boardserver.service.UserService;
import com.fastcampus.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    private static LoginResponse loginResponse;

    /**
     * 사용자 등록
     * @param userDTO
     */
    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO) {
        if(UserDTO.hasNullDataBeforeRegister(userDTO)) {
            throw new RuntimeException("회원가입 정보를 확인해주세요.");
        }
        userService.register(userDTO);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        LoginResponse loginResponse;

        UserDTO userDTO = userService.login(userLoginRequest.getUserId(), userLoginRequest.getPassword());

        if(userDTO == null) {
            return HttpStatus.NOT_FOUND;
        } else if(userDTO != null) {
            loginResponse = LoginResponse.success(userDTO);
            if(userDTO.getStatus() == (UserDTO.Status.ADMIN)) {
                SessionUtil.setLoginAdminId(session, userLoginRequest.getUserId());
            } else {
                SessionUtil.setLoginMemberId(session, userLoginRequest.getUserId());
            }

            responseEntity = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("정보가 올바르지 않습니다.");
        }

        return HttpStatus.OK;
    }
    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);

        if(id == null) {
            id = SessionUtil.getLoginAdminId(session);
        }

        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PutMapping("logout")
    public void logout(HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    @LoginCheck(type = LoginCheck.UserType.ADMIN)
    public ResponseEntity<LoginResponse> updateUserPassword(@RequestParam(value = "accountId", required = false) String accountId, @RequestBody UserUpdatePasswordRequest passwordRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = accountId;
        String beforePassword = passwordRequest.getBeforePassword();
        String afterPassword = passwordRequest.getAfterPassword();

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("delete")
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);

        try {
            userService.deleteId(id, userDeleteId.getPassword());
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("delete 실패");
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }
}
