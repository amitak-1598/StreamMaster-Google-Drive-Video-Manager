package com.videostreaming.VideoStreamingApplication.Entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Video {
    @Id
    @GeneratedValue
    private UUID id;
    private String videoTitle;
    private String videoDesc;
    private String videoPath;

    @ManyToOne
    private Course course;


    
}
