package mx.edu.utez.sigev.repository;

import mx.edu.utez.sigev.entity.RequestAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRequestAttachmentsRepository extends JpaRepository<RequestAttachment, Long> {
    public RequestAttachment findById(long id);
    @Query(value = "SELECT * FROM request_attachments ra WHERE ra.request = :id", nativeQuery = true)
    public RequestAttachment findAttachmentsByRequestId(@Param("id") long id);
}