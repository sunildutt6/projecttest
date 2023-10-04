package com.sunil.service;

import java.util.List;

import com.sunil.binding.SearchCriteria;
import com.sunil.entities.CitizenPlan;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

public interface CitizenPlanService {

	public List<String> getPlanNames();

	public List<String> getPlanStatus();

	public List<CitizenPlan> searchCitizens(SearchCriteria criteria);

	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws Exception;



	

}
