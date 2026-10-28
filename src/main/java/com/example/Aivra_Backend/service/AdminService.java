package com.example.Aivra_Backend.service;

import com.example.Aivra_Backend.dto.request.AdminRequest;
import com.example.Aivra_Backend.dto.response.AdminResponse;

public interface AdminService {


    AdminResponse getAdmin(Long id);

    AdminResponse updateAdmin(AdminRequest adminRequest);

}
