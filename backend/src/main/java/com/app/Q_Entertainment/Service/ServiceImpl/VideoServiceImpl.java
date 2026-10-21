package com.app.Q_Entertainment.Service.ServiceImpl;

import com.app.Q_Entertainment.Service.VideoService;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoServiceImpl implements VideoService {
    @Override
    public void uploadVideo(MultipartFile file) {
        System.out.println("File : " + file.getOriginalFilename());
    }
}
