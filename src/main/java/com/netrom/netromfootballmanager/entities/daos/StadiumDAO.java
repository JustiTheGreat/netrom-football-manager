package com.netrom.netromfootballmanager.entities.daos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
public class StadiumDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "stadium", fetch = FetchType.LAZY)
    private List<GameDAO> games;

    public List<Long> getGamesIds() {
        if (games == null) return null;
        return games.stream().map(GameDAO::getId).collect(Collectors.toList());
    }
}
