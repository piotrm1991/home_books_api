package home_books_api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Status")
@Table(name = "STATUS")
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_status_type")
    private StatusType statusType;

    @Column(name = "date_up")
    private Date dateUp;

    @Column(name = "comment")
    private String comment;

    public Status(Date dateUp, String comment) {
        this.dateUp = dateUp;
        this.comment = comment;
    }
}