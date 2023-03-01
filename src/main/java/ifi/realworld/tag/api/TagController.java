package ifi.realworld.tag.api;

import ifi.realworld.tag.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    @GetMapping("/tags")
    public ResponseEntity<List<String>> getTags() {
        List<String> response = tagRepository.findNames();
        return ResponseEntity.ok(response);
    }

}
