package com.skuratov.jmxProject.spring.jmx;


import com.skuratov.jmxProject.spring.services.MusicService;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@ManagedResource(description = "Manager music service")
public class JmxMConfig {
    private final MusicService musicService;

    public JmxMConfig(MusicService musicService) {
        this.musicService = musicService;
    }


    @ManagedAttribute
    public ArrayList<String> getAllMusic() {
        return new ArrayList<>(musicService.getDescriptionByMusic().keySet());
    }

    @ManagedOperation
    public void setMusic(String music, String description) {
        musicService.setMusic(music, description);
    }

    @ManagedOperation
    public String getMusic(String description) {
        return musicService.getDescription(description);
    }

    @ManagedOperation
    public void removeMusic(String music){
        musicService.removeMusic(music);
    }
}
