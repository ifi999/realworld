package ifi.realworld.api.tag.api;

import ifi.realworld.api.TestSupport;
import ifi.realworld.tag.domain.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TagControllerTest extends TestSupport {

    @BeforeEach
    void setUp() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        tagRepository.saveAll(List.of(tag1, tag2, tag3));
    }

    @AfterEach
    void tearDown() {
        tagRepository.deleteAllInBatch();
    }

    @DisplayName("태그 목록을 조회한다.")
    @Test
    public void getTags() throws Exception {
        // then
        mockMvc.perform(
                get("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").exists())
        .andExpect(jsonPath("$[0]").value("tag1"))
        .andExpect(jsonPath("$[1]").exists())
        .andExpect(jsonPath("$[1]").value("tag2"))
        .andExpect(jsonPath("$[2]").exists())
        .andExpect(jsonPath("$[2]").value("tag3"));
    }

}