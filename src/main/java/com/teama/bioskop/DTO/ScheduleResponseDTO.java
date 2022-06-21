package com.teama.bioskop.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teama.bioskop.Models.Films;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScheduleResponseDTO {
    private Films films;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggalTayang;

    @JsonFormat(pattern = "HH:mm:ss")
    private Time jamMulai;

    @JsonFormat(pattern = "HH:mm:ss")
    private Time jamSelesai;


    private  Double hargaTiket;
}