package cip.interview.passmanagement.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Pass {

    @Id
    @GeneratedValue
    private Long id;

    private String customerName;

    private String homeCity;

    private String passCity;

    private Long passLength;

    private String passId;

    private String vendorId;
}
