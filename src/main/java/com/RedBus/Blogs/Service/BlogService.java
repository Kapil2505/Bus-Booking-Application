package com.RedBus.Blogs.Service;

import com.RedBus.Blogs.Entity.Blog;

import java.util.List;

public interface BlogService {
    Blog create(Blog blog);

    void delete(String id);

    Blog update(Blog blog, String id);

    List<Blog> getAll();

    List<Blog> getUsingCities(String fromCity, String toCity);

    List<Blog> getUsingAuthor(String author);
}
