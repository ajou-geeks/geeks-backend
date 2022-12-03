package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.taxi.ChangeDto;
import com.geeks.geeksbackend.dto.taxi.CreateDto;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.entity.Taxi;
import com.geeks.geeksbackend.entity.TaxiUser;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.repository.UserRepository;
import com.geeks.geeksbackend.repository.TaxiUserRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final UserRepository userRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiUserRepository taxiUserRepository;

    public List<Taxi> getTaxis() {
        return taxiRepository.findAll();
    }

    public Optional<Taxi> getTaxi(long id) {
        return taxiRepository.findById(id);
    }

    public List<TaxiUser> getTaxiUsers(long id) {
        return taxiUserRepository.findByTaxiId(id);
    }

    public void createTaxi(CreateDto createDto) {
        Taxi taxi = Taxi.builder()
                .userId(createDto.getUserId())
                .price(createDto.getPrice())
                .startTime(LocalDateTime.parse(createDto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(createDto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .maxParticipant(createDto.getMaxParticipant())
                .source(createDto.getSource())
                .destination(createDto.getDestination())
                .status(CoBuyStatus.OPEN)
                .createdBy(createDto.getUserId())
                .updatedBy(createDto.getUserId())
                .build();

        Taxi newTaxi = taxiRepository.save(taxi);
        User user = userRepository.findById(createDto.getUserId());

        TaxiUser taxiUser = TaxiUser.builder()
                .taxiId(newTaxi.getId())
                .userId(newTaxi.getUserId())
                .email(user.getEmail())
                .build();

        taxiUserRepository.save(taxiUser);
    }

    public boolean cancleTaxi(ChangeDto changeDto) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getUserId() != changeDto.getUserId()) {
                return false;
            }
            taxi.setDeleted(true);
            taxi.setDeletedAt(LocalDateTime.now());
            taxiRepository.save(taxi);
            return true;
        }
        return false;
    }

    public boolean completeTaxi(ChangeDto changeDto) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getUserId() != changeDto.getUserId()) {
                return false;
            }
            taxi.setStatus(CoBuyStatus.COMPLETE);;
            taxi.setUpdatedAt(LocalDateTime.now());
            taxiRepository.save(taxi);
            return true;
        }
        return false;
    }

    /**
     *
     * @param changeDto
     * @return 0 : 참여 성공
     *         1 : 참여기간 초과
     *         2 : 참여 가능한 상태가 아닌 공동구매
     *         3 : 이미 참여한 공동구매
     *         4 : 존재하지 않는 공동구매
     *         5 : 존재하지 않는 유저
     */
    public int joinTaxi(ChangeDto changeDto) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getEndTime().isAfter(LocalDateTime.now())) {
                return 1;
            }

            if (taxi.getStatus() != CoBuyStatus.OPEN) {
                return 2;
            }

            TaxiUser taxiUser = taxiUserRepository.findByTaxiIdAndUserId(changeDto.getId(), changeDto.getUserId());
            if (taxiUser != null && taxiUser.getUserId() == changeDto.getUserId()) {
                return 3;
            }

            User user = userRepository.findById(changeDto.getUserId());
            if (user != null) {
                TaxiUser newTaxiUser = TaxiUser.builder()
                        .taxiId(changeDto.getId())
                        .userId(changeDto.getUserId())
                        .email(user.getEmail())
                        .build();

                taxiUserRepository.save(newTaxiUser);
                return 0;
            }
            return 5;
        }
        return 4;
    }
}
