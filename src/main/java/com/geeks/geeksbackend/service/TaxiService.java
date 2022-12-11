package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.taxi.ChangeDto;
import com.geeks.geeksbackend.dto.taxi.CreateDto;
import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.entity.Taxi;
import com.geeks.geeksbackend.entity.TaxiMember;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.repository.TaxiMemberRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final MemberRepository memberRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiMemberRepository taxiMemberRepository;

    public List<Taxi> getTaxis() {
        return taxiRepository.findAll();
    }

    public Optional<Taxi> getTaxi(long id) {
        return taxiRepository.findById(id);
    }

    public List<TaxiMember> getTaxiMembers(long id) {
        return taxiMemberRepository.findByTaxiId(id);
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
        Member member = memberRepository.findById(createDto.getUserId());

        TaxiMember taxiMember = TaxiMember.builder()
                .taxiId(newTaxi.getId())
                .userId(newTaxi.getUserId())
                .email(member.getEmail())
                .build();

        taxiMemberRepository.save(taxiMember);
    }

    public boolean cancleTaxi(ChangeDto changeDto) {
        Taxi taxi = taxiRepository.findOneById(changeDto.getId());
        if (taxi != null) {
            if (taxi.getUserId() != changeDto.getUserId()) {
                return false;
            }
            taxi.setStatus(CoBuyStatus.CANCEL);
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
            taxi.setStatus(CoBuyStatus.COMPLETE);
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

            TaxiMember taxiMember = taxiMemberRepository.findByTaxiIdAndUserId(changeDto.getId(), changeDto.getUserId());
            if (taxiMember != null && taxiMember.getUserId() == changeDto.getUserId()) {
                return 3;
            }

            Member member = memberRepository.findById(changeDto.getUserId());
            if (member != null) {
                TaxiMember newTaxiMember = TaxiMember.builder()
                        .taxiId(changeDto.getId())
                        .userId(changeDto.getUserId())
                        .email(member.getEmail())
                        .build();

                taxiMemberRepository.save(newTaxiMember);
                return 0;
            }
            return 5;
        }
        return 4;
    }
}
