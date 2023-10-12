package com.TravelSchedule.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TravelSchedule.dao.TravelDao;
import com.TravelSchedule.dto.Calendar;
import com.TravelSchedule.dto.Schedule;
import com.TravelSchedule.dto.Tdest;

@Service
public class TravelService {

	@Autowired
	TravelDao tdao;
	
	public ArrayList<Calendar> getCalendar(String mid) {
		System.out.println("TravelService - getCalender()");
		return tdao.selectCalendar(mid);
	}

	public int registCalendar(Calendar cd) {
		System.out.println("TravelService - registCalendar()");
		String cdcode = tdao.maxcode(cd.getMid());
		if(cdcode.equals("CD00000")) {
		cdcode = "CD00001";	
		}else {
			
		String codeName = cdcode.substring(0,2);
		int codeNum = Integer.parseInt(cdcode.substring(2))+1;
		String codeNum_String = String.format("%05d", codeNum);
		cdcode = codeName + codeNum_String;
		}
		System.out.println(cdcode);
		cd.setCdcode(cdcode);
		
		return tdao.insertCalendar(cd);
	}

	public ArrayList<Calendar> getCdcode(String mid) {
		System.out.println("travelService - getCdcode()");
		return tdao.selectCdcode(mid);
	}

	public int registSelectDest(Schedule sc) {
		System.out.println("travelService - registSelectDest()");
		
		return tdao.insertFKcode(sc);
	}

	public ArrayList<Schedule> getScList(String mid, String cdcode) {
		System.out.println("travelService - getSchedule()");
		return tdao.selectSchedule(mid, cdcode);
	}

	public Tdest getTdest(String tdcode) {
		System.out.println("travelService - getTdest()");
		return tdao.selectTdest(tdcode);
	}
	
	public ArrayList<Tdest> TdestSearch() {
		System.out.println("SERVICE - 여행지 검색 페이지 이동 서비스");
		
		ArrayList<Tdest> TdestService = tdao.TdestService();
		return TdestService;
	}

	public ArrayList<Tdest> CtTdestList(String ctcode) {
		System.out.println("SERVICE - 도시코드 기반 여행지 검색 서비스");
		
		ArrayList<Tdest> CtTdestService = tdao.CtTdestService(ctcode);
		return CtTdestService;
	}

	public ArrayList<Tdest> SearchTdestList(String searchVal) {
		System.out.println("SERVICE - 검색 결과 요청");
		
		ArrayList<Tdest> SearchTdest = tdao.SearchTdest(searchVal);
		return SearchTdest;
	}

}

