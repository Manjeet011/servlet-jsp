package com.misha.service;

import com.misha.model.Registration;
import com.misha.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.misha.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import java.lang.invoke.StringConcatException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registaationRepository;
    public void saveRegistrationDetails(Registration registration) {
        registaationRepository.save(registration);
    }
    public Registration getRegistrationByEmail(String email,String password) {

        return registaationRepository.findByEmailAndPassword(email,password);
    }
    public int UpdateRegistrationDetails(Set<Role> roles,LocalDateTime updatedAt, String email) {

        return registaationRepository.updateRolesByEmail(roles, updatedAt,email);
    }

    public int UpdateRegistration(Set<Role> roles, LocalDateTime updatedAt, String email, String uuid) {

        return  registaationRepository.updateEmailAndRolesById(email,roles, updatedAt,uuid);

    }
    public Registration getRegistrationByEmailAndPassword(String email, String pass) {

        return registaationRepository.findByEmailAndPassword(email,pass);
    }
    public Page<Registration> fetchAllDetails(int size, int page,String sortBy,String direction) {

        Sort.Direction sortDirection;
        try {
            System.out.println(direction);
            System.out.println(sortBy);
            sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;

        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return registaationRepository.findAll(pageable);
    }


}
