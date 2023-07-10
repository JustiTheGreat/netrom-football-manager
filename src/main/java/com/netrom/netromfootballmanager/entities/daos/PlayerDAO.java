package com.netrom.netromfootballmanager.entities.daos;

import com.netrom.netromfootballmanager.entities.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer goalsScored;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamDAO team;

    public Long getTeamId() {
        if (team == null) return null;
        return team.getId();
    }
}
