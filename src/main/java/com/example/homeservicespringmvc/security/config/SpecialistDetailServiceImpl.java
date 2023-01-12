package com.example.homeservicespringmvc.security.config;

import com.example.homeservicespringmvc.repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialistDetailServiceImpl implements UserDetailsService {

    private final SpecialistRepository specialistRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return specialistRepository.findSpecialistByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
    }
}
