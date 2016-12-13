package com.brainmatics.training.bpjs.android.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;

@Entity @Table(name = "tagihan")
public class Tagihan {
    @Id @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "id_peserta")
    @NotNull
    private Peserta peserta;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    @Column(name = "tanggal_tagihan")
    private LocalDate tanggalTagihan;
    
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull
    @Column(name = "tanggal_jatuh_tempo")
    private LocalDate tanggalJatuhTempo;
    
    @NotNull @Min(0)
    private BigDecimal nilai;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Peserta getPeserta() {
        return peserta;
    }

    public void setPeserta(Peserta peserta) {
        this.peserta = peserta;
    }

    public LocalDate getTanggalTagihan() {
        return tanggalTagihan;
    }

    public void setTanggalTagihan(LocalDate tanggalTagihan) {
        this.tanggalTagihan = tanggalTagihan;
    }

    public LocalDate getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setTanggalJatuhTempo(LocalDate tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public BigDecimal getNilai() {
        return nilai;
    }

    public void setNilai(BigDecimal nilai) {
        this.nilai = nilai;
    }
    
    
}
