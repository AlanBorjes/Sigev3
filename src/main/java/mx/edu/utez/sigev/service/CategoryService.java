package mx.edu.utez.sigev.service;

import mx.edu.utez.sigev.entity.Category;
import mx.edu.utez.sigev.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(long id) {
        return categoryRepository.getById(id);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    public boolean exists(String name) {
        return categoryRepository.findByName(name) == null ? false : true;
    }

    public Page<Category> listPagination(PageRequest page) {
        return categoryRepository.findAll((org.springframework.data.domain.Pageable) page);
    }

    public boolean save(Category obj) {
        boolean flag = false;
        Category tmp = categoryRepository.save(obj);
        if (!tmp.equals(null)) {
            flag = true;
        }
        return flag;
    }

    public boolean delete(long id) {
        boolean flag = false;
        Category tmp = categoryRepository.getById(id);
        if (!tmp.equals(null)) {
            categoryRepository.delete(tmp);
            flag = true;
        }
        return flag;
    }
    
}
