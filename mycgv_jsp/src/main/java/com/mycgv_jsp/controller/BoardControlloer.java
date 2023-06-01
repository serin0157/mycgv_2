package com.mycgv_jsp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycgv_jsp.service.BoardService;
import com.mycgv_jsp.service.PageServiceImpl;
import com.mycgv_jsp.vo.BoardVo;

@Controller
public class BoardControlloer {
	
	
	@Autowired
	private BoardService boardService;
	@Autowired
	private PageServiceImpl pageService;
	
	//header 게시판(json) 호출되는 주소
	@RequestMapping(value = "/board_list_json.do", method = RequestMethod.GET)
	public String board_list_json() {
		return "/board/board_list_json";
	}
	
	/**
	 * board_list_json_date.do - ajax에서 호출되는 게시글 전체 리스트(JSON)
	 * 데이터를 보낼 시 String 형태로 보낸다.
	 */
	@RequestMapping(value = "/board_list_json_data.do", method = RequestMethod.GET, 
					produces="text/plain;charset=UTF-8")//한글 안 깨지게 하는 코드
	@ResponseBody
	public String board_list_json_data(String page) {

		// 페이징 처리 - startCount, endCount 구하기
		int startCount = 0;
		int endCount = 0;
		int pageSize = 5; // 한페이지당 게시물 수 : 한 페이지 당 지정한 숫자만큼만 보여준다. 
		int reqPage = 1; // 요청페이지
		int pageCount = 1; // 전체 페이지 수
		int dbCount = boardService.getTotalRowCount(); // DB에서 가져온 전체 행수

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

		ArrayList<BoardVo> list = boardService.getSelet(startCount, endCount);
		
		//list 객체의 데이터를 JSON 형태로 생성
		JsonObject jlist = new JsonObject();
		JsonArray jarray = new JsonArray();
		
		for(BoardVo boardVo : list) {
			JsonObject jobj = new JsonObject(); //{}
			jobj.addProperty("rno", boardVo.getRno()); //{rno:1}
			jobj.addProperty("btitle", boardVo.getBtitle()); //{rno:1, btitle:"~"}
			jobj.addProperty("bhits", boardVo.getBhits());
			jobj.addProperty("id", boardVo.getId());
			jobj.addProperty("bdate", boardVo.getBdate());
			
			jarray.add(jobj);
		}
		
		jlist.add("jlist", jarray);
		jlist.addProperty("totals", dbCount);
		jlist.addProperty("pageSize", pageSize);
		jlist.addProperty("maxSize", pageCount);
		jlist.addProperty("page", reqPage);
		 
		return new Gson().toJson(jlist);
	}
	
	/**
	 * board_list.do - 게시글 전체 리스트
	 */
	@RequestMapping(value = "/board_list.do", method = RequestMethod.GET)
	public ModelAndView board_list(String page) {
		ModelAndView model = new ModelAndView();
		
		Map<String, Integer> param = pageService.getPageResult(page, "board");
		ArrayList<BoardVo> list = boardService.getSelet(param.get("startCount"), param.get("endCount"));

		model.addObject("list", list);
		model.addObject("totals", param.get("dbCount"));
		model.addObject("pageSize", param.get("pageSize"));
		model.addObject("maxSize", param.get("maxSize"));
		model.addObject("page", param.get("reqPage"));

		model.setViewName("/board/board_list");

		return model;
	}

	/**
	 * board_delete_proc.do - 게시글 삭제 처리
	 */
	@RequestMapping(value = "/board_delete_proc.do", method = RequestMethod.POST)
	public String board_delete_proc(String bid) {
		String viewName = "";
		int result = boardService.getBoardDelete(bid);
		if (result == 1) {
			viewName = "redirect:/board_list.do";
		} else {

		}
		return viewName;
	}

	/**
	 * board_delete.do - 게시글 삭제폼
	 */
	@RequestMapping(value = "/board_delete.do", method = RequestMethod.GET)
	public ModelAndView board_delete(String bid) {
		ModelAndView model = new ModelAndView();
		model.addObject("bid", bid);
		model.setViewName("/board/board_delete");

		return model;
	}

	/**
	 * board_update_proc.do - 게시글 수정 처리
	 */
	@RequestMapping(value = "/board_update_proc.do", method = RequestMethod.POST)
	public String board_update_proc(BoardVo boardVo) {
		String viewName = "";
		int result = boardService.getBoardUpdate(boardVo);
		if (result == 1) {
			viewName = "redirect:/board_list.do";
		} else {
			// 에러 페이지 호출
		}

		return viewName;
	}

	/**
	 * board_update.do - 게시글 수정 폼
	 */
	@RequestMapping(value = "/board_update.do", method = RequestMethod.GET)
	public ModelAndView board_update(String bid) {
		// 수정폼은 상세보기 내용을 가져와서 폼에 추가하여 출력
		ModelAndView model = new ModelAndView();
		BoardVo boardVo = boardService.getBoardContent(bid);

		model.addObject("boardVo", boardVo);
		model.setViewName("/board/board_update");

		return model;
	}

	/**
	 * board_write_proc.do - 게시글 글쓰기 처리
	 */
	@RequestMapping(value = "/board_write_proc.do", method = RequestMethod.POST)
	public String board_write_proc(BoardVo boardVo, HttpServletRequest request) throws Exception{
		String viewName = "";
		//파일의 저장 위치
		String root_path = request.getSession().getServletContext().getRealPath("/");
		String attach_path = "\\resources\\upload\\"; // webapp까지 옴
		
		//bfile, bsfile 파일명 생성 - 파일이 없을 경우 nullpointexception이 발생함(if문 생성)
		if(boardVo.getFile1().getOriginalFilename() != null 
				&& !boardVo.getFile1().getOriginalFilename().equals("") ) { //파일이 존재하면
			
			//BSFILE 파일 중복 처리 
			UUID uuid = UUID.randomUUID();
			String bfile = boardVo.getFile1().getOriginalFilename();
			String bsfile = uuid + "_" + bfile;
			
			//파일이 넘어가나 확인용
			System.out.println(root_path + attach_path);// 파일 경로 확인
			System.out.println("bfile-->" + bfile);
			System.out.println("bsfile-->" + bsfile);
			
			boardVo.setBfile(bfile);
			boardVo.setBsfile(bsfile);
		}else {
			System.out.println("파일 없어");
		}
		
		int result = boardService.getBoardWrite(boardVo);
		if (result == 1) {
			
			//파일이 존재하면 서버에 저장
			File saveFile = new File(root_path+ attach_path + boardVo.getBsfile()); //서버에 저장되는 bsfile을 가져온다. 경로 생성
			boardVo.getFile1().transferTo(saveFile); // 생성된 경로에 파일 저장
			
			viewName = "redirect:/board_list.do";
		} else {
			// 에러 페이지 호출
		}
		return viewName;
	}

	/**
	 * board_write.do - 게시글 글쓰기
	 */
	@RequestMapping(value = "/board_write.do", method = RequestMethod.GET)
	public String board_write() {
		return "/board/board_write";
	}

	/**
	 * board_content.do - 게시글 상세보기
	 */
	@RequestMapping(value = "/board_content.do", method = RequestMethod.GET)
	public ModelAndView board_content(String bid) {
		ModelAndView model = new ModelAndView();
		BoardVo boardVo = boardService.getBoardContent(bid);
		if (boardVo != null) {
			boardService.getUpdateHits(bid);
		}

		model.addObject("bvo", boardVo);
		model.setViewName("/board/board_content");

		return model;
	}
}

