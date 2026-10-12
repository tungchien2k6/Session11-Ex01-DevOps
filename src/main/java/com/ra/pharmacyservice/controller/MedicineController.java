package com.ra.pharmacyservice.controller;

import com.ra.pharmacyservice.entity.Medicine;
import com.ra.pharmacyservice.service.MedicineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineDetail(@PathVariable Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        return ResponseEntity.ok(medicine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicine) {
        medicine.setId(id);
        return ResponseEntity.ok(medicineService.updateMedicine(medicine));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Medicine medicine){
        Medicine medicineNew = medicineService.createMedicine(medicine);
        return new ResponseEntity<>(medicineNew, HttpStatus.CREATED);
    }

    @PutMapping("/sell/{id}")
    public void sellMedicine(@PathVariable Long id) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 1 : " + rsBuy);
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                String rsBuy = medicineService.sellMedicine(id);
                System.out.println("Người dùng 2 : " + rsBuy);
            }
        });

        thread2.start();
        thread1.start();
    }
}