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
	
	//header �Խ���(json) ȣ��Ǵ� �ּ�
	@RequestMapping(value = "/board_list_json.do", method = RequestMethod.GET)
	public String board_list_json() {
		return "/board/board_list_json";
	}
	
	/**
	 * board_list_json_date.do - ajax���� ȣ��Ǵ� �Խñ� ��ü ����Ʈ(JSON)
	 * �����͸� ���� �� String ���·� ������.
	 */
	@RequestMapping(value = "/board_list_json_data.do", method = RequestMethod.GET, 
					produces="text/plain;charset=UTF-8")//�ѱ� �� ������ �ϴ� �ڵ�
	@ResponseBody
	public String board_list_json_data(String page) {

		// ����¡ ó�� - startCount, endCount ���ϱ�
		int startCount = 0;
		int endCount = 0;
		int pageSize = 5; // ���������� �Խù� �� : �� ������ �� ������ ���ڸ�ŭ�� �����ش�. 
		int reqPage = 1; // ��û������
		int pageCount = 1; // ��ü ������ ��
		int dbCount = boardService.getTotalRowCount(); // DB���� ������ ��ü ���

		// �� ������ �� ���
		if (dbCount % pageSize == 0) {
			pageCount = dbCount / pageSize;
		} else {
			pageCount = dbCount / pageSize + 1;
		}

		// ��û ������ ���
		if (page != null) {
			reqPage = Integer.parseInt(page);
			startCount = (reqPage - 1) * pageSize + 1;
			endCount = reqPage * pageSize;
		} else {
			startCount = 1;
			endCount = pageSize;
		}

		ArrayList<BoardVo> list = boardService.getSelet(startCount, endCount);
		
		//list ��ü�� �����͸� JSON ���·� ����
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
	 * board_list.do - �Խñ� ��ü ����Ʈ
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
	 * board_delete_proc.do - �Խñ� ���� ó��
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
	 * board_delete.do - �Խñ� ������
	 */
	@RequestMapping(value = "/board_delete.do", method = RequestMethod.GET)
	public ModelAndView board_delete(String bid) {
		ModelAndView model = new ModelAndView();
		model.addObject("bid", bid);
		model.setViewName("/board/board_delete");

		return model;
	}

	/**
	 * board_update_proc.do - �Խñ� ���� ó��
	 */
	@RequestMapping(value = "/board_update_proc.do", method = RequestMethod.POST)
	public String board_update_proc(BoardVo boardVo) {
		String viewName = "";
		int result = boardService.getBoardUpdate(boardVo);
		if (result == 1) {
			viewName = "redirect:/board_list.do";
		} else {
			// ���� ������ ȣ��
		}

		return viewName;
	}

	/**
	 * board_update.do - �Խñ� ���� ��
	 */
	@RequestMapping(value = "/board_update.do", method = RequestMethod.GET)
	public ModelAndView board_update(String bid) {
		// �������� �󼼺��� ������ �����ͼ� ���� �߰��Ͽ� ���
		ModelAndView model = new ModelAndView();
		BoardVo boardVo = boardService.getBoardContent(bid);

		model.addObject("boardVo", boardVo);
		model.setViewName("/board/board_update");

		return model;
	}

	/**
	 * board_write_proc.do - �Խñ� �۾��� ó��
	 */
	@RequestMapping(value = "/board_write_proc.do", method = RequestMethod.POST)
	public String board_write_proc(BoardVo boardVo, HttpServletRequest request) throws Exception{
		String viewName = "";
		//������ ���� ��ġ
		String root_path = request.getSession().getServletContext().getRealPath("/");
		String attach_path = "\\resources\\upload\\"; // webapp���� ��
		
		//bfile, bsfile ���ϸ� ���� - ������ ���� ��� nullpointexception�� �߻���(if�� ����)
		if(boardVo.getFile1().getOriginalFilename() != null 
				&& !boardVo.getFile1().getOriginalFilename().equals("") ) { //������ �����ϸ�
			
			//BSFILE ���� �ߺ� ó�� 
			UUID uuid = UUID.randomUUID();
			String bfile = boardVo.getFile1().getOriginalFilename();
			String bsfile = uuid + "_" + bfile;
			
			//������ �Ѿ�� Ȯ�ο�
			System.out.println(root_path + attach_path);// ���� ��� Ȯ��
			System.out.println("bfile-->" + bfile);
			System.out.println("bsfile-->" + bsfile);
			
			boardVo.setBfile(bfile);
			boardVo.setBsfile(bsfile);
		}else {
			System.out.println("���� ����");
		}
		
		int result = boardService.getBoardWrite(boardVo);
		if (result == 1) {
			
			//������ �����ϸ� ������ ����
			File saveFile = new File(root_path+ attach_path + boardVo.getBsfile()); //������ ����Ǵ� bsfile�� �����´�. ��� ����
			boardVo.getFile1().transferTo(saveFile); // ������ ��ο� ���� ����
			
			viewName = "redirect:/board_list.do";
		} else {
			// ���� ������ ȣ��
		}
		return viewName;
	}

	/**
	 * board_write.do - �Խñ� �۾���
	 */
	@RequestMapping(value = "/board_write.do", method = RequestMethod.GET)
	public String board_write() {
		return "/board/board_write";
	}

	/**
	 * board_content.do - �Խñ� �󼼺���
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
