package com.TravelSchedule.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;

import com.TravelSchedule.dto.Country;
import com.TravelSchedule.dto.Festival;
import com.TravelSchedule.dto.Tdest;

public interface ApiDao {

	String selectCtcode(@Param("ctname")String addr1);

	String maxcode(@Param("seloption")String seloption);

	void insertTdest(Tdest tdest);

	String selectTdcode(@Param("tdname")String tdname);

	ArrayList<Tdest> selectTdest();

	String selectFecode(@Param("fename")String fename);

	void insertFestival(Festival festival);

	ArrayList<Country> selectCountry();

	ArrayList<Festival> selectFestival_country(@Param("ctcode")String ctcode);

	ArrayList<Festival> selectFestival();

	Festival selectFestival_detail(@Param("code")String code);
}
