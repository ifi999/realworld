package ifi.realworld.user.api;

import ifi.realworld.user.api.dto.ProfileDto;
import ifi.realworld.user.app.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Transactional
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{username}")
    public ProfileDto getProfile(@PathVariable String username, @AuthenticationPrincipal User user) {
        return profileService.getProfile(username, user);
    }

    @PostMapping("/{username}/follow")
    public ProfileDto followUser(@PathVariable String username, @AuthenticationPrincipal User user) {
        return profileService.followUser(username, user);
    }

    @DeleteMapping("/{username}/follow")
    public ProfileDto unFollowUser(@PathVariable String username, @AuthenticationPrincipal User user) {
        return profileService.unFollowUser(username, user);
    }

}
