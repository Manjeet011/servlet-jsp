package com.misha.controller;
import com.misha.model.Registration;
import com.misha.model.Role;
import com.misha.payload.request.RequestRegistration;
import com.misha.payload.request.UpdateRegistration;
import com.misha.payload.response.RegistrationDTO;
import com.misha.payload.response.ResponseRegistration;
import com.misha.repository.RegistrationRepository;
import com.misha.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@RestController
@RequestMapping("/user")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private RegistrationRepository registaationRepository;

    @PostMapping
    public ResponseEntity<?> saveRegistrationDetails(@Valid @RequestBody RequestRegistration request) {
        try {
            Registration reg = registrationService.getRegistrationByEmail(request.getEmail(), request.getPassword());

            if (reg != null) {
                ResponseRegistration response = new ResponseRegistration(false, "Email already in use !");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

        } catch (NullPointerException e) {
            System.out.println("Caught a NullPointerException!");
            e.printStackTrace();
        }

        Registration registration = new Registration(request.getRoles(), request.getPassword(), request.getEmail());
        registrationService.saveRegistrationDetails(registration);
        RegistrationDTO responseDTO = new RegistrationDTO(
                registration.getId(),
                registration.getUuid(),
                registration.getEmail(),
                registration.getRoles(),
                registration.isStatus(),
                registration.isIsdeleted(),
                registration.getCreatedAt(),
                registration.getCreatedBy(),
                registration.getUpdatedAt(),
                registration.getUpdatedBy()
        );
        ResponseRegistration response = new ResponseRegistration(true, "Registration Successful", responseDTO);
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UpdateRegistration updateRegistration) {

        Registration registration = registrationService.getRegistrationByEmail(updateRegistration.getEmail(), updateRegistration.getPassword());
        if (registration == null) {

            ResponseRegistration response = new ResponseRegistration(false, "This Credential Does not Exits in the table please provide valid one ");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        int updated = registrationService.UpdateRegistrationDetails(updateRegistration.getRoles(), LocalDateTime.now(), updateRegistration.getEmail());
        Registration registration1 = registrationService.getRegistrationByEmailAndPassword(updateRegistration.getEmail(), updateRegistration.getPassword());
        RegistrationDTO responseDTO = new RegistrationDTO(
                registration1.getId(),
                registration1.getUuid(),
                registration1.getEmail(),
                registration1.getRoles(),
                registration1.isStatus(),
                registration1.isIsdeleted(),
                registration1.getCreatedAt(),
                registration1.getCreatedBy(),
                registration1.getUpdatedAt(),
                registration1.getUpdatedBy()
        );
        ResponseRegistration response = new ResponseRegistration(true, "Updation Successful", responseDTO);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> deleteRegistration(@PathVariable String uuid) {
        try {
            Registration registration = registaationRepository.findByUuid(uuid);

            if (registration == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseRegistration(false, "User not found"));
            }

            registaationRepository.deleteByUuid(uuid);

            RegistrationDTO responseDTO = new RegistrationDTO(
                    registration.getId(),
                    registration.getUuid(),
                    registration.getEmail(),
                    registration.getRoles(),
                    registration.isStatus(),
                    registration.isIsdeleted(),
                    registration.getCreatedAt(),
                    registration.getCreatedBy(),
                    registration.getUpdatedAt(),
                    registration.getUpdatedBy()
            );

            return ResponseEntity.ok(new ResponseRegistration(true, "Deletion Successful", responseDTO));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseRegistration(false, "An error occurred"));
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getUserByUuid(@PathVariable String uuid) {
        try {
            Registration registration = registaationRepository.findByUuid(uuid);

            if (registration == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseRegistration(false, "User not found"));
            }
            Set<Role> roles = new HashSet<>();
            RegistrationDTO responseDTO = new RegistrationDTO(
                    registration.getId(),
                    registration.getUuid(),
                    registration.getEmail(),
                    registration.getRoles(),
                    registration.isStatus(),
                    registration.isIsdeleted(),
                    registration.getCreatedAt(),
                    registration.getCreatedBy(),
                    registration.getUpdatedAt(),
                    registration.getUpdatedBy()
            );

            return ResponseEntity.ok(new ResponseRegistration(true, "Data Fetched Successfully ", responseDTO));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseRegistration(false, "An error occurred"));
        }
    }

    @GetMapping("/{page}/{size}/{sortBy}/{direction}")
    public ResponseEntity<Page<Registration>> getAllRegistrations(
            @PathVariable int page,
            @PathVariable int size,
            @PathVariable String sortBy,
            @PathVariable String direction) {

        direction = direction.equalsIgnoreCase("desc") ? "desc" : "asc";
        Page<Registration> registrations = registrationService.fetchAllDetails(size, page, sortBy, direction);
        return ResponseEntity.ok(registrations);
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<?> updateRegistration(
            @PathVariable String uuid, @Valid @RequestBody UpdateRegistration updateRegistration

    ) {

        Registration registration = registaationRepository.findByUuid(uuid);
        if (registration == null) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseRegistration(false, "User not found"));
          }
            RegistrationDTO responseDTO = null;

            int x = registrationService.UpdateRegistration(updateRegistration.getRoles(), LocalDateTime.now(), updateRegistration.getEmail(), uuid);
            if (x > 0) {
                Registration registration1 = registaationRepository.findByUuid(uuid);
                responseDTO = new RegistrationDTO(
                        registration1.getId(),
                        registration1.getUuid(),
                        registration1.getEmail(),
                        registration1.getRoles(),
                        registration1.isStatus(),
                        registration1.isIsdeleted(),
                        registration1.getCreatedAt(),
                        registration1.getCreatedBy(),
                        registration1.getUpdatedAt(),
                        registration1.getUpdatedBy()
                );

            }
            ResponseRegistration response = new ResponseRegistration(true, "Updation Successful", responseDTO);
            return ResponseEntity.status(200).body(response);

        }

}
