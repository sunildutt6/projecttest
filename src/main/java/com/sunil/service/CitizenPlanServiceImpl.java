package com.sunil.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sunil.binding.SearchCriteria;
import com.sunil.entities.CitizenPlan;
import com.sunil.repo.CitizenPlanRepo;
import com.sunil.utils.EmailUtils;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CitizenPlanServiceImpl implements CitizenPlanService {

	@Autowired
	private CitizenPlanRepo repo;

	@Autowired
	private EmailUtils emailUtils;
	
	@Override
	public List<String> getPlanNames() {
		return repo.getPlanNames();

	}

	@Override
	public List<String> getPlanStatus() {
		return repo.getPlanStatus();
	}

	@Override
	public List<CitizenPlan> searchCitizens(SearchCriteria criteria) {
		CitizenPlan entity = new CitizenPlan();
		if (StringUtils.isNotBlank(criteria.getPlanName())) {
			entity.setPlanName(criteria.getPlanName());
		}
		if (StringUtils.isNotBlank(criteria.getPlanStatus())) {
			entity.setPlanStatus(criteria.getPlanStatus());
		}
		if (StringUtils.isNotBlank(criteria.getGender())) {
			entity.setGender(criteria.getGender());
		}
		if (null != criteria.getPlanStartDate()) {
			entity.setPlanStartDate(criteria.getPlanStartDate());
		}
		if (null != criteria.getPlanEndDate()) {
			entity.setPlanEndDate(criteria.getPlanEndDate());
		}

		Example<CitizenPlan> of = Example.of(entity);
		return repo.findAll(of);
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {
		List<CitizenPlan> records = repo.findAll();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Data");
		XSSFRow headerRow = sheet.createRow(0);

		headerRow.createCell(0).setCellValue("Name");
		headerRow.createCell(1).setCellValue("Email");
		headerRow.createCell(2).setCellValue("Gender");
		headerRow.createCell(3).setCellValue("Phone");
		headerRow.createCell(4).setCellValue("Plan Name");
		headerRow.createCell(5).setCellValue("Plan Status");
		headerRow.createCell(6).setCellValue("SSN");

		int rowIndex = 1;
		for (CitizenPlan record : records) {
			XSSFRow dataRow = sheet.createRow(rowIndex);
			dataRow.createCell(0).setCellValue(record.getName());
			dataRow.createCell(1).setCellValue(record.getEmail());
			dataRow.createCell(2).setCellValue(record.getGender());
			dataRow.createCell(3).setCellValue(record.getPhno());
			dataRow.createCell(4).setCellValue(record.getPlanName());
			dataRow.createCell(5).setCellValue(record.getPlanStatus());
			dataRow.createCell(6).setCellValue(record.getSsn());

			rowIndex++;
		}
		
		File file = new File("data.xls");
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		emailUtils.sendEmail(file);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		List<CitizenPlan> records = repo.findAll();

		//tthis for browser
		Document pdfDoc1 = new Document(PageSize.A4);
		ServletOutputStream outputStream = response.getOutputStream();
		PdfWriter.getInstance(pdfDoc1, outputStream);
		pdfDoc1.open();
		
		//email attachment
		Document pdfDoc2 = new Document(PageSize.A4);
		File f = new File("data.pdf");
		FileOutputStream fos = new FileOutputStream(f);
		PdfWriter.getInstance(pdfDoc2, fos);
		pdfDoc2.open();

		Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		fontTiltle.setSize(20);

		Paragraph p = new Paragraph("Citizen plans info", fontTiltle);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		pdfDoc1.add(p);
		pdfDoc2.add(p);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setWidths(new int[] { 3, 3, 3, 3, 3, 3 });
		table.setSpacingBefore(5);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(CMYKColor.gray);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
		font.setColor(CMYKColor.WHITE);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Gender", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("SSN", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Plan Status", font));
		table.addCell(cell);

		for (CitizenPlan record : records) {
			table.addCell(record.getName());
			table.addCell(record.getEmail());
			table.addCell(record.getGender());
			table.addCell(String.valueOf(record.getSsn()));
			table.addCell(record.getPlanName());
			table.addCell(record.getPlanStatus());
		}

		pdfDoc1.add(table);
		pdfDoc2.add(table);
		
		pdfDoc1.close();
		outputStream.close();
		
		pdfDoc2.close();
		fos.close();
		
		emailUtils.sendEmail(f);

	}

	

	

}
