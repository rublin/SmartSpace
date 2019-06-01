package org.rublin.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rublin.events.OnNewEvent;
import org.rublin.model.Trigger;
import org.rublin.model.Type;
import org.rublin.model.event.AnalogEvent;
import org.rublin.model.event.DigitEvent;
import org.rublin.model.event.Event;
import org.rublin.service.EventService;
import org.rublin.service.TriggerService;
import org.rublin.util.exception.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@RestController
@RequestMapping(EventRestController.REST_URL)
@RequiredArgsConstructor
public class EventRestController {
    static final String REST_URL = "/events";

    private final TriggerService triggerService;
    private final ApplicationEventPublisher eventPublisher;
    private final EventService eventService;

    @GetMapping("/add")
    public ResponseEntity<String> saveNewEvent(@RequestParam int triggerId, @RequestParam String state) {
        log.info("Received new event {} from trigger {}", state, triggerId);
        Trigger trigger = triggerService.get(triggerId);
        Event event;
        if (trigger.getType() == Type.ANALOG && state.matches("\\d+\\.?\\d*")) {
            event = new AnalogEvent(trigger, Double.valueOf(state));
        } else if (trigger.getType() == Type.DIGITAL && state.toLowerCase().matches("true|false")) {
            event = new DigitEvent(trigger, Boolean.valueOf(state));
        } else {
            return ResponseEntity.badRequest().body(format("Wrong state %s. \n" +
                    "Accepted only true or false for the digital sensors and number for the analog ones", state));
        }
        eventPublisher.publishEvent(new OnNewEvent(event));
        return ResponseEntity.status(HttpStatus.CREATED).body("Event will be saved");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> get (@PathVariable("id") int triggerId) {
        return eventService.get(triggerService.get(triggerId));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAll() {
        return eventService.getAll();
    }

    @RequestMapping(value = "/between", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getBetween(
            @RequestParam(value = "from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return eventService.getBetween(from, to);
    }

    @RequestMapping(value = "/alarmed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Event> getAlarmed() {
        return eventService.getAlarmed();
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(NotFoundException e) {
        return e.getMessage();
    }
}
