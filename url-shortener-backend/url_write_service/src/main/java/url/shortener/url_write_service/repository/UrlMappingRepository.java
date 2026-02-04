package url.shortener.url_write_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import url.shortener.url_write_service.entity.UrlMapping;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

}
