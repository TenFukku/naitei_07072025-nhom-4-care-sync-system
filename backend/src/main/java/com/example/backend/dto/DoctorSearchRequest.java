package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSearchRequest {
    
    // Tìm kiếm
    private String searchTerm; // Tìm theo tên bác sĩ hoặc chuyên khoa
    
    // Lọc
    private Long specialtyId; // Lọc theo chuyên khoa
    private Integer minExperience; // Kinh nghiệm tối thiểu
    private Integer maxExperience; // Kinh nghiệm tối đa
    private BigDecimal minFee; // Phí khám tối thiểu
    private BigDecimal maxFee; // Phí khám tối đa
    
    // Phân trang
    private Integer page = 0; // Trang hiện tại (bắt đầu từ 0)
    private Integer size = 10; // Số lượng item trên mỗi trang
    
    // Sắp xếp - chỉ sắp xếp theo các trường của Doctor
    private String sortBy = "id"; // id, experienceYears, consultationFee
    private String sortDirection = "ASC"; // ASC hoặc DESC
    
    public Sort getSort() {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        return Sort.by(direction, sortBy);
    }
}
