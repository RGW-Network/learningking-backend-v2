package com.byaffe.learningking.services;

import com.byaffe.learningking.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface DashboardRepository  extends JpaRepository<Student, Long> {

	@Query(value ="SELECT \n" +
			"        DAYOFWEEK(date_created) AS dayOfWeek, \n" +
			"        COUNT(*) AS count \n" +
			"    FROM members  \n" +
			"    WHERE YEARWEEK(date_created, 1) = YEARWEEK(CURRENT_DATE, 1)\n" +
			"    GROUP BY DAYOFWEEK(date_created)\n" +
			"    ORDER BY DAYOFWEEK(date_created)",nativeQuery = true)
	List<Object[]> findWeeklySignupCounts();

}
