package com.RedBus.DriverJobPage.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="driver_job_details")
public class Driver {

    @Id
    private String id;

    @Column(name="full_name",nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String experience;
    @Lob
    @Column(name="licence_image",nullable = false)
    private byte[] licenceImage;
    @Column(nullable = false)
    private String address;
    @Column(name="expected_salary",nullable = false)
    private String expectedSalary;
}
