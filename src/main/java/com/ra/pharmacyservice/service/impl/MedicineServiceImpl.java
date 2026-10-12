package com.ra.pharmacyservice.service.impl;

import com.ra.pharmacyservice.entity.Medicine;
import com.ra.pharmacyservice.repository.MedicineRepository;
import com.ra.pharmacyservice.service.MedicineService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final RedissonClient redissonClient;

    public MedicineServiceImpl(MedicineRepository medicineRepository, RedissonClient redissonClient) {
        this.medicineRepository = medicineRepository;
        this.redissonClient = redissonClient;
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
    public String sellMedicine(Long id) {
        String lockKey = "lock:medicine:" + id;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isAcquired = lock.tryLock(3, 5, TimeUnit.SECONDS);

            if (!isAcquired) {
                return "He thong dang ban, vui long thu lai!";
            }

            try {
                Medicine medicine = medicineRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Khong tim thay thuoc ID: " + id));

                if (medicine.getQuantity() <= 0) {
                    return "San pham da het hang!";
                }

                medicine.setQuantity(medicine.getQuantity() - 1);
                medicineRepository.save(medicine);

                return "Thanh toan thanh cong thuoc: " + medicine.getMedicineName();

            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Lỗi xử lý luồng!";
        }
    }

    @Override
    public Medicine createMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }
}