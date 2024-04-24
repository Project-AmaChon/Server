package v1.amachon.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;
import v1.amachon.common.config.jwt.JwtAuthenticationFilter;
import v1.amachon.common.config.security.CustomUserDetailService;
import v1.amachon.common.config.security.SecurityConfig;
import v1.amachon.common.config.security.repository.LogoutAccessTokenRedisRepository;
import v1.amachon.mail.service.EmailService;
import v1.amachon.member.repository.MemberRepository;
import v1.amachon.common.config.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import v1.amachon.member.service.AuthService;
import v1.amachon.member.service.MemberService;
import v1.amachon.message.service.MessageService;
import v1.amachon.project.service.ProjectRecruitService;
import v1.amachon.project.service.ProjectService;
import v1.amachon.project.service.SearchProjectService;
import v1.amachon.tags.service.RegionTagService;
import v1.amachon.tags.service.TechTagService;

import javax.servlet.http.HttpServletRequest;

@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
@Import({JwtTokenUtil.class, JwtAuthenticationFilter.class, CustomUserDetailService.class, SecurityConfig.class})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    protected LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

    @MockBean
    protected HttpServletRequest httpServletRequest;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected EmailService emailService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MessageService messageService;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected ProjectRecruitService projectRecruitService;

    @MockBean
    protected SearchProjectService searchProjectService;

    @MockBean
    protected TechTagService techTagService;

    @MockBean
    protected RegionTagService regionTagService;

}
