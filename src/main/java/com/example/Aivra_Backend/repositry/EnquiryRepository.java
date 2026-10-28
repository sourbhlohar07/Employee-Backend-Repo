package com.example.Aivra_Backend.repositry;

import com.example.Aivra_Backend.entity.Enquiry;
import com.example.Aivra_Backend.enums.EnquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
    //Always use Respositry method name such as findByStatus, findByEmail, findByNumber
    List<Enquiry> findByStatus(EnquiryStatus status);
}
