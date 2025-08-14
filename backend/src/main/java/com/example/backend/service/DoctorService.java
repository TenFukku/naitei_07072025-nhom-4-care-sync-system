package com.example.backend.service;

import com.example.backend.dto.DoctorDto;
import com.example.backend.dto.DoctorSearchRequest;
import com.example.backend.dto.PageResponse;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> getAllDoctors();
    List<DoctorDto> getAllActiveDoctors();
    List<DoctorDto> getDoctorsBySpecialty(Long specialtyId);

    //Search and Paging
    PageResponse<DoctorDto> searchDoctors(DoctorSearchRequest request);
}
