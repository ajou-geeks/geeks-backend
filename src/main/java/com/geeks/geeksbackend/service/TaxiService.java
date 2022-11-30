package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.entity.Taxi;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final TaxiRepository taxiRepository;

    public List<Taxi> findAll() {
        return taxiRepository.findAll();
    }

    public Optional<Taxi> findById(long id) {
        return taxiRepository.findById(id);
    }
}
