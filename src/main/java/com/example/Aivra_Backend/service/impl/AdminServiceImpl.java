
package com.example.Aivra_Backend.service.impl;

import com.example.Aivra_Backend.dto.request.AdminRequest;
import com.example.Aivra_Backend.dto.response.AdminResponse;
import com.example.Aivra_Backend.entity.AdminDetail;
import com.example.Aivra_Backend.repositry.AdminRepository;
import com.example.Aivra_Backend.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponse getAdmin(Long id) {
        AdminDetail admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Admin not found with id: " + id
                ));

        return mapToResponse(admin);
    }

    @Override
    public AdminResponse updateAdmin(AdminRequest request) {

        // This interface has no ID parameter, so this method
        // assumes your application manages a single admin account.
        // Confirm the intended admin-selection mechanism before use.
        throw new UnsupportedOperationException(
                "Admin update requires an admin ID or authenticated admin identity"
        );
    }

    private AdminResponse mapToResponse(AdminDetail admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getName(),
                admin.getEmail()
        );
    }
}
