package home_books_api.service;

import home_books_api.model.StatusType;
import home_books_api.repository.StatusTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusTypeService {

    private StatusTypeRepository statusTypeRepository;

    public StatusTypeService(StatusTypeRepository statusTypeRepository) {
        this.statusTypeRepository = statusTypeRepository;
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
