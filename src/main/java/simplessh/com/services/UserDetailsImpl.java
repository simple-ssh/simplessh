package simplessh.com.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import simplessh.com.dao.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Corneli F.
 *
 * still don't use this class probably will usefor future
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;

    @JsonIgnore
    private String password;

    private User user;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl( String username,  String password,
                           Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<String> roles = Arrays.asList(user.getRoles());

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUsername(),
                user.getUsername(),
                authorities);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(username, user.username);
    }
}
