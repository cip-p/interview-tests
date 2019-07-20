package cip.interview.passmanagement.web;

import lombok.Data;

@Data
public class PassRequest {

    private String customerName;

    private String homeCity;

    private String passCity;

    private Long passLength;
}
