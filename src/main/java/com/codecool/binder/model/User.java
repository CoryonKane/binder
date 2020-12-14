package com.codecool.binder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Binder")
public class User {
    @Id
    @GeneratedValue
    // kinda private
    private Long id;
    // public
    @Column(nullable = false)
    private String firstName;
    // public
    @Column(nullable = false)
    private String lastName;
    // public
    private String nickName;
    // nagyon private
    @Column(nullable = false)
    private String password;
    // private
    @Column(unique = true, nullable = false)
    private String email;
    // public
    private String profilePicture;
    // private
    @ElementCollection
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<String> roles = new ArrayList<>();
    // private
    // user állíthatja egyenként hogy public vagy private
    @ElementCollection
    @Singular
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Map<Profile, Boolean> profileNames = new HashMap<>();
    // public
    @ElementCollection
    @Singular
    private Set<String> interests = new HashSet<>();
    // user állíthatja egyenként hogy public vagy private
    @ElementCollection
    @Singular
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Map<Project, Boolean> projects = new HashMap<>();
    // public, followwal news feedbe kerül
    @OneToMany
    @Singular
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Post> posts = new HashSet<>();
    // csak saját magadnak settingsben
    @ManyToMany
    @Singular("match")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> matchList = new HashSet<>();
    // csak saját magadnak settingsben
    @ManyToMany
    @Singular("follow")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> followList = new HashSet<>();
    // csak saját magadnak settingsben
    @ManyToMany
    @Singular("nope")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> nopeList = new HashSet<>();
    // csak saját magadnak settingsben
    @ManyToMany
    @Singular("ban")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<User> banList = new HashSet<>();

    public void addProfileName (Profile profile, boolean isPublic) {
        Profile profInMap = hasProfile(profile);
        if (profInMap != null) {
            this.profileNames.remove(profInMap);
        }
        this.profileNames.put(profile, isPublic);
    }

    public void removeProfileName(Profile profile) {
        this.profileNames.remove(profile);
    }

    private Profile hasProfile(Profile profile) {
        return this.profileNames.keySet().stream().filter(p -> p.getWebPage().equals(profile.getWebPage())).findFirst().orElse(null);
    }

    public void addProject (Project project, boolean isPublic) {
        this.projects.put(project, isPublic);
    }

    public void removeProject (Project project) {
        this.projects.remove(project);
    }

    public void addMatch(User user) {
        matchList.add(user);
    }

    public void removeMatch(User user) {
        matchList.remove(user);
    }

    public void addFollow(User user) {
        followList.add(user);
    }

    public void removeFollow(User user) {
        followList.remove(user);
    }

    public void addNope(User user) {
        nopeList.add(user);
    }

    public void removeNope(User user) {
        nopeList.remove(user);
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
    }

    public void addInterest(String s) {
        interests.add(s);
    }

    public void removeInterest(String s) {
        interests.remove(s);
    }

    public void addBan (User u) {
        this.banList.add(u);
    }

    public void removeBan (User u) {
        this.banList.remove(u);
    }

    public boolean isMatched(User u) {
        return this.matchList.contains(u);
    }

    public boolean isFollowed(User target) {
        return this.followList.contains(target);
    }

    public boolean isNoped(User target) {
        return this.nopeList.contains(target);
    }

    public boolean isBanned (User u) {
        return this.banList.contains(u);
    }
}
