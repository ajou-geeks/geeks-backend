package com.geeks.geeksbackend.service;

import java.io.*;
import java.util.Collections;
import java.util.List;

import com.geeks.geeksbackend.dto.Member.FileDto;
import com.geeks.geeksbackend.dto.Member.MemberInfoDto;
import com.geeks.geeksbackend.dto.Member.UpdateMemberDto;
import com.geeks.geeksbackend.dto.Member.UserDto;
import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.entity.MemberPattern;
import com.geeks.geeksbackend.exception.DuplicateMemberException;
import com.geeks.geeksbackend.exception.NotFoundMemberException;
import com.geeks.geeksbackend.repository.MemberPatternRepository;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final AwsS3Service awsS3Service;
    private final MemberRepository memberRepository;
    private final MemberPatternRepository memberPatternRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signup(UserDto userDto, String url) throws IOException {
        if (memberRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        FileDto fileDto = convertPdfToTxt(convert(userDto.getFile()));

        Member member = Member.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .filename(url)
                .dormitory(fileDto.getDormitory())
                .ho(fileDto.getHo())
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(memberRepository.save(member));
    }

    public MemberInfoDto findById(long id) {
        Member member = memberRepository.findById(id);
        List<MemberPattern> memberPatterns = memberPatternRepository.findAllById(id);
        if (member != null) {
            MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                    .id(member.getId())
                    .password(null)
                    .email(member.getEmail())
                    .profileImage(member.getProfileImage())
                    .filename(member.getFilename())
                    .dormitory(member.getDormitory())
                    .ho(member.getHo())
                    .detail(member.getDetail())
                    .pattern(member.getPattern())
                    .patternDetail(member.getPatternDetail())
                    .authorities(member.getAuthorities())
                    .memberPatterns(memberPatterns)
                    .build();
            return memberInfoDto;
        }
        return null;
    }
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void update(long id, UpdateMemberDto updateMemberDto) {
        Member selectedMember = memberRepository.findById(id);
        if (selectedMember != null) {
            selectedMember.setDetail(updateMemberDto.getDetail() == null ? selectedMember.getDetail() : updateMemberDto.getDetail());
            selectedMember.setPattern(updateMemberDto.getPattern() == null ? selectedMember.getPattern() : updateMemberDto.getPattern());
            selectedMember.setPatternDetail(updateMemberDto.getPatternDetail() == null ? selectedMember.getPatternDetail() : updateMemberDto.getPatternDetail());
            memberRepository.save(selectedMember);
        }
    }

    public void update(long id, MultipartFile multipartFile) {
        Member selectedMember = memberRepository.findById(id);
        if (selectedMember != null) {
            String url = awsS3Service.uploadFileV1(multipartFile);
            selectedMember.setProfileImage(url);
            memberRepository.save(selectedMember);
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String email) {
        return UserDto.from(memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    public FileDto convertPdfToTxt(File file) throws IOException {
        PDDocument pdf = PDDocument.load(file);
        String text = new PDFTextStripper().getText(pdf);

        String parsed = text.substring(0, text.indexOf("호실"));
        parsed = parsed.substring(parsed.lastIndexOf(":") + 2, parsed.length());
        String dormitory = parsed.split(" ")[0];
        String ho = parsed.split(" ")[1];

        FileDto fileDto = FileDto.builder()
                .dormitory(dormitory)
                .ho(ho)
                .build();

        return fileDto;
    }

    public File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }
}