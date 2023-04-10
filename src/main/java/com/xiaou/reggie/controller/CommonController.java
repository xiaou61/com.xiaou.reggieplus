package com.xiaou.reggie.controller;

import com.xiaou.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 *
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    /**
     * 文件上传
     * @param file
     * @return
     */

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        //获得后缀
        String suffix= originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID
        String fileName = UUID.randomUUID().toString()+suffix;


        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()) {
            //目录不存在，创建
            dir.mkdirs();
        }
        //file是一个临时文件，需要转存到指定的位置，否则本次请求会被删除
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return R.success(fileName);
    }

    /***
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fis=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件回浏览器，在浏览器展示图片
            ServletOutputStream outputStream=response.getOutputStream();
            //图片文件
            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes = new byte[2048];
            while ((len=fis.read(bytes))!=-1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}