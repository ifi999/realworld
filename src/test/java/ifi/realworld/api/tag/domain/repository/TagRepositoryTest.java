package ifi.realworld.api.tag.domain.repository;

import ifi.realworld.api.TestSupport;
import ifi.realworld.tag.domain.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TagRepositoryTest extends TestSupport {
    
    @DisplayName("태그명으로 조회한다.")
    @Test
    public void findByName() {
        // given
        Tag tag = new Tag("tag1");
        tagRepository.save(tag);

        // when
        Tag getTag = tagRepository.findByName(tag.getName()).get();

        // then
        assertThat(getTag).isEqualTo(tag);
        assertThat(getTag.getName()).isEqualTo(tag.getName());
    }

    @DisplayName("태그 목록을 조회한다.")
    @Test
    public void findNames() {
        // given
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Tag tag3 = new Tag("tag3");
        tagRepository.saveAll(List.of(tag1, tag2, tag3));

        // when
        List<String> tags = tagRepository.findNames();

        // then
        assertThat(tags).hasSize(3)
                .containsExactlyInAnyOrder(
                        "tag1", "tag2", "tag3"
                );
    }

}