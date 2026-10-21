package com.app.Q_Entertainment.Service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {

    void uploadVideo(MultipartFile file);

}
