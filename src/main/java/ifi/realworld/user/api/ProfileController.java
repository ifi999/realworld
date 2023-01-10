package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.app.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileDto getProfile(@PathVariable String username) {
        return ProfileDto.of(profileService.getProfile(username));
    }

    @PostMapping("/{username}/follow")
    public ProfileDto followUser(@PathVariable String username) {
        return ProfileDto.of(profileService.followUser(username));
    }

}
