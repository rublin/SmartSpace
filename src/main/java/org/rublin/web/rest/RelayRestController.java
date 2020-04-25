package org.rublin.web.rest;

import lombok.RequiredArgsConstructor;
import org.rublin.model.RelayState;
import org.rublin.service.RelayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RelayRestController {

    private final RelayService relayService;

    @GetMapping("/relay/{id}")
    public RelayState getPumpState(@PathVariable("id") Integer id) {
        return relayService.getRelayStatus(id);
    }
}
