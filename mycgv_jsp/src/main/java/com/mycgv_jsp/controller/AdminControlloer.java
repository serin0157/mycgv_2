package com.mycgv_jsp.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mycgv_jsp.service.MemberService;
import com.mycgv_jsp.service.NoticeService;
import com.mycgv_jsp.service.PageServiceImpl;
import com.mycgv_jsp.vo.MemberVo;
import com.mycgv_jsp.vo.NoticeVo;

@Controller
public class AdminControlloer {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private PageServiceImpl pageService;
	
	/**
	 * admin_notice_list.do - 관리자 공지사항 게시글 전체 리스트
	 */
	@RequestMapping(value = "/admin_notice_list.do", method = RequestMethod.GET)
	public ModelAndView admin_notice_list(String page) {
		ModelAndView model = new ModelAndView();
		Map<String, Integer> param = pageService.getPageResult(page, "notice");
		ArrayList<NoticeVo> list = noticeService.getSelect(param.get("startCount"), param.get("endCount"));

		model.addObject("list", list);
		model.addObject("totals", param.get("dbCount"));
		model.addObject("pageSize", param.get("pageSize"));
		model.addObject("maxSize", param.get("maxSize"));
		model.addObject("page", param.get("reqPage"));

		model.setViewName("admin/notice/admin_notice_list");

		return model;
	}

	/**
	 * admin_member_list.do - 관리자 회원 전체 리스트
	 */
	@RequestMapping(value = "/admin_member_list.do", method = RequestMethod.GET)
	public ModelAndView admin_member_list(String page) {
		ModelAndView model = new ModelAndView();
		Map<String, Integer> param = pageService.getPageResult(page, "member");
		ArrayList<MemberVo> list = memberService.getList(param.get("startCount"), param.get("endCount"));

		model.addObject("list", list);
		model.addObject("totals", param.get("dbCount"));
		model.addObject("pageSize", param.get("pageSize"));
		model.addObject("maxSize", param.get("maxSize"));
		model.addObject("page", param.get("reqPage"));

		model.setViewName("admin/member/admin_member_list");

		return model;
	}

	/*	*//**
			 * admin_member_list.do - 관리자 회원 메인페이지
			 *//*
				 * 
				 * @RequestMapping(value = "/admin_member_list.do", method = RequestMethod.GET)
				 * public ModelAndView admin_member_list() { ModelAndView model = new
				 * ModelAndView(); MemberDao memberDao = new MemberDao(); ArrayList<MemberVo>
				 * list = memberDao.select();
				 * 
				 * model.addObject("list", list);
				 * model.setViewName("/admin/member/admin_member_list");
				 * 
				 * return model; }
				 */

	/**
	 * admin_notice_delete_proc.do - 공지사항 게시글 삭제 처리
	 */
	@RequestMapping(value = "/admin_notice_delete_proc.do", method = RequestMethod.POST)
	public ModelAndView admin_notice_delete_proc(String nid) {
		ModelAndView model = new ModelAndView(); 
		int result = noticeService.getDelete(nid);
		if (result == 1) {
			model.setViewName("redirect:/admin_notice_list.do");
		}
		return model;
	}

	/**
	 * admin_notice_delete - 공지사항 게시글 삭제
	 */
	@RequestMapping(value = "/admin_delete.do", method = RequestMethod.GET)
	public ModelAndView admin_delete(String nid) {
		ModelAndView model = new ModelAndView();
		model.addObject("nid", nid);
		model.setViewName("/admin/notice/admin_notice_delete");

		return model;
	}

	/**
	 * admin_notice_update_proc.do - 공지사항 게시글 수정 처리
	 */
	@RequestMapping(value = "/admin_notice_update_proc.do", method = RequestMethod.POST)
	public ModelAndView admin_notice_update_proc(NoticeVo noticeVo) {
		ModelAndView model = new ModelAndView();
		int result = noticeService.getUpdate(noticeVo);
		if (result == 1) {
				model.setViewName("redirect:/admin_notice_list.do"); 
		}else {
			
		}
		return model;
	}

	/**
	 * admin_notice_update.do - 공지사항 수정
	 */
	@RequestMapping(value = "/admin_notice_update.do", method = RequestMethod.GET)
	public ModelAndView admin_notice_update(String nid) {
		ModelAndView model = new ModelAndView();
		NoticeVo noticeVo = noticeService.getSelect(nid) ;
		
		model.addObject("nid", nid);
		model.setViewName("admin/notice/admin_notice_update");

		return model;
	}

	/**
	 * admin_notice_write_proc.do - 공지사항 글쓰기 처리
	 */
	@RequestMapping(value = "/admin_notice_write_proc.do", method = RequestMethod.POST)
	public ModelAndView admin_notice_write_proc(NoticeVo noticeVo) {
		ModelAndView model = new ModelAndView();
		int result = noticeService.getInsert(noticeVo);
		if (result == 1) {
			model.setViewName("redirect:/admin_notice_list.do"); 
		} else {
			// 에러 페이지 호출
		}
		return model;
	}
	/**
	 * admin_notice_write.do - 공지사항 글쓰기
	 */
	@RequestMapping(value = "/admin_notice_write.do", method = RequestMethod.GET)
	public String board_write() {
		return "/admin/notice/admin_notice_write";
	}

	/**
	 * admin_notice_content.do - 공지사항 게시글 상세 보기
	 */
	@RequestMapping(value = "/admin_notice_content.do", method = RequestMethod.GET)
	public ModelAndView admin_content_list(String nid) {
		ModelAndView model = new ModelAndView();
		NoticeVo noticeVo = noticeService.getSelect(nid);
		if (noticeVo != null) {
			noticeService.getUpdateHits(nid);
		}

		model.addObject("noticeVo", noticeVo);
		model.setViewName("admin/notice/admin_notice_content");

		return model;
	}

	/**
	 * admin_notice_list.do - 관리자 공지사항 메인페이지
	 *//*
		 * @RequestMapping(value = "/admin_notice_list.do", method = RequestMethod.GET)
		 * public ModelAndView admin_notice_list() { ModelAndView model = new
		 * ModelAndView(); NoticeDao noticeDao = new NoticeDao(); ArrayList<NoticeVo>
		 * list = noticeDao.select();
		 * 
		 * model.addObject("list", list);
		 * model.setViewName("/admin/notice/admin_notice_list");
		 * 
		 * return model; }
		 */

	/**
	 * admin_index.do - 관리자 메인페이지
	 */
	@RequestMapping(value = "/admin_index.do", method = RequestMethod.GET)
	public String admin_index() {
		return "/admin/admin_index";
	}
}
