package com.geeks.geeksbackend.service;

import java.io.*;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import com.geeks.geeksbackend.dto.FileDto;
import com.geeks.geeksbackend.dto.UserDto;
import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.exception.DuplicateMemberException;
import com.geeks.geeksbackend.exception.NotFoundMemberException;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final AwsS3Service awsS3Service;
    private final MemberRepository memberRepository;
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

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void update(long id, MultipartFile multipartFile, Member member) {
        Optional<Member> optionalMember = findById(id);
        optionalMember.ifPresent(m -> {
            String url = awsS3Service.uploadFileV1(multipartFile);
            m.setProfileImage(url);
            m.setDetail(member.getDetail());
            m.setPattern(member.getPattern());
            m.setPatternDetail(m.getPatternDetail());
            memberRepository.save(m);
        });
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String name) {
        return UserDto.from(memberRepository.findOneWithAuthoritiesByEmail(name).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

//    public String saveFile(MultipartFile file) throws IOException {
//        if (file.isEmpty()) {
//            return null;
//        }
//
//        String originalName = file.getOriginalFilename();
//        String uuid = UUID.randomUUID().toString();
//        String extension = originalName.substring(originalName.lastIndexOf("."));
//        String savedName = uuid + extension;
//        String savedPath = fildDir + savedName;
//
//        File createdFile = new File(savedPath);
//        file.transferTo(createdFile);
//        return savedName;
//    }

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