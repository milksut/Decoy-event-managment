package com.Decoy.event_managment.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Duyuru
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @NonNull
    private String etkinlikIsmi;

    private String etkinlikAciklamasi;

    @NonNull
    private Integer durum = 0;

    private LocalDateTime etkinlikTarihi;

    private String ekstra;

    private String thumnailLink;
}
