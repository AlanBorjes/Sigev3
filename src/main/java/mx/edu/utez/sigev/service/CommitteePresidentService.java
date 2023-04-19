package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.CommitteePresident;
import mx.edu.utez.sigev.repository.ICommitteePresidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitteePresidentService {
    
    @Autowired
    private ICommitteePresidentRepository presidentRepository;

    public List<CommitteePresident> findAll() {
        return presidentRepository.findAll();
    }

    public CommitteePresident findByUser(long id) {
        return presidentRepository.findByUserId(id);
    }

    public boolean hasPresident(long id) {
        return presidentRepository.totalCommitteePresidentCountByCommitteeId(id) > 0 ? true : false;
    }

    public CommitteePresident findById(long id) {
        return presidentRepository.getById(id);
    }

    public List<CommitteePresident> listPagination(long id) {
        return presidentRepository.listPagination(id);
    }

    public boolean save (CommitteePresident obj) {
        boolean flag = false;
        CommitteePresident tmp = presidentRepository.save(obj);
        if (tmp!=null) {
            flag = true;
        }
        return flag;
    }
    
}
