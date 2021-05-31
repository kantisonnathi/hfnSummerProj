package org.heartfulness.avtc.Springsec;

/*import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class MyUserDetails implements UserDetails {
    private String email;
    private String password;
    private int id;
    private boolean phonev;
    private boolean emailv;
    private AuthenticationProvider authenticationProvider;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  MyUserDetails(UserEntity user)
    {
        this.email=user.getEmail();
        this.password=user.getPassword();
        this.id=user.getId();
        this.phonev=user.isPhoneVerified();
        this.emailv=user.isEmailVerified();
        this.authenticationProvider=user.getAuthProvider();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    public int getId()
    {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
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
        if(phonev&&emailv) {
            return true;
        }
        return false;
    }
}*/
