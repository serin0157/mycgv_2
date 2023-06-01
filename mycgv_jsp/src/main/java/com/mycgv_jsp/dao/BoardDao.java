package com.mycgv_jsp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycgv_jsp.vo.BoardVo;

@Repository
public class BoardDao{
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	/**
	 * startCount, endCount - 페이징 처리
	 * */
	public ArrayList<BoardVo> select(int startCount , int endCount){
		Map<String, Integer> param = new HashMap<String, Integer>();
		param.put("start", startCount);
		param.put("end", endCount);
		
		List<BoardVo> list = sqlSession.selectList("mapper.board.list",param);
		
		return (ArrayList<BoardVo>)list;
	}
	
	/**
	 * updateHits - 게시글 조회수 증가
	 * */
	public void updateHits(String bid){
		sqlSession.update("mapper.board.updateHits", bid);
	}	
	/**
	 * delete - 게시글 삭제
	 * */
	public int delete(String bid) {
		return sqlSession.delete("mapper.board.delete", bid);
	}
	/**
	 * update - 게시글 수정
	 * */
	public int update(BoardVo boardVo) {
		return sqlSession.update("mapper.board.update",boardVo);
	}
		
	/**
	 * select - 게시글 상세보기
	 * */
	public BoardVo select(String bid){
		return sqlSession.selectOne("mapper.board.content", bid);
	}
	/**
	 * select - 게시글 전체 리스트
	 * */
	public ArrayList<BoardVo> select(){
		List<BoardVo> list = sqlSession.selectList("mapper.board.list2");
		return (ArrayList<BoardVo>)list;
//		ArrayList<BoardVo> list = new ArrayList<BoardVo>();
//		String sql = "	SELECT ROWNUM RNO, BID, BTITLE, BCONTENT, BHITS, ID, to_char(BDATE, 'yyyy-mm-dd') BDATE" + 
//					 "  FROM(SELECT BID, BTITLE, BCONTENT,BHITS, ID, BDATE FROM MYCGV_BOARD" + 
//					 "  ORDER BY BDATE DESC)";
//		getPreparedStatement(sql);
//		
//		try {
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {
//				BoardVo boardVo = new BoardVo();
//				boardVo.setRno(rs.getInt(1));
//				boardVo.setBid(rs.getString(2));
//				boardVo.setBtitle(rs.getString(3));
//				boardVo.setBcontent(rs.getString(4));
//				boardVo.setBhits(rs.getInt(5));
//				boardVo.setId(rs.getString(6));
//				boardVo.setBdate(rs.getString(7));
//				
//				list.add(boardVo);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return list;
	}
	
	/**
	 * insert - 게시글 등록
	 * */
	public int insert(BoardVo boardVo) {
		return sqlSession.insert("mapper.board.insert",boardVo);
		
	}
//		int result = 0;
//		
//		String sql = " insert into mycgv_board(bid, btitle, bcontent, bhits, id, bdate, bfile, bsfile) "
//					+" values('b_'||LTRIM(to_char(sequ_mycgv_board.nextval,'0000')),?,?,0,?,sysdate,?,?)";
//		getPreparedStatement(sql);
//		
//		try {
//			pstmt.setString(1, boardVo.getBtitle());
//			pstmt.setString(2, boardVo.getBcontent());
//			pstmt.setString(3, boardVo.getId());
//			pstmt.setString(4, boardVo.getBfile());
//			pstmt.setString(5, boardVo.getBsfile());
//			
//			result = pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return result;
//	} 
	
}
