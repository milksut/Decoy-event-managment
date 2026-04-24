package com.Decoy.event_managment.Repositorys;

import com.Decoy.event_managment.Models.Duyuru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DuyuruRepository extends JpaRepository<Duyuru, Long>
{
    List<Duyuru> findByEtkinlikTarihiBetween(LocalDateTime start, LocalDateTime end);
    List<Duyuru> findByDurum(Integer durum);
    List<Duyuru> findByDurumLessThan(Integer durum);
    List<Duyuru> findByDurumGreaterThan(Integer durum);
    List<Duyuru> findByDurumLessThanEqual(Integer durum);
    List<Duyuru> findByDurumGreaterThanEqual(Integer durum);
}