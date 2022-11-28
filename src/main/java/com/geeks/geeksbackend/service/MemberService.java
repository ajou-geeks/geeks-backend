package com.geeks.geeksbackend.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Value("${file.pdf.dir}")
    private String filePdfDir;
    @Value("${file.txt.dir}")
    private String fileTxtDir;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signup(UserDto userDto) throws IOException {
        if (memberRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        FileDto fileDto = saveFile(userDto.getEmail(), userDto.getFile());

        Member member = Member.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .filename(fileDto.getFilename())
                .dormitory(fileDto.getDormitory())
                .ho(fileDto.getHo())
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(memberRepository.save(member));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
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

    public FileDto saveFile(String email, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = uuid + extension;
        String savedPath = filePdfDir + savedName;

        File createdFile = new File(savedPath);
        file.transferTo(createdFile);
        return convertPdfToTxt(email, savedName, createdFile);
    }

    public FileDto convertPdfToTxt(String email, String prevName, File file) throws IOException {
        PDDocument pdf = PDDocument.load(file);
        String text = new PDFTextStripper().getText(pdf);

        String parsed = text.substring(0, text.indexOf("호실"));
        parsed = parsed.substring(parsed.lastIndexOf(":") + 2, parsed.length());
        String dormitory = parsed.split(" ")[0];
        String ho = parsed.split(" ")[1];

        FileDto fileDto = FileDto.builder()
                .filename(prevName)
                .dormitory(dormitory)
                .ho(ho)
                .build();

        String savedName = file.getName();
        savedName = savedName.substring(0, savedName.lastIndexOf("."));
        String savedPath = fileTxtDir + savedName + ".txt";

        File createdFile = new File(savedPath);
        if (!createdFile.exists()) {
            createdFile.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(createdFile, true));
        bw.write(text);
        bw.flush();
        bw.close();

        return fileDto;
    }
}