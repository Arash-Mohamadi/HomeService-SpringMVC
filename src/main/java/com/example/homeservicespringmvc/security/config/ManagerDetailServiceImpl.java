package com.example.homeservicespringmvc.security.config;

import com.example.homeservicespringmvc.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerDetailServiceImpl implements UserDetailsService {

    private final ManagerRepository managerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findManagerByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
}
