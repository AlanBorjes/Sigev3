package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Roles;
import mx.edu.utez.sigev.repository.IRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    @Autowired
    private IRolesRepository rolesRepository;

    public List<Roles> findAll() {
        return rolesRepository.findAll();
    }

    public Roles findById(long id) {
        return rolesRepository.findById(id);
    }

    public boolean save(Roles obj) {
        boolean flag = false;
        Roles tmp = rolesRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        Roles tmp = rolesRepository.findById(id);
        if (!tmp.equals(null)) {
            flag = true;
            rolesRepository.delete(tmp);
        }
        return flag;
    }

    public Roles findByAuthority(String authority) {
        return rolesRepository.findByAuthority(authority);
    }
    
}
