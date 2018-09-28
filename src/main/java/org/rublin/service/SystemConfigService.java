package org.rublin.service;

import org.rublin.model.ConfigKey;
import org.rublin.model.SystemConfig;

public interface SystemConfigService {
    String  get(ConfigKey key);

    void put(SystemConfig config);
}
