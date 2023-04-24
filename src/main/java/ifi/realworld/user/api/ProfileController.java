package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.app.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/profiles/{username}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String username) {
        ProfileDto response = profileService.getProfile(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileDto> followUser(@PathVariable String username) {
        ProfileDto response = profileService.followUser(username);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profiles/{username}/follow")
    public ResponseEntity<ProfileDto> unFollowUser(@PathVariable String username) {
        ProfileDto response = profileService.unFollowUser(username);
        return ResponseEntity.ok(response);
    }

}
