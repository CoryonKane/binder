package com.codecool.binder.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue
    // kinda private
    private Long id;
    // public
    private String firstName;
    // public
    private String lastName;
    // public
    private String nickName;
    // nagyon private
    private String password;
    // private
    private String email;
    // public
    private String profilePicture;
    // private
    @ElementCollection
    private List<SimpleGrantedAuthority> roles;
    // user állíthatja egyenként hogy public vagy private
    @ElementCollection
    @Singular
    private Map<Profile, Boolean> profileNames;
    // public
    @ElementCollection
    @Singular
    private Set<String> interests;
    // user állíthatja egyenként hogy public vagy private
    @ElementCollection
    @Singular
    private Map<Project, Boolean> projects;
    // public, followwal news feedbe kerül
    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    private Set<Post> posts;
    // csak saját magadnak settingsben
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Singular("match")
    private Set<User> matchList;
    // csak saját magadnak settingsben
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Singular("follow")
    private Set<User> followList;
    // csak saját magadnak settingsben
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @Singular("nope")
    private Set<User> nopeList;

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

    public boolean removeInterest(String s) {
        return interests.remove(s);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasMatch(User sessionUser) {
        return this.followList.contains(sessionUser);
    }
}
