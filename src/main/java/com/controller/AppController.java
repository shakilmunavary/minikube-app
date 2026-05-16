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

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> users = userRepository.findAll();
        List<GitHubRepo> repos = gitHubService.getUserRepos();

        model.addAttribute("users", users);
        model.addAttribute("repos", repos);
        model.addAttribute("user", new User());

        return "dashboard";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/dashboard"; // ✅ fixed redirect
    }

    @ResponseBody
    @GetMapping("/github/repo/details/{repoName}")
    public GitHubRepo getRepoDetails(@PathVariable String repoName) {
        return gitHubService.getRepoByName(repoName);
    }

    @ResponseBody
    @GetMapping("/ping")
    public String ping() {
        return "App is alive";
    }
}
