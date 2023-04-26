package org.example.server.service;

import org.example.entity.Comment;
import org.example.exceptions.CommentException;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment) throws CommentException;
    List<Comment> getComments(String game) throws CommentException;
    void reset() throws CommentException;


}
