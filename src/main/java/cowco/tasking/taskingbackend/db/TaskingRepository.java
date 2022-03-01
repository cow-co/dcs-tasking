package cowco.tasking.taskingbackend.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskingRepository extends CrudRepository<TaskingEntity, Long> {

}
