package ifi.realworld.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifi.realworld.user.domain.repository.UserRepository;
import ifi.realworld.utils.security.UserPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(RestDocumentationExtension.class)
public abstract class RestCodsSupport {

    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected final UserPasswordEncoder userPasswordEncoder = Mockito.mock(UserPasswordEncoder.class);

    protected final UserRepository userRepository = Mockito.mock(UserRepository.class);

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build();
    }

    protected abstract Object initController();

}
