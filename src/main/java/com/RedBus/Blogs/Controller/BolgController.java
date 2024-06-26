package com.RedBus.Blogs.Controller;

import com.RedBus.Blogs.Entity.Blog;
import com.RedBus.Blogs.Service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Blog/")
@Tag(name="blog Apis",description = "class contains apis for creating , updating , deleting , finding blog details")
public class BolgController {

    @Autowired
    private BlogService service;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "api for creating blog")
    @PostMapping("/create")
    public ResponseEntity<Blog>createBlog(@RequestBody Blog blog)
    {
        Blog blog1 = service.create(blog);
        return new ResponseEntity<>(blog1, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "api for deleting blog")
    @DeleteMapping("/delete")
    public ResponseEntity<?>deleteBlog(@PathVariable String id)
    {
        service.delete(id);
        return new ResponseEntity<>("blog is deleted successfully !",HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "api for updating blog")
    @PutMapping("/update")
    public ResponseEntity<Blog>updateBlog(@RequestBody Blog blog , @PathVariable String id)
    {
        Blog blog1 = service.update(blog , id);
        return  new ResponseEntity<>(blog1,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "api for finding all details of blog")
    @GetMapping("/all")
    public ResponseEntity<List<Blog>>getAllBlogs()
    {
        List<Blog>list = service.getAll();
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "api for finding details of blog using cities")
    @GetMapping("/byCities")
    public ResponseEntity<List<Blog>>ByCities(@RequestParam String fromCity , @RequestParam String toCity)
    {
        List<Blog>list = service.getUsingCities(fromCity,toCity);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "api for finding details of blog using Author name")
    @GetMapping("/byAuthor")
    public ResponseEntity<List<Blog>>ByCities(@PathVariable String author)
    {
        List<Blog>list = service.getUsingAuthor(author);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
}
