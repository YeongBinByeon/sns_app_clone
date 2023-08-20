package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest{

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    // 회원가입 성공
    @Test
    void 회원가입_성공(){
        String userName = "userName";
        String password = "password";

        // 동일 userName 회원 존재하지 않음
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

        // save() 어떤 파라메터로든 호출 시 사용자가 정의한 UserEntity return하도록 정의
        when(userEntityRepository.save(Mockito.any())).thenReturn(UserEntityFixture.get(userName, password, 1));

        Assertions.assertDoesNotThrow(()->userService.join(userName, password));
    }

    // 회원가입 실패 이미 존재하는 계정명
    @Test
    void 회원가입_실패_이미_존재하는_계정명(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // 동일 userName 회원 존재함
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // save() 어떤 파라메터로든 호출 시 사용자가 정의한 UserEntity return하도록 정의
        when(userEntityRepository.save(Mockito.any())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
    }

    // 로그인 성공
    @Test
    void 로그인_성공(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // 회원가입 되어 있을 때
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // 비밀번호가 일치할 때
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(()->userService.login(userName,password));
    }

    // 로그인 실패 존재하지 않는 회원
    @Test
    void 로그인_실패_존재하지_않는_회원(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // 회원가입 되어 있을 때
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        
//        // 비밀번호가 일치할 때, 아래 mock 주석해놓으면 라이브러리에서 정의 안 된 mock message는 false 리턴하는 것 확인하였음
//        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName,password));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, e.getErrorCode().getStatus());
    }

    // 로그인 실패 비밀번호가 틀린 경우
    @Test
    void 로그인_실패_비밀번호가_틀린_경우(){
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password, 1);

        // 회원가입 되어 있을 때
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

        // 비밀번호가 일치할 때
        when(encoder.matches(password, fixture.getPassword())).thenReturn(false);

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName,password));
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, e.getErrorCode().getStatus());
    }
}

//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserService userService;
//
//    @MockBean
//    private UserEntityRepository userEntityRepository;
//
//    @MockBean
//    private BCryptPasswordEncoder encoder;
//
//    @Test
//    void 회원가입이_정상적으로_동작하는_경우(){
//        String userName = "userName";
//        String password = "password";
//
//        //mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
//        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName, password));
//
//        Assertions.assertDoesNotThrow(()->userService.join(userName, password));
//    }
//
//    @Test
//    void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우(){
//        String userName = "userName";
//        String password = "password";
//
//        UserEntity fixture = UserEntityFixture.get(userName, password);
//
//        //mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));
//
//        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.join(userName, password));
//        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
//    }
//
//    @Test
//    void 로그인이_정상적으로_동작하는_경우(){
//        String userName = "userName";
//        String password = "password";
//
//        UserEntity fixture = UserEntityFixture.get(userName, password);
//
//        //mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);
//
//        Assertions.assertDoesNotThrow(()->userService.login(userName, password));
//    }
//
//    @Test
//    void 로그인시_userName으로_회원가입한_유저가_없는경우(){
//        String userName = "userName";
//        String password = "password";
//
//        UserEntity fixture = UserEntityFixture.get(userName, password);
//
//        //mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
//
//        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName, password));
//        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
//    }
//
//    @Test
//    void 로그인시_패스워드가_틀린경우(){
//        String userName = "userName";
//        String password = "password";
//        String wrongPassword = "wrongPassword";
//
//        UserEntity fixture = UserEntityFixture.get(userName, password);
//
//        //mocking
//        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));
//
//        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, ()->userService.login(userName, wrongPassword));
//        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
//
//    }
//
//}
