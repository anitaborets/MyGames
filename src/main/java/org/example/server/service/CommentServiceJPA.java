package org.example.server.service;

import org.example.entity.Comment;
import org.example.exceptions.CommentException;
import org.example.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentServiceJPA implements CommentService {
    Logger LOGGER = Logger.getLogger(CommentServiceJPA.class.getName());
    @Autowired
    CommentRepository repository;
    @Override
    public int addComment(Comment comment) {
        repository.save(comment);
        LOGGER.log(Level.INFO, "comment was added");
        return 1;
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return repository.findAll();
    }

    @Override
    public void reset() throws CommentException {
        repository.deleteAll();
    }
}
