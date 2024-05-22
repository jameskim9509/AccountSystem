package com.example.account.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class AccountUser {
    @Id @GeneratedValue(generator = "accountUserGenerator")
    private Long id;

    private String name;
}
