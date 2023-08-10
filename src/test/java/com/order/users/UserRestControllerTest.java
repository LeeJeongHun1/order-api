package com.order.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.dto.user.LoginRequest;
import com.order.dto.user.LoginResult;
import com.order.repository.UserRepository;
import com.order.security.CustomUserDetailsService;
import com.order.security.SecurityConfig;
import com.order.security.WithMockJwtAuthentication;
import com.order.security.jwt.AuthenticationEntryPointImpl;
import com.order.security.jwt.JwtAuthenticationFilter;
import com.order.security.jwt.JwtProvider;
import com.order.service.UserService;
import com.order.utils.ApiUtils;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.ProxyableObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
@WebMvcTest(UserRestController.class)
//@AutoConfigureMockMvc(addFilters = false)
@MockBean({
        JpaMetamodelMappingContext.class,
        AuthenticationEntryPointImpl.class,
//        CustomUserDetailsService.class,
        JwtAuthenticationFilter.class
})
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 성공 테스트 (아이디, 비밀번호가 올바른 경우)")
//    @WithMockUser(username = "tester@gmail.com", roles = "USER")
//    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "tester@gmail.com")
//    @WithMockJwtAuthentication
    void loginSuccessTest() throws Exception {
//        given(userService.login(Mockito.any(LoginRequest.class))).willReturn(expectedLoginResult);
//        when(userService.login(new LoginRequest())).thenReturn(new LoginResult("", null));
//        given(userService.login(Mockito.any(LoginRequest.class))).isEqualTo(LoginResult.class);
//        given(userService.login(Mockito.any(LoginRequest.class))).willReturn(new ApiUtils.ApiResult<LoginResult>());
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
//                        .content("{\"principal\":\"tester@gmail.com\",\"credentials\":\"test1!\"}")
                        .content(objectMapper.writeValueAsString(new LoginRequest("tester2@gmail.com", "test1!")))

        );
        result.andDo(print());
        result.andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.token").exists())
                .andExpect(jsonPath("$.response.token").isString())
                .andExpect(jsonPath("$.response.user.name", is("tester")));
//                .andExpect(jsonPath("$.response.user.email.address", is("tester@gmail.com")))
//                .andExpect(jsonPath("$.response.user.loginCount").exists())
//                .andExpect(jsonPath("$.response.user.loginCount").isNumber())
//                .andExpect(jsonPath("$.response.user.lastLoginAt").exists());
    }

    //    @Test
//    @DisplayName("로그인 실패 테스트 (아이디, 비밀번호가 올바르지 않은 경우)")
//    void loginFailureTest() throws Exception {
//        ResultActions result = mockMvc.perform(
//                post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content("{\"principal\":\"tester@gmail.com\",\"credentials\":\"4321\"}")
//        );
//        result.andDo(print())
//                .andExpect(status().is4xxClientError())
//                .andExpect(handler().handlerType(UserRestController.class))
//                .andExpect(handler().methodName("login"))
//                .andExpect(jsonPath("$.success", is(false)))
//                .andExpect(jsonPath("$.error").exists())
//                .andExpect(jsonPath("$.error.status", is(401)))
//        ;
//    }
//
//    @Test
//    @WithMockJwtAuthentication
//    @DisplayName("내 정보 조회 성공 테스트 (토큰이 올바른 경우)")
//    void meSuccessTest() throws Exception {
//        ResultActions result = mockMvc.perform(
//                get("/api/users/me")
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//        result.andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(handler().handlerType(UserRestController.class))
//                .andExpect(handler().methodName("me"))
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.response.name", is("tester")))
//                .andExpect(jsonPath("$.response.email.address", is("tester@gmail.com")))
//                .andExpect(jsonPath("$.response.loginCount").exists())
//                .andExpect(jsonPath("$.response.loginCount").isNumber())
//        ;
//    }
//
    @Test
    @DisplayName("내 정보 조회 실패 테스트 (토큰이 올바르지 않을 경우)")
//    @WithMockUser
    void meFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/me")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
//        .header(jwtTokenConfigure.getHeader(), "Bearer " + randomAlphanumeric(60))
                        .header("Authorization", "Bearer " + randomAlphanumeric(60))
        );
        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
                .andExpect(jsonPath("$.error.message", is("Unauthorized")))
        ;
    }

}