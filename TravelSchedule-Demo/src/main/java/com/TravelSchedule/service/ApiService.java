package com.TravelSchedule.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TravelSchedule.dao.ApiDao;
import com.TravelSchedule.dto.Country;
import com.TravelSchedule.dto.Festival;
import com.TravelSchedule.dto.Tdest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class ApiService {

	private final String Servicekey = "S7zgFEQqSlrWVhHdRtINMDDNv%2BTnaJrW2iEOUsm2Y5UdcfYh6inhqrsA1Qls%2BtLEub4iFJ4UT89YTfsFhZ0sZQ%3D%3D";
	@Autowired
	ApiDao apiDao;
	public ArrayList<Festival> getFestival() throws Exception  {
		
		
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService1/searchFestival1"); /*URL*/
		urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + Servicekey); /*Service Key*/
		urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("423", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8")); /*	OS 구분 : IOS (아이폰), AND (안드로이드), WIN (윈도우폰), ETC(기타)*/
        urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("AppTest", "UTF-8")); /*서비스명(어플명)*/
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)*/
        urlBuilder.append("&" + URLEncoder.encode("listYN","UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8")); /*목록구분(Y=목록, N=개수)*/
        urlBuilder.append("&" + URLEncoder.encode("arrange","UTF-8") + "=" + URLEncoder.encode("A", "UTF-8")); /*정렬구분 (A=제목순, C=수정일순, D=생성일순) 대표이미지가반드시있는정렬(O=제목순, Q=수정일순, R=생성일순)*/
        urlBuilder.append("&" + URLEncoder.encode("eventStartDate","UTF-8") + "=" + URLEncoder.encode("20231005", "UTF-8")); /*행사시작일(형식 :YYYYMMDD)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        
        JsonObject Festival_Json = (JsonObject) JsonParser.parseString(sb.toString()); 
        System.out.println("[Festival_Json] "+Festival_Json);
        JsonArray FestivalList = Festival_Json.get("response").getAsJsonObject()
        		.get("body").getAsJsonObject()
        		.get("items").getAsJsonObject()
        		.get("item").getAsJsonArray();
        
        System.out.println("[FestivalList] "+FestivalList);
        
        JsonObject Festival_body = Festival_Json.get("response").getAsJsonObject().get("body").getAsJsonObject();
        
        JsonObject Festival_Items = Festival_body.get("items").getAsJsonObject();
        
        System.out.println("[Festival_body] "+Festival_body);
        System.out.println("[Festival_Items] "+Festival_Items);
        
//        JsonArray itemList = Festival_body.get("item").getAsJsonArray();
//        System.out.println("[itemList] "+itemList);
//        
//        System.out.println("[itemList.size()] "+itemList.size());
        
        ArrayList<Festival> FestList = new ArrayList<Festival>();
        for(int i=0; i<FestivalList.size(); i++) {
        	Festival festival = new Festival();
        	String feaddress = FestivalList.get(i).getAsJsonObject().get("addr1").getAsString();
        	String feaddress2 = FestivalList.get(i).getAsJsonObject().get("addr2").getAsString();
        	festival.setFeaddress(feaddress + " " + feaddress2);
        	
        	String feposter = FestivalList.get(i).getAsJsonObject().get("firstimage").getAsString();
        	festival.setFeposter(feposter);
        	
        	String fename = FestivalList.get(i).getAsJsonObject().get("title").getAsString();
        	festival.setFename(fename);
        	
        	String opendate = FestivalList.get(i).getAsJsonObject().get("eventstartdate").getAsString();
        	festival.setOpendate(opendate);
        	
        	String enddate = FestivalList.get(i).getAsJsonObject().get("eventenddate").getAsString();
        	festival.setEnddate(enddate);
        	
        	String fetel = FestivalList.get(i).getAsJsonObject().get("tel").getAsString();
        	festival.setFetel(fetel);
        	
        	String felati = FestivalList.get(i).getAsJsonObject().get("mapx").getAsString();
        	festival.setFelati(felati);
        	
        	String felongti = FestivalList.get(i).getAsJsonObject().get("mapy").getAsString();
        	festival.setFelongti(felongti);
        	String ctname = feaddress.split(" ")[0];
        	switch(ctname) {
        	case "전북":
        		ctname = "전라북도";
        		break;
        	case "전남":
        		ctname = "전라남도";
        		break;
        	case "경북":
        		ctname = "경상북도";
        		break;
        	case "경남":
        		ctname = "경상남도";
        		break;
        	case "충북":
        		ctname = "충청북도";
        		break;
        	case "충남":
        		ctname = "충청남도";
        		break;
        	case "강원도":
        		ctname = "강원특별자치도";
        		break;
        	case "제주도":
        		ctname = "제주특별자치도";
        		break;
        	}        	
        	
        	String ctcode = apiDao.selectCtcode(ctname);
        	System.out.println(ctcode + " " + feaddress);
        	
        	String fecode = apiDao.maxcode("festival");
			String codeName = fecode.substring(0,2);
			int codeNum = Integer.parseInt(fecode.substring(2))+1;
			String codeNum_String = String.format("%05d", codeNum);
			fecode = codeName + codeNum_String;
			
        	System.out.println(fecode);
        	festival.setCtcode(ctcode);
        	festival.setFecode(fecode);
        	FestList.add(festival);
        	
        	
        	
        	String rs = apiDao.selectFecode(fename);
			if(rs.equals("Y")) {				
				apiDao.insertFestival(festival);				
			}
        }
        System.out.println(FestList);
        
		return FestList;
	}

	public ArrayList<Tdest> getTdestList() throws Exception {
		
		StringBuilder urlBuilder = new StringBuilder(
				"https://apis.data.go.kr/B551011/KorService1/areaBasedList1"); /* URL */
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + Servicekey); /* Service Key */
		urlBuilder.append(
				"&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /* 페이지 번호 */
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
				+ URLEncoder.encode("6000", "UTF-8")); /* 한 페이지 결과 수 */
		urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("ETC", "UTF-8"));
		urlBuilder.append(
				"&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TravelSchedule", "UTF-8"));
		urlBuilder.append(
				"&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /* XML/JSON 여부 */
		urlBuilder.append(
				"&" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /* 지역코드 */
		urlBuilder.append("&" + URLEncoder.encode("contentTypeId", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8"));

		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("Response code: " + conn.getResponseCode());
		BufferedReader rd;
		if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();
		conn.disconnect();
//		System.out.println(sb.toString());

		// json 변환
		JsonObject DestInfo = (JsonObject) JsonParser.parseString(sb.toString());
//		System.out.println("[DestInfo] : " + DestInfo);

		JsonArray infoList = DestInfo.get("response").getAsJsonObject().get("body").getAsJsonObject().get("items")
				.getAsJsonObject().get("item").getAsJsonArray();
		
//		System.out.println("[infoList] : " + infoList);

		JsonObject DestInfo_Body = DestInfo.get("response").getAsJsonObject().get("body").getAsJsonObject();

		JsonObject DestInfo_Items = DestInfo_Body.get("items").getAsJsonObject();

//		System.out.println("[DestInfo_Body] : " + DestInfo_Body);
//		System.out.println("[DestInfo_Itmes] : " + DestInfo_Items);

//	        JsonArray itemList = DestInfo_Items.get("item").getAsJsonArray();

//	        System.out.println("[itemList] : "+itemList);
//	        System.out.println("[tiemList.size] : "+itemList.size());

		ArrayList<Tdest> TdestList = new ArrayList<Tdest>();
		for (int i = 0; i < infoList.size(); i++) {
			Tdest tdest = new Tdest();
			String tdname = infoList.get(i).getAsJsonObject().get("title").getAsString();
			

			String addr1 = infoList.get(i).getAsJsonObject().get("addr1").getAsString();
			String ctcode = apiDao.selectCtcode(addr1.split(" ")[0]);	
			if(ctcode.equals("CT00000")) {
				/*
				ctcode = apiDao.maxcode("country", addr1);
				String codeName = ctcode.substring(0,2);
				int codeNum = Integer.parseInt(ctcode.substring(2))+1;
				System.out.println("codeName : "+codeName + " codeNum : "+codeNum);
				String codeNum_String = String.format("%05d", codeNum);
				ctcode = codeName + codeNum_String;
				System.out.println(ctcode);
				 * */
				continue;
			}
			String tdcode = apiDao.maxcode("tdest");
			String codeName = tdcode.substring(0,2);
			int codeNum = Integer.parseInt(tdcode.substring(2))+1;
			String codeNum_String = String.format("%05d", codeNum);
			tdcode = codeName + codeNum_String;
			
			tdest.setTdcode(tdcode);
			tdest.setTdname(tdname);
			tdest.setCtcode(ctcode);
			String addr2 = infoList.get(i).getAsJsonObject().get("addr2").getAsString();
			String tdaddress = addr1 + addr2;
			tdest.setTdaddress(tdaddress);
			String tdlati = infoList.get(i).getAsJsonObject().get("mapx").getAsString();
			tdest.setTdlati(tdlati);
			String tdlongti = infoList.get(i).getAsJsonObject().get("mapy").getAsString();
			tdest.setTdlongti(tdlongti);
			String tdphoto = infoList.get(i).getAsJsonObject().get("firstimage").getAsString();
			tdest.setTdphoto(tdphoto);
			String rs = apiDao.selectTdcode(tdname);
			if(rs.equals("Y")) {
				if(!tdphoto.equals("")) {
					apiDao.insertTdest(tdest);
				}
			}
			TdestList.add(tdest);
			System.out.println(tdest);
		}
	//	System.out.println(TdestList);
		return TdestList;
	}

	public ArrayList<Tdest> getTdList() {
		System.out.println("ApiService - getTdList()");
		return apiDao.selectTdest();
	}

	public ArrayList<Country> getCountry() {
		System.out.println("ApiService - getCountry()");
		return apiDao.selectCountry();
	}

	public ArrayList<Festival> festival_country(String ctcode) {
		System.out.println("ApiService - festival_country()");
		return apiDao.selectFestival_country(ctcode);
	}

	public ArrayList<Festival> getFestival_db() {
		System.out.println("ApiService - getFestival_db()");
		return apiDao.selectFestival();
	}

	public Festival detailFestival(String code) {
		System.out.println("ApiService - detailFestival()");
		return apiDao.selectFestival_detail(code);
	}

}
