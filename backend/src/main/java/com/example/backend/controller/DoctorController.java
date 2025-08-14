package com.example.backend.controller;

import com.example.backend.constant.ApiConstants;
import com.example.backend.dto.ApiResponse;
import com.example.backend.dto.DoctorDto;
import com.example.backend.dto.DoctorSearchRequest;
import com.example.backend.dto.PageResponse;
import com.example.backend.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.DOCTORS_ENDPOINT)
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "Doctor management APIs")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve a list of all active doctors")
    public ApiResponse<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        return ApiResponse.success(doctors, "Doctors retrieved successfully");
    }

    @GetMapping("/specialty/{specialtyId}")
    @Operation(summary = "Get doctors by specialty", description = "Retrieve doctors filtered by specialty ID")
    public ApiResponse<List<DoctorDto>> getDoctorsBySpecialty(@PathVariable Long specialtyId) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecialty(specialtyId);
        return ApiResponse.success(doctors, "Doctors retrieved successfully");
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctors with pagination", description = "Search and filter doctors with pagination support")
    public ApiResponse<PageResponse<DoctorDto>> searchDoctors(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long specialtyId,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) BigDecimal minFee,
            @RequestParam(required = false) BigDecimal maxFee,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
            ){
        try{
            DoctorSearchRequest request = DoctorSearchRequest.builder()
                    .searchTerm(searchTerm)
                    .specialtyId(specialtyId)
                    .minExperience(minExperience)
                    .maxExperience(maxExperience)
                    .minFee(minFee)
                    .maxFee(maxFee)
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .sortDirection(sortDirection)
                    .build();

            PageResponse<DoctorDto> result = doctorService.searchDoctors(request);
            return ApiResponse.success(result, "Doctors retrieved successfully");
        } catch (Exception e) {
            return ApiResponse.error("Failed to search doctors: " + e.getMessage());
        }
    }
}
