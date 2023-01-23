package com.skuratov.jmxProject.spring.services;


import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Getter
public class MusicService {
    private final Map<String, String> descriptionByMusic = new HashMap<>();

    public void setMusic(String name, String description) {
        descriptionByMusic.put(name, description);
    }

    public void removeMusic(String music) {
        descriptionByMusic.remove(music);
    }

    public String getDescription(String music) {
        return descriptionByMusic.get(music);
    }

}
