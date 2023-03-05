package com.cchojnowski.pocmultilanguage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "translation")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isoName;

    private String isoCode;

    @OneToMany(mappedBy = "language")
    private List<TextContent> textContents;

    @OneToMany(mappedBy = "language")
    private List<Translation> translations;

}
