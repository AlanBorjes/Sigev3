package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Images;
import mx.edu.utez.sigev.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagesService {

    @Autowired
    ImagesRepository imagesRepository;

    public Images findImages(long id){return imagesRepository.findImagesById(id);}

    public boolean save(Images obj){
        boolean flag = false;
        Images tmp = imagesRepository.save(obj);
        if (tmp!=null){
            flag = true;
        }
        return flag;
    }

}
