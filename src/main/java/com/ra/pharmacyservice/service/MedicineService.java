package com.ra.pharmacyservice.service;

import com.ra.pharmacyservice.entity.Medicine;

public interface MedicineService {
    Medicine getMedicineById(Long id);
    Medicine createMedicine(Medicine medicine);
    Medicine updateMedicine(Medicine medicine);
}