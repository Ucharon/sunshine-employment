package cn.homyit.service;

import javax.servlet.http.HttpServletResponse;

public interface QrCodeService {

    void createCodeToFile(String content, String filePath);

    void createCodeToStream(String content, HttpServletResponse response);
}
