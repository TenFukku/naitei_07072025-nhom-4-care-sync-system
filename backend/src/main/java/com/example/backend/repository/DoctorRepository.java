package com.example.backend.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("SELECT d " +
            "FROM Doctor d " +
            "JOIN FETCH d.user u " +
            "JOIN FETCH d.specialty s " +
            "WHERE d.user.isActive = true")
    List<Doctor> findAllActiveDoctors();

    @Query("SELECT d " +
            "FROM Doctor d " +
            "JOIN FETCH d.user u " +
            "JOIN FETCH d.specialty s " +
            "WHERE d.specialty.id = :specialtyId")
    List<Doctor> findBySpecialty(@Param("specialtyId") Long specialtyId);

    //paging
    @Query("SELECT d " +
            "FROM Doctor d " +
            "JOIN d.user u " +
            "JOIN d.specialty s " +
            "WHERE d.user.isActive = true " +
            "AND (:searchTerm IS NULL OR " +
            "   LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "   LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND (:specialtyId IS NULL OR s.id = :specialtyId) " +
            "AND (:minExperience IS NULL OR d.experienceYears >= :minExperience) " +
            "AND (:maxExperience IS NULL OR d.experienceYears <= :maxExperience) " +
            "AND (:minFee IS NULL OR d.consultationFee >= :minFee) " +
            "AND (:maxFee IS NULL OR d.consultationFee <= :maxFee)")
    Page<Doctor> searchDoctors(
            @Param("searchTerm") String searchTerm,
            @Param("specialtyId") Long specialtyId,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            @Param("minFee") BigDecimal minFee,
            @Param("maxFee") BigDecimal maxFee,
            Pageable pageable
    );
}
