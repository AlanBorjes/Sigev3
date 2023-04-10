package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Color;
import mx.edu.utez.sigev.entity.ContentApp;
import mx.edu.utez.sigev.repository.IColorRepository;
import mx.edu.utez.sigev.repository.IContentApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentAppService {

    @Autowired
    private IContentApp contentApp;

    public ContentApp findById(long id) {
        return contentApp.findById(1);
    }
}
