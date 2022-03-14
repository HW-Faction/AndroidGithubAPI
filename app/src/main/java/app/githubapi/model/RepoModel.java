package app.githubapi.model;

public class RepoModel {
    private String totalStars;
    private String description;
    private String url;
    private String username;
    private String repositoryName;

    public RepoModel(String totalStars, String description, String url, String username, String repositoryName) {
        this.totalStars = totalStars;
        this.description = description;
        this.url = url;
        this.username = username;
        this.repositoryName = repositoryName;
    }

    public RepoModel() {
    }

    public String getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(String totalStars) {
        this.totalStars = totalStars;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
