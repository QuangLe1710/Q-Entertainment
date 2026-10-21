package com.app.Q_Entertainment.Controller;

import com.app.Q_Entertainment.Model.DTO.ApiResponse;
import com.app.Q_Entertainment.Model.DTO.ResponseUtil;
import com.app.Q_Entertainment.Service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/private/api/video")
@AllArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/uploadVideo")
    public ApiResponse<?> uploadVideo(HttpServletRequest request, @RequestParam("file") MultipartFile file){

        videoService.uploadVideo(file);

        return ResponseUtil.success(null, "Upload video successfully", request.getRequestURI());
    }

}
