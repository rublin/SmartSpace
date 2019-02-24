package org.rublin.service;

import org.rublin.to.HeatingResponseDto;

public interface HeatingService {
    String pump(boolean enable);

    HeatingResponseDto current();
}
