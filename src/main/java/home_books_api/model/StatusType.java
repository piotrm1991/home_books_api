package home_books_api.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "StatusType")
@Table(name = "STATUS_TYPE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_type")
    private Integer id;

    @Column(name = "name")
    private String name;

    public StatusType(String name) {
        this.name = name;
    }
}