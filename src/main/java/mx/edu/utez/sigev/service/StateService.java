package mx.edu.utez.sigev.service;


import mx.edu.utez.sigev.entity.State;
import mx.edu.utez.sigev.repository.IStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private IStateRepository stateRepository;

    public List<State> findAll() {
        return stateRepository.findAll();
    }

    public State findById(long id) {
        return stateRepository.findById(id);
    }

    public boolean save(State obj) {
        boolean flag = false;
        State tmp = stateRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        State tmp = stateRepository.findById(id);
        if (!tmp.equals(null)) {
            stateRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }
    
}
