package org.example.server.webcontroller;


import org.example.minesweeper.core.Field;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/js")
public class MinesJSController {
    private Field field;
    @GetMapping("/new")
    public Field getTopScores(@RequestParam int rows, @RequestParam int cols, @RequestParam int mines) {
        this.field = new Field(rows, cols, mines);
        return this.field;
    }
    @GetMapping("/mark")
    public Field markTile(@RequestParam int row, @RequestParam int col) {
        this.field.markTile(row, col);
        return this.field;
    }

    @GetMapping("/open")
    public Field openTile(@RequestParam int row, @RequestParam int col) {
        this.field.openTile(row, col);
        return this.field;
    }


}
