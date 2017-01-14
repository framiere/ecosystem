package fr.ramiere.spike.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = PROTECTED)
public class Git {
    @URL
    @NotEmpty
    public final String url;
    @NotEmpty
    @Column
    public final String branch;
}
