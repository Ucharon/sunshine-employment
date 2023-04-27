package cn.homyit.service.impl;

import cn.homyit.enums.ResultCodeEnum;
import cn.homyit.exception.BizException;
import cn.homyit.service.QrCodeService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeException;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: graduate-website
 * @description:
 * @author: Charon
 * @create: 2023-03-27 19:49
 **/
@Service
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    @Resource
    private QrConfig config;

    @Override
    public void createCodeToFile(String content, String filePath) {
        try {
            QrCodeUtil.generate(content, config, FileUtil.file(filePath));
        } catch (QrCodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createCodeToStream(String content, HttpServletResponse response) {
        try {
            QrCodeUtil.generate(content, config, "png",response.getOutputStream());
        } catch (QrCodeException | IOException e) {
            throw new BizException(ResultCodeEnum.QR_ERROR, e);
        }
    }


}
