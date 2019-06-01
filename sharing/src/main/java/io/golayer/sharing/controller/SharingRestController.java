package io.golayer.sharing.controller;

import io.golayer.sharing.dto.CreateSharingRequestDto;
import io.golayer.sharing.model.User;
import io.golayer.sharing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SharingRestController {

    private final UserService userService;

    @PostMapping("/shares")
    public ResponseEntity createSharing(CreateSharingRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody @Valid User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }
}
