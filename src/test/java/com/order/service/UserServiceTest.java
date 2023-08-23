package com.order.service;

import com.order.dto.user.UserDto;
import com.order.entity.User;
import com.order.repository.UserRepository;
import com.order.security.CustomUserDetailsService;
import com.order.security.jwt.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void me() {
        Long userId = 1l;
        User user = new User(userId, "tester", "tester@gmail.com", "", 0);
        UserDto tester = new UserDto("tester", "tester@gmail.com", 0);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

//        Optional<User> byId = userRepository.findById(userId);
//        given(userService.getUser(userId)).willReturn(tester);

        UserDto userDto = userService.getUser(userId);

        // 검증
        assertNotNull(userDto);
        // 여기에서 UserDto와 User 객체 사이의 매핑 검증을 수행할 수 있습니다.
        // 예: assertEquals(user.getUsername(), userDto.getUsername());

        verify(userRepository, times(1)).findById(userId);

    }

}
