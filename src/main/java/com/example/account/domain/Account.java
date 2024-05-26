package com.example.account.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(generator = "accountGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_USER_ID")
    private AccountUser accountUser;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private Long balance;
    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;
}
