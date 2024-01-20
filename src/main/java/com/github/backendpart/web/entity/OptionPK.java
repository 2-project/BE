package com.github.backendpart.web.entity;

import jakarta.persistence.Column;

import java.io.Serializable;

public class OptionPK implements Serializable {
    @Column(name="option_id")
    private String optionName;
    private Long productId;
}
