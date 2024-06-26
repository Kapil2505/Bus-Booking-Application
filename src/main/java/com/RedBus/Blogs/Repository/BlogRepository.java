package com.RedBus.Blogs.Repository;

import com.RedBus.Blogs.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,String> {

    List<Blog>findByFromAndTo(String fromCity,String toCity);
    List<Blog>findByAuthor(String author);

}
