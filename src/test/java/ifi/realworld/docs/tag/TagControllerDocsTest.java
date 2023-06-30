package ifi.realworld.docs.tag;

import ifi.realworld.docs.RestDocsSupport;
import ifi.realworld.tag.api.TagController;
import ifi.realworld.tag.domain.Tag;
import ifi.realworld.tag.domain.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerDocsTest extends RestDocsSupport {

    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);

    @Override
    protected Object initController() {
        return new TagController(tagRepository);
    }

    private final Tag tag1 = new Tag("tag1");
    private final Tag tag2 = new Tag("tag2");
    private final Tag tag3 = new Tag("tag3");

    @DisplayName("태그 목록을 조회한다.")
    @Test
    public void getTags() throws Exception {
        // then
        given(tagRepository.findNames())
                .willReturn(List.of(tag1.getName(), tag2.getName(), tag3.getName()));

        mockMvc.perform(
                get("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andDo(print())
        .andDo(document(
                "get-tags"
                , preprocessRequest(prettyPrint())
                , preprocessResponse(prettyPrint())
                , responseFields(
                        fieldWithPath("[]").type(ARRAY)
                                .optional()
                                .description("태그 목록")
                )
        ));
    }

}