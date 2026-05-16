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
        List<User> users = userRepository.findAll();
        List<GitHubRepo> repos = gitHubService.getUserRepos();

        model.addAttribute("users", users);
        model.addAttribute("repos", repos);
        model.addAttribute("user", new User()); // for add form

        return "dashboard"; // single HTML file
    }

    // Add user from dashboard form
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/dashboard"; // context path auto-prepends /webapp
    }

    // GitHub repo details (JSON for inline table rendering)
    @ResponseBody
    @GetMapping("/github/repo/details/{repoName}")
    public GitHubRepo getRepoDetails(@PathVariable String repoName) {
        return gitHubService.getRepoByName(repoName);
    }

    // Health check
    @ResponseBody
    @GetMapping("/ping")
    public String ping() {
        return "App is alive";
    }
}
