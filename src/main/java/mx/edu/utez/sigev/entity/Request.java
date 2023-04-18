package mx.edu.utez.sigev.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "request")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category", nullable = false)
    @NotNull(message = "La categoría no puede estar vacía")
    private Category category;

    @Column(name = "description")
    @Size(min = 2, message = "La descripción debe tener mínimo 2 caracteres")
    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @ManyToOne
    @JoinColumn(name = "president")
    private CommitteePresident president;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "payment_status", nullable = false)
    @NotNull(message = "El estatus de pago no puede estar vacío")
    private int paymentStatus;

    @Column(name = "payment_amount", nullable = true)
    private Double paymentAmount;

    @Column(name = "status", nullable = false)
    @NotNull(message = "El estatus no puede estar vacío")
    private int status;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "attachment", joinColumns = @JoinColumn(name = "requests"), inverseJoinColumns = @JoinColumn(name = "attachments"))
    private Set<RequestAttachment> RequestAttachment;

    public Request(int state, int paymentStatus ) {
        this.status = state;
        this.paymentStatus = paymentStatus;
    }
    public Request() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommitteePresident getPresident() {
        return president;
    }

    public void setPresident(CommitteePresident president) {
        this.president = president;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<mx.edu.utez.sigev.entity.RequestAttachment> getRequestAttachment() {
        return RequestAttachment;
    }

    public void setRequestAttachment(Set<mx.edu.utez.sigev.entity.RequestAttachment> requestAttachment) {
        RequestAttachment = requestAttachment;
    }
}
