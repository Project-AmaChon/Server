package v1.amachon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.domain.mail.service.EmailService;
import v1.amachon.domain.member.repository.MemberRepository;
import v1.amachon.domain.member.repository.auth.LogoutAccessTokenRedisRepository;
import v1.amachon.domain.member.service.AuthService;
import v1.amachon.domain.member.service.MemberService;
import v1.amachon.domain.message.service.MessageService;
import v1.amachon.domain.project.service.ProjectService;
import v1.amachon.domain.tags.service.RegionTagService;
import v1.amachon.domain.tags.service.TechTagService;
import v1.amachon.global.config.jwt.JwtAuthenticationFilter;
import v1.amachon.global.config.jwt.JwtTokenUtil;
import v1.amachon.global.config.security.CustomUserDetailService;

@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
@Import(JwtTokenUtil.class)
public class WebMVCTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenUtil jwtTokenUtil;

    @MockBean
    protected MemberRepository memberRepository;

    @MockBean
    protected CustomUserDetailService customUserDetailService;

    @MockBean
    protected RegionTagService regionTagService;

    @MockBean
    protected TechTagService techTagService;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected MessageService messageService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected JwtAuthenticationFilter jwtAuthenticationFilter;
}
