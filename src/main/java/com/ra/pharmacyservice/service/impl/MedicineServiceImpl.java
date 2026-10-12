package com.ra.pharmacyservice.service.impl;

import com.ra.pharmacyservice.entity.Medicine;
import com.ra.pharmacyservice.repository.MedicineRepository;
import com.ra.pharmacyservice.service.MedicineService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    @Cacheable(value = "medicines", key = "#id")
    public Medicine getMedicineById(Long id) {
        System.out.println("=== Dang truy van Database tim thuoc ID: " + id + " ===");

        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Khong tim thay thuoc voi ID: " + id));
    }

    @Override
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}