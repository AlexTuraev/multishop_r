package org.tasks.myshop.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.tasks.myshop.dao.model.UserEntity;
import org.tasks.myshop.dao.repository.UserRepository;

import java.util.List;


public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .doOnNext(u -> {
                    if (u == null) {
                        throw new UsernameNotFoundException(username);
                    }
                })
                .blockFirst();

        return new User(
//                user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))
                user.getUsername(), user.getPassword(), List.of()
        );
    }
}
