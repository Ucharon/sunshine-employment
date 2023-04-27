package cn.homyit.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExcelService {


    void uploadUsers(MultipartFile file);
}
