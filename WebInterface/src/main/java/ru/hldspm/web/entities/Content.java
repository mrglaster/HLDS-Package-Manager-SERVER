package ru.hldspm.web.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "content_type")
    private ContentType contentType;

    @ManyToOne
    @JoinColumn(name = "platform")
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "game")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt;

    @Column(name="is_active")
    private boolean isActive = true;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Content content = (Content) o;
        return getId() != null && Objects.equals(getId(), content.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}