package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Roles;
import mx.edu.utez.sigev.entity.Suburb;
import mx.edu.utez.sigev.repository.ISuburbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuburbService {
    
    @Autowired
    private ISuburbRepository suburbRepository;

    public List<Suburb> findAll() {
        return suburbRepository.findAll();
    }

    public Page<Suburb> listPagination(PageRequest page) {
        return suburbRepository.findAll((org.springframework.data.domain.Pageable) page);
    }

    public Suburb findOne(long id) {
        return suburbRepository.getById(id);
    }


    public boolean save(Suburb obj) {
        boolean flag = false;
        Suburb tmp = suburbRepository.save(obj);
        if (tmp!=null) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        Suburb tmp = suburbRepository.getById(id);
        if (tmp!=null) {
            suburbRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }

}
