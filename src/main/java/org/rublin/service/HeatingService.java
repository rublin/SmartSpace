package org.rublin.service;

public interface HeatingService {
    void pump(boolean enable);

    boolean current();
}
