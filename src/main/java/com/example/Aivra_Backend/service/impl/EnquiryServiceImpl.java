package com.example.Aivra_Backend.service.impl;

import com.example.Aivra_Backend.dto.request.EnquiryRequest;
import com.example.Aivra_Backend.dto.response.EnquiryResponse;
import com.example.Aivra_Backend.entity.Enquiry;
import com.example.Aivra_Backend.enums.EnquiryStatus;
import com.example.Aivra_Backend.exception.ResourceNotFoundException;
import com.example.Aivra_Backend.repositry.EnquiryRepository;
import com.example.Aivra_Backend.service.EnquiryService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnquiryServiceImpl implements EnquiryService {

    private final EnquiryRepository enquiryRepository;

    public EnquiryServiceImpl(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @Override
    public EnquiryResponse createEnquiry(EnquiryRequest request) {
        Enquiry enquiry = new Enquiry();


        enquiry.setName(request.getName().trim());
        enquiry.setEmail(request.getEmail().trim());
        enquiry.setMessage(request.getMessage().trim());
        enquiry.setPhone(request.getPhone().trim());
        enquiry.setStatus(EnquiryStatus.NEW);
        enquiry.setCity(request.getCity().trim());

        Enquiry saved = enquiryRepository.save(enquiry);

        return toResponse(saved);

    }

    @Override
    public List<Enquiry> getClosedEnquiry(){
        return enquiryRepository.findByStatus(EnquiryStatus.CLOSED);
    }


    @Override
    public Page<EnquiryResponse> getAllEnquiries(Pageable pageable) {
        return enquiryRepository.findAll(pageable).map(this::toResponse);
    }



    @Override
    public EnquiryResponse updateEnquiry(Long id,EnquiryRequest enquiryRequest){
        Enquiry enquiry = findEnquiryOrThrow(id);
        enquiry.setName(enquiryRequest.getName().trim());
        enquiry.setEmail(enquiryRequest.getEmail().trim());
        enquiry.setMessage(enquiryRequest.getMessage().trim());
        enquiry.setPhone(enquiryRequest.getPhone().trim());
        enquiry.setCity(enquiryRequest.getCity().trim());

        enquiryRepository.save(enquiry);
        return toResponse(enquiry);
    }

    @Override
    @Transactional
    public EnquiryResponse updateEnquiryStatus(
            Long id,
            EnquiryStatus status) {


        Enquiry enquiry = findEnquiryOrThrow(id);
        enquiry.setStatus(status);

        Enquiry updated = enquiryRepository.save(enquiry);

        return toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteEnquiry(Long id) {
        Enquiry enquiry = findEnquiryOrThrow(id);
        enquiryRepository.delete(enquiry);
    }

    private Enquiry findEnquiryOrThrow(Long id) {
        return enquiryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Enquiry not found with ID: " + id
                        )
                );
    }


    private EnquiryResponse toResponse(Enquiry enquiry) {
        return new EnquiryResponse(
                enquiry.getId(),
                enquiry.getName(),
                enquiry.getEmail(),
                enquiry.getPhone(),
                enquiry.getMessage(),
                enquiry.getCity(),
                enquiry.getStatus(),
                enquiry.getCreatedAt()
        );
    }

}