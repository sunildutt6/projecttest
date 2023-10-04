package com.sunil.runner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sunil.entities.CitizenPlan;
import com.sunil.repo.CitizenPlanRepo;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
	private CitizenPlanRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		repo.deleteAll();

		CitizenPlan p1 = new CitizenPlan("John", "j@h.com", 1233l, "Male", 33232l, "Cash", "Approved", LocalDate.now(),
				LocalDate.now().plusMonths(6));

		CitizenPlan p2 = new CitizenPlan("Smith", "s@k.com", 123343l, "Male", 34332l, "Cash", "Denied", null, null);

		CitizenPlan p3 = new CitizenPlan("Cathy", "Cathy@h.com", 1233l, "Female", 33232l, "Food", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));

		CitizenPlan p4 = new CitizenPlan("Johny", "j@h.com", 1233l, "Female", 33232l, "Food", "Denied", null, null);

		CitizenPlan p5 = new CitizenPlan("Robert", "robert@h.com", 13233l, "Male", 33232l, "Medical", "Approved",
				LocalDate.now(), LocalDate.now().plusMonths(6));

		CitizenPlan p6 = new CitizenPlan("Anie", "Annie@h.com", 1233l, "Female", 33232l, "Medical", "Denied", null,
				null);

		List<CitizenPlan> records = Arrays.asList(p1, p2, p3, p4, p5, p6);
		repo.saveAll(records);
	}

}
