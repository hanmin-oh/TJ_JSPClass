package com.tjoeun.myCalendar;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SolaToLunar {
	
//	월별 양력과 음력을 크롤링하고 양력, 음력 공휴일을 계산해서 리턴하는 메소드
	public static ArrayList<LunarDate> solaToLunar(int year , int month) {
		
		ArrayList<LunarDate> lunarList = new ArrayList<>(); // 1~12월의 양력과 대응되는 음력을 기억한다. 
		//인수로 넘겨받은 year에 해당되는 1 ~ 12월의 양력에 대응되는 음력을 크롤링해서 얻어온다.
		for(int i = 1 ; i <= 12 ; i++) {
			String targetSite = String.format("https://astro.kasi.re.kr/life/pageView/5?search_year=%04d&search_month=%02d&search_check=G", year, i);
//			System.out.println(targetSite);
			
//			크롤링할 데이터를 기억할 org.jsoup.nodes 패키지의 Document 클래스 객체를 선언한다.
			Document document = null;
			
//			Jsoup 클래스의
			try {
				document = Jsoup.connect(targetSite).get();
				//System.out.println(document);
				
//				Document 클래스 객체에 저장된 타겟 사이트의 정보 중에서 날짜(<tr>) 단위로 얻어온다.
//				 - Elements 클래스 객체에 Document 클래스 객체로 읽어들인 내용을 select() 메소드를 사용해서 필요한 정보를 얻어온다.
				Elements elements = document.select("tbody > tr");
//				System.out.println(elements);
				
//				Elements 클래스 객체에는 크롤링된 전체 데이터가 저장되어 있으므로 element 객체에 하나씩 저장한다. 
				for(Element element : elements) {
//					System.out.println(element);
//					System.out.println("============================");
//					날짜 단위(<tr>)로 얻어온 정보에서 양력, 음력 단위(<td>)로 얻어온다.
					Elements ele = element.select("td");
					// System.out.println(ele);
//					System.out.println(ele.get(0).text()); //양력만 출력
//					System.out.println(ele.get(1).text()); //음력만 출력
//					System.out.println(ele.get(2).text()); //간지만 출력
					String sola = ele.get(0).text();
					String lunar = ele.get(1).text();
//					System.out.println("양력 : " + sola + "  ---- 음력 : " + lunar);
//					============크롤링 끝
					
//					크롤링한 결과를 LunarDate 클래스 객체에 저장한다.
					LunarDate lunarDate = new LunarDate();
//					split() 메소드는 인수로 지정한 구분자로 문자열을 나눠서 배열로 리턴한다.
//					System.out.println(sola.split(" ")[0]);
					lunarDate.setYear(Integer.parseInt(sola.split(" ")[0].substring(0 , 4)));
					lunarDate.setMonth(Integer.parseInt(sola.split(" ")[1].substring(0 , 2)));
					lunarDate.setDay(Integer.parseInt(sola.split(" ")[2].substring(0 , 2)));
					lunarDate.setYearLunar(Integer.parseInt(lunar.split(" ")[0].substring(0 , 4)));
					try {
						//음력 평달
						lunarDate.setMonthLunar(Integer.parseInt(lunar.split(" ")[1].substring(0 , 2)));
					} catch(NumberFormatException e) {
						//음력 윤달
						lunarDate.setMonthLunar(Integer.parseInt(lunar.split(" ")[1].substring(1 , 3)));
						lunarDate.setLunarFlags(true);
					}
					lunarDate.setDayLunar(Integer.parseInt(lunar.split(" ")[2].substring(0 , 2)));
					//lunarDate.setLunarFlags(ele.get(2).text().split(" ").length <= 2 ? true : false); //날짜에 '윤'을 넣기 위해 -- 위의 catch에서 처리
//					System.out.println(lunarDate);
//					1년치 양력 날짜와 양력 날짜에 대응되는 음력 날짜를 저장한다.
					lunarList.add(lunarDate);
//					System.out.println(lunarList);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// =====================  공휴일 처리
		for(int i = 0 ; i<lunarList.size() ; i++) {
			// 양력 공휴일
			if(lunarList.get(i).getMonth() == 1 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>신정</span>");
			} else if (lunarList.get(i).getMonth() == 3 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>삼일절</span>");
			} else if (lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 1) {
				lunarList.get(i).setHoliday("<br><span>근로자의 날</span>");
			} else if (lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 5) {
				lunarList.get(i).setHoliday("<br><span>어린이날</span>");
			} else if (lunarList.get(i).getMonth() == 6 && lunarList.get(i).getDay() == 6) {
				lunarList.get(i).setHoliday("<br><span>현충일</span>");
			} else if (lunarList.get(i).getMonth() == 8 && lunarList.get(i).getDay() == 15) {
				lunarList.get(i).setHoliday("<br><span>광복절</span>");
			} else if (lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 3) {
				lunarList.get(i).setHoliday("<br><span>개천절</span>");
			} else if (lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 9) {
				lunarList.get(i).setHoliday("<br><span>한글날</span>");
			} else if (lunarList.get(i).getMonth() == 12 && lunarList.get(i).getDay() == 25) {
				lunarList.get(i).setHoliday("<br><span>크리스마스</span>");
			} 
			// 음력 공휴일 => 음력 1월 1일(설날), 음력 4월 8일(석가탄신일) , 음력 8월 15일(추석) => 윤달일경우 공휴일이 아니다.
			if(lunarList.get(i).getMonthLunar() == 1 && lunarList.get(i).getDayLunar() == 1 && !lunarList.get(i).isLunarFlags()) {
				lunarList.get(i-1).setHoliday("<br><span>설날연휴</span>");
				lunarList.get(i).setHoliday("<br><span>설날</span>");
				lunarList.get(i+1).setHoliday("<br><span>설날연휴</span>");
			}
			if(lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8 && !lunarList.get(i).isLunarFlags()) {
				lunarList.get(i).setHoliday("<br><span>석가탄신일</span>");
			}
			if(lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15 && !lunarList.get(i).isLunarFlags()) {
				lunarList.get(i-1).setHoliday("<br><span>추석연휴</span>");
				lunarList.get(i).setHoliday("<br><span>추석</span>");
				lunarList.get(i+1).setHoliday("<br><span>추석연휴</span>");
			}
			// 대체 공휴일 => 설날, 삼일절, 석가탄신일, 어린이날, 광복절, 개천절, 한글날, 크리스마스가 주말(토, 일)이나 다른 공휴일과 겹치면 그 다음 첫번째 비공휴일을 대체 공휴일로 한다.
			// - 양력 날짜의 요일을 계산해둔다.
			int holiday = MyCalendar.weekDay(year, lunarList.get(i).getMonth(), lunarList.get(i).getDay());
			
			// - 설날 대체 공휴일 =
			if(lunarList.get(i).getMonthLunar() == 1 && lunarList.get(i).getDayLunar() == 1 && !lunarList.get(i).isLunarFlags()) {
				if (holiday == 0 || holiday == 6) {
					lunarList.get(i + 2).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// - 삼일절 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 3 && lunarList.get(i).getDayLunar() == 3) {
				if (MyCalendar.weekDay(year, 3, 1) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 3 && lunarList.get(i).getDay() == 3) {
				if (MyCalendar.weekDay(year, 3, 1) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// - 석가탄신일(음력) 대체 공휴일 =
			if(lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8 && !lunarList.get(i).isLunarFlags()) {
				if (MyCalendar.weekDay(year, lunarList.get(i).getMonth() , lunarList.get(i).getDay()) == 0) {
					lunarList.get(i+1).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8 && !lunarList.get(i).isLunarFlags()) {
				if (MyCalendar.weekDay(year, lunarList.get(i).getMonth() , lunarList.get(i).getDay()) == 6) {
					lunarList.get(i+2).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// - 어린이날 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 5 && lunarList.get(i).getDayLunar() == 6) {
				if (MyCalendar.weekDay(year, 5, 5) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 7) {
				if (MyCalendar.weekDay(year, 5, 5) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// 어린이날과 석가탄신일이 평일에 겹쳤을 때 
			 if (lunarList.get(i).getMonth() == 5 && lunarList.get(i).getDay() == 5
					 && lunarList.get(i).getMonthLunar() == 4 && lunarList.get(i).getDayLunar() == 8 && !lunarList.get(i).isLunarFlags()) {
				 lunarList.get(i).setHoliday("<br><span>석가탄신일</span><br><span>어린이날</span>");
				if (holiday == 6) {
					// 어린이날과 석가탄신일이 토요일에 겹쳤을 때
					lunarList.get(i + 2).setHoliday("<br><span>대체공휴일</span>");
				} else if (holiday == 5) {

				} else {
					// 어린이날과 석가탄신일이 토요일을 제외한 나머지 요일에 겹쳤을 때
					lunarList.get(i + 1).setHoliday("<br><span>대체공휴일</span>");
				}
			}

			// - 광복절 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 16) {
				if (MyCalendar.weekDay(year, 8, 15) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 8 && lunarList.get(i).getDay() == 17) {
				if (MyCalendar.weekDay(year, 8, 15) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// -  추석 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15 && !lunarList.get(i).isLunarFlags()) {
				if (holiday == 0 || holiday == 6) {
					lunarList.get(i + 2).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// - 개천절 대체 공휴일 
			if(lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 4 && !lunarList.get(i).isLunarFlags()) {
				if (MyCalendar.weekDay(year, 10, 3) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 5 && !lunarList.get(i).isLunarFlags()) {
				if (MyCalendar.weekDay(year, 10, 3) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			
			// 개천절과 추석이 겹쳤을 떄 
			 if (lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 3
					 && lunarList.get(i).getMonthLunar() == 8 && lunarList.get(i).getDayLunar() == 15 && !lunarList.get(i).isLunarFlags()) {
				 lunarList.get(i).setHoliday("<br><span>추석</span><br><span>개천절</span>");
				if (holiday == 4 || holiday == 5) {
					// 개천절과 추석이 목요일 또는 금요일에 겹쳤을 때 
				} else {
					// 개천절과 추석이 목요일 또는 금요일을 제외한 요일에 겹쳤을 때 
					lunarList.get(i + 2).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			// - 한글날 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 10 && lunarList.get(i).getDayLunar() == 10) {
				if (MyCalendar.weekDay(year, 10, 9) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 10 && lunarList.get(i).getDay() == 11) {
				if (MyCalendar.weekDay(year, 10, 9) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			// - 크리스마스 대체 공휴일 
			if(lunarList.get(i).getMonthLunar() == 12 && lunarList.get(i).getDayLunar() == 26) {
				if (MyCalendar.weekDay(year, 12, 25) == 0) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			}
			if(lunarList.get(i).getMonth() == 12 && lunarList.get(i).getDay() == 27) {
				if (MyCalendar.weekDay(year, 12, 25) == 6) {
					lunarList.get(i).setHoliday("<br><span>대체공휴일</span>");
				}
			} 
		}
		
//		1년에 존재하는 모든 공휴일을 처리했으므로 달력 출력에 사용할 달의 정보만 별도로 리턴시킨다.
		ArrayList<LunarDate> list = new ArrayList<>(); // 인수로 받은 month의 양력에 대응되는 음력을 기억한다.
		for ( int i = 0 ; i < lunarList.size(); i++) {
			if(lunarList.get(i).getMonth() == month) {
				list.add(lunarList.get(i));
			}
		}
//		System.out.println(list);
		
		return list;
	}
}
















 