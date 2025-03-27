package com.RedBus.Blogs.Service.serviceImpl;

import com.RedBus.Blogs.Entity.Blog;
import com.RedBus.Blogs.Repository.BlogRepository;
import com.RedBus.Blogs.Service.BlogService;
import com.RedBus.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository repository;
    @Override
    public Blog create(Blog blog) {

        String id = UUID.randomUUID().toString();
        blog.setId(id);
        Blog save = repository.save(blog);
        return save;
    }

    @Override
    public void delete(String id) {
        repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("blog is not found "));
        repository.deleteById(id);
    }

    @Override
    public Blog update(Blog blog, String id) {
        Blog blog1 = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("blog is not found "));
        blog1.setTitle(blog.getTitle());
        blog1.setContent(blog.getContent());
        blog1.setToLocation(blog.getToLocation());
        blog1.setFromLocation(blog.getFromLocation());
        return repository.save(blog1);
    }

    @Override
    public List<Blog> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Blog> getUsingCities(String fromCity, String toCity) {
        return repository.findByFromLocationAndToLocation(fromCity,toCity);
    }

    @Override
    public List<Blog> getUsingAuthor(String author) {
        return repository.findByAuthor(author);
    }
}
