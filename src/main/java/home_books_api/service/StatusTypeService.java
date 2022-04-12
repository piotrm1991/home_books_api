package home_books_api.service;

import home_books_api.model.StatusType;
import home_books_api.modelDTO.StatusTypeDTO;
import home_books_api.repository.BookRepository;
import home_books_api.repository.StatusTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusTypeService {

    private StatusTypeRepository statusTypeRepository;
    private BookRepository bookRepository;

    public StatusTypeService(StatusTypeRepository statusTypeRepository, BookRepository bookRepository) {
        this.statusTypeRepository = statusTypeRepository;
        this.bookRepository = bookRepository;
    }

    public List<StatusTypeDTO> getStatusTypesForAngular() {
        List<StatusType> statusTypes  =  this.statusTypeRepository.findAll();
        List<StatusTypeDTO> statusTypesDTO = new ArrayList<>();
        statusTypes.stream().forEach(statusType -> {
            statusTypesDTO.add(StatusTypeDTO.builder()
                    .id(statusType.getId())
                    .name(statusType.getName())
                    .noBooks((int)this.bookRepository.findByStatusStatusTypeId(statusType.getId()).stream().count())
                    .build());
        });
        return statusTypesDTO;
    }

    public void updateStatusType(Integer id, StatusType newPartialStatusType) {
        this.statusTypeRepository.findById(id).ifPresent(statusType -> {
            if (newPartialStatusType.getName() != null) {
                statusType.setName(
                        newPartialStatusType.getName());
            }
            this.statusTypeRepository.save(statusType);
        });
    }
}
