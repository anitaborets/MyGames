package org.example.restClient;

import org.example.entity.Comment;
import org.example.exceptions.CommentException;
import org.example.server.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

public class CommentServiceRestClient implements CommentService {
    @Value("${remote.server.api}")
    private String URL;

    private RestTemplate restTemplate;

    @Override
    public int addComment(Comment comment) throws CommentException {
        restTemplate.postForEntity(URL, comment, Comment.class);
        return 1;
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return Arrays.asList(restTemplate.getForEntity(URL + "/" + game, Comment[].class).getBody());
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("This operation is not supported");
    }
}
