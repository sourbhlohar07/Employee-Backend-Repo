package com.example.Aivra_Backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnquiryRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter valid Email address")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Enter a valid 10-digit mobile number"
    )
    private String phone;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;


    public EnquiryRequest(String name, String email, String phone, String message, String city) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.message = message;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }



    public String getMessage() {
        return message;
    }


    public String getCity() {
        return city;
    }


}
