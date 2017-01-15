package fr.ramiere.spike.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import static lombok.AccessLevel.PROTECTED;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = PROTECTED)
public class Git {
    @URL
    @NotEmpty
    @Column(nullable = false, length = 255)
    @Size(max = 244)
    public final String url;
    @NotEmpty
    @Column(nullable = false, length = 50)
    @Size(max = 50)
    public final String branch;
}
