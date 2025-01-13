package com.byaffe.learningking.services;

import com.byaffe.learningking.dtos.DashboardDto;
import com.byaffe.learningking.models.payments.Currency;
import com.byaffe.learningking.shared.models.Country;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * @author Mzee Sr.
 *
 */
public interface DashboardService  {


	List<Long> findWeeklySignupCounts();
	DashboardDto getSummaries();

	DashboardDto getSignupOverview();

	DashboardDto getRecentUsers();

	DashboardDto getRecentReviews();
	DashboardDto getMonthlyRevenueSummary();
}
