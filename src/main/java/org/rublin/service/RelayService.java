package org.rublin.service;

import org.rublin.model.Relay;
import org.rublin.model.RelayState;

public interface RelayService {
    Relay find(Integer id);

    Relay create(Relay relay);

    RelayState getRelayStatus(Integer id);

    RelayState changeRelayStatus(Integer id, int productivity);
}
