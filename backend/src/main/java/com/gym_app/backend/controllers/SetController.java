package com.gym_app.backend.controllers;

import com.gym_app.backend.models.Set;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gym_app.backend.repositories.SetRepository;

@RestController
@RequestMapping("/api/sets")
public class SetController {

    private final SetRepository setController;

    public SetController(SetRepository setController) {
        this.setController = setController;
    }

    @GetMapping
    public List<Set> getAllSets() {
        return setController.findAll();
    }
}