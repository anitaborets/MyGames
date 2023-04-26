package org.example.server.webcontroller;

import org.example.entity.Comment;
import org.example.exceptions.CommentException;
import org.example.repository.CommentRepository;
import org.example.server.EntityDTO.CommentDTO;
import org.example.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.example.common.Constants.MINESWEEPER;
import static org.example.console.GameStudioConsole.userName;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class CommentWebController {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/comment")
    public String greeting(Model model) throws CommentException {
        model.addAttribute("comment", new Comment());
        List<Comment> list = commentService.getComments(MINESWEEPER);
        model.addAttribute("list", list);
        return "comment.html";
    }

    @PostMapping(value = "/comment/add")
    public String addComment(@ModelAttribute("comment") CommentDTO comment) throws CommentException {
        if(comment!=null) {
            Comment commentForSave = new Comment();
            commentForSave.setGame(comment.getGame());
            commentForSave.setComment(comment.getComment());
            commentForSave.setPlayer(userName);

            commentService.addComment(commentForSave);
        }
        return "redirect:/comment";
    }

}
