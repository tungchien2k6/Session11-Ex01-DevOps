package com.ra.pharmacyservice.service.impl;

import com.ra.pharmacyservice.entity.Medicine;
import com.ra.pharmacyservice.repository.MedicineRepository;
import com.ra.pharmacyservice.service.MedicineService;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = "medicines", key = "#medicine.id")
    public Medicine updateMedicine(Medicine medicine) {
        System.out.println("=== Cap nhat thong tin thuoc ID " + medicine.getId() + " vao Database ===");

        Medicine existingMedicine = medicineRepository.findById(medicine.getId())
                .orElseThrow(() -> new RuntimeException("Khong tim thay thuoc voi ID: " + medicine.getId()));

        existingMedicine.setMedicineName(medicine.getMedicineName());
        existingMedicine.setDescription(medicine.getDescription());
        existingMedicine.setPrice(medicine.getPrice());
        existingMedicine.setQuantity(medicine.getQuantity());

        return medicineRepository.save(existingMedicine);
    }

    @Override
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}