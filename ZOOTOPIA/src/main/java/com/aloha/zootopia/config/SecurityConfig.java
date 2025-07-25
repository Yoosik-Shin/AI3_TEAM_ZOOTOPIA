package com.aloha.zootopia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.aloha.zootopia.security.CustomAccessDeniedHandler;
import com.aloha.zootopia.security.CustomLogoutSuccessHandler;
import com.aloha.zootopia.security.LoginFailureHandler;
import com.aloha.zootopia.security.LoginSuccessHandler;
import com.aloha.zootopia.service.UserDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity  // 해당 클래스를 스프링 시큐리티 설정 빈으로 등록
                    // @Secured / @PreAuthorized, @PostAuthorized 으로 메서드 권한 제어 활성화
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private javax.sql.DataSource dataSource;

    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;


    // 🔐 스프링 시큐리티 설정 메소드
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {

        http.csrf(csrf -> csrf
                                .ignoringRequestMatchers("/hospitals/new", "/hospitals/edit")
        );

        // ✅ 인가 설정
        
        http.authorizeHttpRequests(auth -> auth
                                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user", "/user/**").hasAnyRole("USER","ADMIN")
                                .requestMatchers("/products/create", "/products/create/**").hasRole("ADMIN") // 상품 등록은 관리자만

                                .requestMatchers("/images/**", "/img/**", "/upload/**").permitAll()
                                .requestMatchers("/hospitals", "/hospitals/detail/**").permitAll()

                                .requestMatchers("/comments/add").authenticated() 
                                // .requestMatchers("/products/detail/**").authenticated() // 상품 상세 - 임시 비활성화
                                .requestMatchers("/cart/**").authenticated() // 장바구니 - 로그인 필요
                                .requestMatchers("/posts/upload/image").permitAll()
                                .requestMatchers("/lost/upload/image").permitAll()
                                .requestMatchers("/images/**", "/**").permitAll()

                                .requestMatchers("/mypage/**").authenticated()

                                .requestMatchers(HttpMethod.GET, "/hospitals/{hospitalId}/reviews").permitAll() // 추가

                                .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**").permitAll() // 🔐 OAuth2 로그인 설정 (네이버)
                                .anyRequest().permitAll()
                                );
        // 🔐 OAuth2 로그인 설정 (네이버)
        http.oauth2Login(oauth2 -> oauth2
                            .loginPage("/login") // 사용자 정의 로그인 페이지
                            .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                            .successHandler(loginSuccessHandler) // ✅ 소셜 로그인 성공 핸들러 추가
            );
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/posts/upload/image") // ✅ CSRF 무시 설정
            .ignoringRequestMatchers("/lost/upload/image") // ✅ CSRF 무시 설정
            );


        // 🔐 폼 로그인
        // http.formLogin(login -> login.permitAll());

        // ✅ 커스텀 로그인 페이지
        http.formLogin(login -> login
                                     .usernameParameter("email")       // 아이디 파라미터
                                     //.passwordParameter("pw")       // 비밀번호 파라미터
                                     .loginPage("/login")                   // 로그인 페이지 경로
                                     .loginProcessingUrl("/login") // 로그인 요청 경로
                                     // .defaultSuccessUrl("/?=true") // 로그인 성공 경로
                                     .successHandler(loginSuccessHandler)      // 로그인 성공 핸들러 설정
                                     .failureHandler(loginFailureHandler)      // 로그인 실패 핸들러 설정

                        );

        http.exceptionHandling( exception -> exception
                                            // 예외 처리 페이지 설정
                                            // .accessDeniedPage("/exception")
                                            // 접근 거부 핸들러 설정
                                            .accessDeniedHandler(customAccessDeniedHandler)

                                );

        // 👩‍💼 사용자 정의 인증
        http.userDetailsService(userDetailServiceImpl);

        // 🔄 자동 로그인 - 임시 비활성화
        http.rememberMe(me -> me
                .key("aloha")
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 7));

        // 🔓 로그아웃 설정
        http.logout(logout -> logout
                            .logoutUrl("/logout")   // 로그아웃 요청 경로
                            .invalidateHttpSession(true)        // 세션 초기화
                            .deleteCookies("remember-id")       // 로그아웃 시, 아이디저장 쿠키 삭제
                            .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 시 URL
                            .logoutSuccessHandler(customLogoutSuccessHandler) // ✅ 커스텀 핸들러 등록
                    );

        return http.build();
    }

    // PersistentRepository 토큰정보 객체 - 빈 등록
    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JdbcTokenRepositoryImpl : 토큰 저장 데이터 베이스를 등록하는 객체
        JdbcTokenRepositoryImpl repositoryImpl = new JdbcTokenRepositoryImpl();
        // 토큰 저장소를 사용하는 데이터 소스 지정
        repositoryImpl.setDataSource(dataSource);
        // persistent_logins 테이블 자동 생성
        // repositoryImpl.setCreateTableOnStartup(true);
        try {
            org.springframework.jdbc.core.JdbcTemplate jdbcTemplate = repositoryImpl.getJdbcTemplate();
            if (jdbcTemplate != null) {
                jdbcTemplate.execute(JdbcTokenRepositoryImpl.CREATE_TABLE_SQL);
            }
        } catch (Exception e) {
            log.error("persistent_logins 테이블이 이미 생성되었습니다.");
        }
        return repositoryImpl;
    }


    // 👮‍♂️🔐사용자 인증 관리 메소드
    // 인메모리 방식으로 인증
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     // user 123456
    //     UserDetails user = User.builder()
    //                             .username("user")
    //                             .password(passwordEncoder.encode("123456"))
    //                             .roles("USER")
    //                             .build();
    //     // admin 123456
    //     UserDetails admin = User.builder()
    //                             .username("admin")
    //                             .password(passwordEncoder.encode("123456"))
    //                             .roles("USER", "ADMIN")
    //                             .build();

    //     return new InMemoryUserDetailsManager( user, admin );
    //     // return new JdbcUserDetailsManager( ... );
    // }

    /**
     * 🍃 JDBC 인증 방식 빈 등록
     * @return
     */
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     JdbcUserDetailsManager userDetailsManager
    //             = new JdbcUserDetailsManager(dataSource);

    //     // 사용자 인증 쿼리
    //     String sql1 = " SELECT username, password, enabled "
    //                 + " FROM user "
    //                 + " WHERE username = ? "
    //                 ;
    //     // 사용자 권한 쿼리
    //     String sql2 = " SELECT username, auth "
    //                 + " FROM user_auth "
    //                 + " WHERE username = ? "
    //                 ;
    //     userDetailsManager.setUsersByUsernameQuery(sql1);
    //     userDetailsManager.setAuthoritiesByUsernameQuery(sql2);
    //     return userDetailsManager;
    // }


    /**
     * 🍃 AuthenticationManager 인증 관리자 빈 등록
     * @param authenticationConfiguration
     * @return
     * @throws Exception
    */
    @Bean
    public AuthenticationManager authenticationManager(
                                    AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
