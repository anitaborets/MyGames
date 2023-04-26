package org.example.server.webservice;

import org.example.entity.Comment;
import org.example.exceptions.CommentException;
import org.example.repository.CommentRepository;
import org.example.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/comment")
@EnableJpaRepositories
public class CommentWebServiceREST {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/{game}")
    public List<Comment> getAllComments(@PathVariable String game) throws CommentException {
        return commentService.getComments(game);
    }

    @PostMapping
    public void addScore(@RequestBody Comment comment) throws CommentException {
        commentService.addComment(comment);
    }

}
