package service;

import model.AbstractTrigger;
import model.DigitTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.StateRepository;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by Sheremet on 16.06.2016.
 */
@Service
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository repository;
    @Override
    public void save(DigitTrigger trigger, boolean state) {
        repository.save(trigger, state);
    }

    @Override
    public Map<LocalDateTime, Boolean> getAll(DigitTrigger trigger) {
        return repository.getAll(trigger);
    }
}
