package org.rublin.service;

import org.rublin.model.Relay;
import org.rublin.model.RelayState;

import java.util.List;

public interface RelayService {
    Relay find(Integer id);

    Relay find(String name);

    Relay create(Relay relay);

    RelayState getRelayStatus(Integer id);

    RelayState changeRelayStatus(String name, int productivity);

    List<Relay> getAll();

    String toTelegram(Relay relay);
}
