package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.Commentary;
import mx.edu.utez.sigev.repository.IColorRepository;
import mx.edu.utez.sigev.repository.ICommentaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

    @Autowired
    private IColorRepository colorRepository;

    public Color findById(long id) {
        return colorRepository.findById(1);
    }
}
