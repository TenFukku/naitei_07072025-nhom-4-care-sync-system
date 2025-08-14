package com.example.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.backend.dto.DoctorDto;
import com.example.backend.dto.DoctorSearchRequest;
import com.example.backend.dto.PageResponse;
import com.example.backend.entity.Doctor;
import com.example.backend.mapper.DoctorMapper;
import com.example.backend.repository.DoctorRepository;
import com.example.backend.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorDto> getAllDoctors(){
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.toDtoList(doctors);
    }

    @Override
    public List<DoctorDto> getAllActiveDoctors(){
        List<Doctor> doctors = doctorRepository.findAllActiveDoctors();
        return doctorMapper.toDtoList(doctors);
    }

    @Override
    public List<DoctorDto> getDoctorsBySpecialty(Long specialtyId){
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialtyId);
        return doctorMapper.toDtoList(doctors);
    }

    //search and paging
    @Override
    public PageResponse<DoctorDto> searchDoctors(DoctorSearchRequest request){
        String sortBy = validateSortBy(request.getSortBy());

        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDirection().toUpperCase()), sortBy)
        );

        Page<Doctor> doctorPage = doctorRepository.searchDoctors(
                request.getSearchTerm(),
                request.getSpecialtyId(),
                request.getMinExperience(),
                request.getMaxExperience(),
                request.getMinFee(),
                request.getMaxFee(),
                pageRequest
        );

        List<DoctorDto> doctorDtos = doctorMapper.toDtoList(doctorPage.getContent());

        return PageResponse.<DoctorDto>builder()
                .content(doctorDtos)
                .pageable(PageResponse.PageableInfo.builder()
                        .page(doctorPage.getNumber())
                        .size(doctorPage.getSize())
                        .totalElements(doctorPage.getTotalElements())
                        .totalPages(doctorPage.getTotalPages())
                        .first(doctorPage.isFirst())
                        .last(doctorPage.isLast())
                        .hasNext(doctorPage.hasNext())
                        .hasPrevious(doctorPage.hasPrevious())
                        .build())
                .build();
    }

    private String validateSortBy(String sortBy) {
        if (sortBy == null) return "id";

        switch (sortBy.toLowerCase()) {
            case "id":
            case "experienceyears":
            case "consultationfee":
            case "title":
                return sortBy;
            default:
                return "id"; 
        }
    }
}
