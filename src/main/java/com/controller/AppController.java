package com.controller;

import com.model.User;
import com.model.GitHubRepo;
import com.repository.UserRepository;
import com.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GitHubService gitHubService;

    // Unified dashboard view
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("repos", gitHubService.getUserRepos());
        model.addAttribute("user", new User()); // for add form
        return "dashboard"; // single HTML file
    }

    // Add user from dashboard form
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/webapp/dashboard"; // redirect back to unified view
    }

    // GitHub repo details (JSON)
    @ResponseBody
    @GetMapping("/github/repo/{repoName}")
    public GitHubRepo getRepo(@PathVariable String repoName) {
        return gitHubService.getRepoByName(repoName);
    }

    // Health check
    @ResponseBody
    @GetMapping("/ping")
    public String ping() {
        return "App is alive";
    }
}
