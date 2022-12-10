package com.geeks.geeksbackend.service;

import java.io.*;
import java.util.Collections;
import java.util.List;

import com.geeks.geeksbackend.dto.user.FileDto;
import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.dto.user.UpdateUserDto;
import com.geeks.geeksbackend.dto.user.UserDto;
import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.entity.UserCharacter;
import com.geeks.geeksbackend.exception.DuplicateUserException;
import com.geeks.geeksbackend.exception.NotFoundUserException;
import com.geeks.geeksbackend.repository.UserCharacterRepository;
import com.geeks.geeksbackend.repository.UserRepository;
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
public class UserService {

    private final AwsS3Service awsS3Service;
    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signup(UserDto userDto, String url) throws IOException {
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateUserException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        FileDto fileDto = convertPdfToTxt(convert(userDto.getFile()));

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .filename(url)
                .dormitory(fileDto.getDormitory())
                .ho(fileDto.getHo())
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(userRepository.save(user));
    }

    public UserInfoDto findById(long id) {
        User user = userRepository.findById(id);
        List<UserCharacter> userCharacters = userCharacterRepository.findAllById(id);
        if (user != null) {
            UserInfoDto userInfoDto = UserInfoDto.builder()
                    .id(user.getId())
                    .password(null)
                    .email(user.getEmail())
                    .profileImage(user.getProfileImage())
                    .filename(user.getFilename())
                    .dormitory(user.getDormitory())
                    .ho(user.getHo())
                    .detail(user.getDetail())
                    .pattern(user.getPattern())
                    .patternDetail(user.getPatternDetail())
                    .authorities(user.getAuthorities())
                    .userCharacters(userCharacters)
                    .build();
            return userInfoDto;
        }
        return null;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void update(long id, UpdateUserDto updateUserDto) {
        User selectedUser = userRepository.findById(id);
        if (selectedUser != null) {
            selectedUser.setDetail(updateUserDto.getDetail() == null ? selectedUser.getDetail() : updateUserDto.getDetail());
            selectedUser.setPattern(updateUserDto.getPattern() == null ? selectedUser.getPattern() : updateUserDto.getPattern());
            selectedUser.setPatternDetail(updateUserDto.getPatternDetail() == null ? selectedUser.getPatternDetail() : updateUserDto.getPatternDetail());
            userRepository.save(selectedUser);
        }
    }

    public void update(long id, MultipartFile multipartFile) {
        User selectedUser = userRepository.findById(id);
        if (selectedUser != null) {
            String url = awsS3Service.uploadFileV1(multipartFile);
            selectedUser.setProfileImage(url);
            userRepository.save(selectedUser);
        }
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String email) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundUserException("User not found"))
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
