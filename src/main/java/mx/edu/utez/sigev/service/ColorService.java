package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Users;
import mx.edu.utez.sigev.repository.IColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

    @Autowired
    IColorRepository colorRepository;

    public Color findColors(long id){
        return colorRepository.findColorById(id);
    }

    public boolean save(Color obj) {
        boolean flag = false;
        Color tmp = colorRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }
}
