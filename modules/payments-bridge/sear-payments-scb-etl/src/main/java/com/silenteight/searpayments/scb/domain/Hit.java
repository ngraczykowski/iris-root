package com.silenteight.searpayments.scb.domain;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.NONE)
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "hit")
@Builder
public class Hit {

    //  private static final Parser PROTO_JSON_PARSER = JsonFormat.parser().ignoringUnknownFields();
    //  private static final Printer PROTO_JSON_PRINTER =
    //      JsonFormat.printer().includingDefaultValueFields().omittingInsignificantWhitespace();

    @Id
    @Column(name = "hitId", insertable = false, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alertId", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Alert alert;

    @NonNull
    private String entityText;

    @NonNull
    private String tag;

    @NonNull
    private String solutionType;

    @NonNull
    private String matchingText;

    @NonNull
    private String watchlistType;

    @NonNull
    private String watchlistName;

    @NonNull
    private String accountNumber;

    @NonNull
    private String watchlistOfacId;

    @NonNull
    private Integer watchlistInterventionId;

    @NonNull
    private String origin;

    @NonNull
    private String designation;

    @NonNull
    private String messageFieldStructure;

    @Builder.Default
    @OneToMany(mappedBy = "hit", cascade = ALL, orphanRemoval = true, fetch = EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @ToString.Exclude
    private Collection<HittedEntityAddress> addresses = new ArrayList<>();

    @Builder.Default
    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "hit_name", joinColumns = @JoinColumn(name = "hit_id"))
    @Column(name = "name")
    private Collection<String> names = new ArrayList<>();

    private String freeTextAgentRequest;

    private String crossmatchAgentRequest;

    private String nameAgentRequest;

    private String geoAgentRequest;

    private String oneLineAddressAgentRequest;

    private String specificTermsAgentRequest;

    private String delimiterInNameLineAgentRequest;

    private String matchtextFirstTokenOfAddressAgentRequest;

    @Setter
    private String twoLinesNameAgentRequest;

    public void addAddress(HittedEntityAddress hitAddress) {
        hitAddress.setHit(this);
        addresses.add(hitAddress);
    }

    public void addName(String name) {
        names.add(name);
    }

}
