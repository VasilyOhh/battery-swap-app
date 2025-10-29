package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.PackagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PackagePlanRepository extends JpaRepository<PackagePlan, Integer> {
    Optional<PackagePlan> findByPlanName(String packageName);
}
