package com.mycgv_jsp.service;

import java.util.ArrayList;

import com.mycgv_jsp.vo.BoardVo;

public interface BoardService {
	
	public BoardVo getBoardContent(String bid);
	public int getBoardWrite(BoardVo boardVo);
	public int getBoardUpdate(BoardVo boardVo);
	public int getBoardDelete(String bid);
	void getUpdateHits(String bid);
	ArrayList<BoardVo> getSelet(int startCount, int endCount);
	
}
