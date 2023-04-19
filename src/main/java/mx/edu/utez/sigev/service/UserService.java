package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public List<Users> findAllByRole(long id){return userRepository.findAllByRole(id);}

    public Users findById(long id) {
        return userRepository.findById(id);
    }

    public Page<Users> listPagination(PageRequest page) {
        return userRepository.findAll((org.springframework.data.domain.Pageable) page);
    }

    public String findPasswordById(long id) {
        return userRepository.findPasswordById(id);
    }

    public  Boolean existByUsername(String username){return  userRepository.existsByUsername(username);}

    public  Boolean existByEmail(String email){return  userRepository.existsByEmail(email);}

    public boolean save(Users obj) {
        boolean flag = false;
        Users tmp = userRepository.save(obj);
        if (tmp!=null) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        Users tmp = userRepository.findById(id);
        if (tmp!=null) {
            userRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
}
