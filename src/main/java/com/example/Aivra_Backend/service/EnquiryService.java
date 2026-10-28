package com.example.Aivra_Backend.service;

import com.example.Aivra_Backend.dto.request.EnquiryRequest;
import com.example.Aivra_Backend.dto.response.EnquiryResponse;
import com.example.Aivra_Backend.entity.Enquiry;
import com.example.Aivra_Backend.enums.EnquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnquiryService {
    EnquiryResponse createEnquiry(EnquiryRequest request);

    Page<EnquiryResponse> getAllEnquiries(Pageable pageable);

    List<Enquiry> getClosedEnquiry();

    EnquiryResponse updateEnquiry(Long id,EnquiryRequest request);


    EnquiryResponse updateEnquiryStatus(
            Long id,
            EnquiryStatus status
    );

    void deleteEnquiry(Long id);
}
