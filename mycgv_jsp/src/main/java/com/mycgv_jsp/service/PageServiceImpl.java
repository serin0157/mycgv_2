package com.mycgv_jsp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycgv_jsp.dao.PageDao;

@Service("pageService")
public class PageServiceImpl {
	
	@Autowired
	private PageDao pageDao;
	
	//부모인 Map을 사용하기 때문에 크기가 줄어들어 있어 확장해서 사용해야 한다.
	public Map<String, Integer> getPageResult(String page, String serviceName) {
		Map<String, Integer> param = new HashMap<String, Integer>();
		
		// 페이징 처리 - startCount, endCount 구하기
		int startCount = 0;
		int endCount = 0;
		int pageSize = 5;	// 한페이지당 게시물 수 : 한 페이지 당 지정한 숫자만큼만 보여준다. 
		int reqPage = 1; 	// 요청페이지
		int pageCount = 1;  // 전체 페이지 수
		int dbCount = 0; 	// DB에서 가져온 전체 행수

		dbCount = pageDao.totalRowCount(serviceName);
		
		if(serviceName.equals("notice")) {
			//매개변수 serviceType을 noticeService로 변환
			//noticeService = (NoticeService)serviceType;
//			dbCount = noticeService.getTotalRowCount();
//			dbCount = pageDao.totalRowCount(serviceName);
		}else if(serviceName.equals("board")) {
//			dbCount = pageDao.totalRowCount(serviceName);
			pageSize = 7;
		}else if(serviceName.equals("member")) {
//			dbCount = pageDao.totalRowCount(serviceName);
		}
		
		// 총 페이지 수 계산
		if (dbCount % pageSize == 0) {
			pageCount = dbCount / pageSize;
		} else {
			pageCount = dbCount / pageSize + 1;
		}

		// 요청 페이지 계산
		if (page != null) {
			reqPage = Integer.parseInt(page);
			startCount = (reqPage - 1) * pageSize + 1;
			endCount = reqPage * pageSize;
		} else {
			startCount = 1;
			endCount = pageSize;
		}
		//param 객체 데이터 put(넣을 때)     /꺼낼 때는 get
		param.put("startCount", startCount);
		param.put("endCount", endCount);
		param.put("dbCount", dbCount);
		param.put("pageSize", pageSize);
		param.put("maxSize", pageCount);
		param.put("page", reqPage);
		
		return param;
	}
}
