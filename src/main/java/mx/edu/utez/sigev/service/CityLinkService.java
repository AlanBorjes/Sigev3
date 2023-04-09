package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.CityLink;
import mx.edu.utez.sigev.repository.ICityLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityLinkService {

    @Autowired
    private ICityLinkRepository linkRepository;

    public List<CityLink> findAll() {
        return linkRepository.findAll();
    }

    public CityLink findByUserId(long id) {
        return linkRepository.findByUserId(id);
    }

    public boolean hasCityLink(long id) {
        if (linkRepository.totalCityLinkCountByCityId(id) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public CityLink findOne(long id) {
        return linkRepository.getById(id);
    }

    public boolean save(CityLink obj) {
        boolean flag = false;
        CityLink tmp = linkRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        CityLink tmp = linkRepository.getById(id);
        if (!tmp.equals(null)) {
            linkRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }
    
}
