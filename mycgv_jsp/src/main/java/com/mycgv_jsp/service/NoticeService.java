package com.mycgv_jsp.service;

import java.util.ArrayList;

import com.mycgv_jsp.vo.NoticeVo;

public interface NoticeService {
	
	public int getInsert(NoticeVo noticeVo);
	ArrayList<NoticeVo > getSelect(int startCount, int endCount);
	public NoticeVo getSelect(String nid);
	public int getUpdate(NoticeVo noticeVo);
	public int getDelete(String nid);
	void getUpdateHits(String nid);
	
}
