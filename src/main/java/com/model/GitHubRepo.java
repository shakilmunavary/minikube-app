package com.model;

public class GitHubRepo {
    private String name;
    private String description;
    private String language;
    private int stargazers_count;
    private int forks_count;
    private String html_url;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public int getStargazers_count() { return stargazers_count; }
    public void setStargazers_count(int stargazers_count) { this.stargazers_count = stargazers_count; }

    public int getForks_count() { return forks_count; }
    public void setForks_count(int forks_count) { this.forks_count = forks_count; }

    public String getHtml_url() { return html_url; }
    public void setHtml_url(String html_url) { this.html_url = html_url; }
}
