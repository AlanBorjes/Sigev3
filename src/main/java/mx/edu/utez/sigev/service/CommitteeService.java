package mx.edu.utez.sigev.service;


import mx.edu.utez.sigev.entity.Committee;
import mx.edu.utez.sigev.repository.ICommitteeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteeService {

    @Autowired
    private ICommitteeRepository committeeRepository;

    public List<Committee> findAll() {
        return committeeRepository.findAll();
    }

    public Committee findById(long id) {
        return committeeRepository.findById(id);
    }

    public Page<Committee> listarPaginacion(PageRequest page) {
        return committeeRepository.findAll((org.springframework.data.domain.Pageable) page);
    }

    public boolean save(Committee obj) {
        boolean flag = false;
        Committee tmp = committeeRepository.save(obj);
        if (tmp!=null) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        Committee tmp = committeeRepository.findById(id);
        if (tmp!=null) {
            committeeRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }
    
}