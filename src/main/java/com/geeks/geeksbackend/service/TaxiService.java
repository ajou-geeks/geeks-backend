package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.notice.NoticeDto;
import com.geeks.geeksbackend.dto.taxi.ChangeDto;
import com.geeks.geeksbackend.dto.taxi.CreateDto;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.entity.Taxi;
import com.geeks.geeksbackend.entity.TaxiUser;
import com.geeks.geeksbackend.enumeration.GroupBuyingStatus;
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

import static com.geeks.geeksbackend.enumeration.MessageTemplate.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final UserRepository userRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiUserRepository taxiUserRepository;

    private final NoticeService noticeService;

    public List<Taxi> getTaxis() {
        return taxiRepository.findAll();
    }

    public Optional<Taxi> getTaxi(long id) {
        return taxiRepository.findById(id);
    }

    public List<TaxiUser> getTaxiUsers(long id) {
        return taxiUserRepository.findByTaxiId(id);
    }

    public void createTaxi(CreateDto createDto, Long userId) {
        Taxi taxi = Taxi.builder()
                .userId(userId)
                .price(createDto.getPrice())
                .startTime(LocalDateTime.parse(createDto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(createDto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .maxParticipant(createDto.getMaxParticipant())
                .source(createDto.getSource())
                .destination(createDto.getDestination())
                .status(GroupBuyingStatus.OPEN)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        Taxi newTaxi = taxiRepository.save(taxi);
        User user = userRepository.findById(userId).get();

        TaxiUser taxiUser = TaxiUser.builder()
                .taxiId(newTaxi.getId())
                .userId(newTaxi.getUserId())
                .email(user.getEmail())
                .build();

        taxiUserRepository.save(taxiUser);
    }

    public boolean cancleTaxi(ChangeDto changeDto, Long userId) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getUserId() != userId) {
                return false;
            }
            taxi.setDeleted(true);
            taxi.setDeletedAt(LocalDateTime.now());
            taxiRepository.save(taxi);
            return true;
        }
        return false;
    }

    public boolean completeTaxi(ChangeDto changeDto, Long userId) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getUserId() != userId) {
                return false;
            }
            taxi.setStatus(GroupBuyingStatus.COMPLETE);;
            taxi.setUpdatedAt(LocalDateTime.now());
            taxiRepository.save(taxi);
            return true;
        }
        return false;
    }

    /**
     *
     * @param changeDto
     * @return 0 : ?????? ??????
     *         1 : ???????????? ??????
     *         2 : ?????? ????????? ????????? ?????? ????????????
     *         3 : ?????? ????????? ????????????
     *         4 : ???????????? ?????? ????????????
     *         5 : ???????????? ?????? ??????
     */
    public int joinTaxi(ChangeDto changeDto, Long userId) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getEndTime().isAfter(LocalDateTime.now())) {
                return 1;
            }

            if (taxi.getStatus() != GroupBuyingStatus.OPEN) {
                return 2;
            }

            TaxiUser taxiUser = taxiUserRepository.findByTaxiIdAndUserId(changeDto.getId(), userId);
            if (taxiUser != null && taxiUser.getUserId() == userId) {
                return 3;
            }

            User user = userRepository.findById(userId).get();
            if (user != null) {
                TaxiUser newTaxiUser = TaxiUser.builder()
                        .taxiId(changeDto.getId())
                        .userId(userId)
                        .email(user.getEmail())
                        .build();

                taxiUserRepository.save(newTaxiUser);

                // ??????????????? [???????????? ??????] ?????? ??????
                NoticeDto message1 = NoticeDto.builder()
                        .object("TAXI")
                        .title(GROUP_BUYING_JOIN_01.getTitle())
                        .content(GROUP_BUYING_JOIN_01.getContent())
                        .value1(user.getName())
                        .value2(taxi.getDestination())
                        .build();

                noticeService.sendNotice(message1, taxi.getUserId());

                // ??????????????? [?????? ????????????] ?????? ??????
                NoticeDto message2 = NoticeDto.builder()
                        .object("TAXI")
                        .title(TAXI_JOIN_01.getTitle())
                        .content(TAXI_JOIN_01.getContent())
                        .value1(taxi.getDestination())
                        .value2(taxi.getEndTime().format(DateTimeFormatter.ofPattern("Y??? MM??? dd??? HH??? mm???")))
                        .value3(taxi.getSource())
                        .build();

                noticeService.sendNotice(message2, user.getId());

                return 0;
            }
            return 5;
        }
        return 4;
    }
}
