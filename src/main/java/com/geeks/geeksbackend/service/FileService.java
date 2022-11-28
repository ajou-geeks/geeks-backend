package com.geeks.geeksbackend.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.pdf.dir}")
    private String filePdfDir;
    @Value("${file.txt.dir}")
    private String fileTxtDir;

    public String saveFile(MultipartFile file) throws IOException {
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
        convertPdfToTxt(createdFile);

        return savedName;
    }

    public void convertPdfToTxt(File file) throws IOException {
        PDDocument pdf = PDDocument.load(file);
        String text = new PDFTextStripper().getText(pdf);

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
    }
}
