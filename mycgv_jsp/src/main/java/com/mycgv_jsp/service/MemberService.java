package com.mycgv_jsp.service;

import java.util.ArrayList;

import com.mycgv_jsp.vo.MemberVo;

public interface MemberService {
	public int getLoginResult(MemberVo memberVo);
	public String getIdCheckResult(String id);
	public int getJoinResult(MemberVo memberVo);
	public ArrayList<MemberVo> getList(int startCount, int endCount);
	
	
}
