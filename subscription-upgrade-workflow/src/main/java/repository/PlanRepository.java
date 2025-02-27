package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
