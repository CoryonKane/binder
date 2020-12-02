package com.codecool.binder.model;

import lombok.*;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String password;
    private String email;
    private String profilePicture;
    // Different usernames for different platforms
    @ElementCollection
    private Map<String, String> profileNames;
    @ElementCollection
    private Set<String> interests;
    @OneToMany
    private Set<Project> favouriteProjects;
    @OneToMany
    private Set<Post> posts;
    @ManyToMany
    private Set<User> matchList;
    @ManyToMany
    private Set<User> followList;
    @ManyToMany
    private Set<User> nopeList;

    public void addProfileName (String platform, String profileName) {
        if (!this.profileNames.containsKey(platform)) {
            this.profileNames.put(platform, profileName);
        }
    }

    public void removeProfileName(String platform) {
        this.profileNames.remove(platform);
    }

    public void addProject (Project project) {
        this.favouriteProjects.add(project);
    }

    public void removeProject (Project project) {
        this.favouriteProjects.remove(project);
    }

    public boolean addMatch(User user) {
        return matchList.add(user);
    }

    public boolean removeMatch(User user) {
        return matchList.remove(user);
    }

    public boolean addFollow(User user) {
        return followList.add(user);
    }

    public boolean removeFollow(User user) {
        return followList.remove(user);
    }

    public boolean addNope(User user) {
        return nopeList.add(user);
    }

    public boolean removeNope(User user) {
        return nopeList.remove(user);
    }

    public boolean addPost(Post post) {
        return posts.add(post);
    }

    public boolean removePost(Post post) {
        return posts.remove(post);
    }

    public boolean addInterest(String s) {
        return interests.add(s);
    }

    public boolean remove(String s) {
        return interests.remove(s);
    }
}
