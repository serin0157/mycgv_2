package com.mycgv_jsp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycgv_jsp.vo.NoticeVo;

@Repository
public class NoticeDao{
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	/**
	 * startCount, endCount - 페이징 처리
	 * */
	public ArrayList<NoticeVo> select(int startCount , int endCount){
		Map<String, Integer> param = new HashMap<String, Integer >();
		param.put("start", startCount);
		param.put("end", endCount);
		
		List<NoticeVo> list = sqlSession.selectList("mapper.notice.list", param);
		
		return (ArrayList<NoticeVo>)list;
	}
	
	/**
	 * updateHits - 게시글 조회수 증가
	 * */
	public void updateHits(String nid){
		sqlSession.update("mapper.notice.updateHits",nid);
	}	
	/**
	 * delete - 게시글 삭제
	 * */
	public int delete(String nid) {
		return sqlSession.delete("mapper.notice.delete", nid);

	}
	/**
	 * update - 게시글 수정
	 * */
	public int update(NoticeVo noticeVo) {
		return sqlSession.update("mapper.notice.update", noticeVo);
	}
		
	/**
	 * select - 게시글 상세보기
	 * */
	public NoticeVo select(String nid){
		return sqlSession.selectOne("mapper.notice.content", nid);
	}
	/**
	 * select - 게시글 전체 리스트
	 * */
	public ArrayList<NoticeVo> select(){
		List<NoticeVo> list = sqlSession.selectList("mapper.notice.list2");
		return (ArrayList<NoticeVo>) list;
//		ArrayList<NoticeVo> list = new ArrayList<NoticeVo>();
//		String sql = "SELECT ROWNUM RNO, NID, NTITLE, NHITS,TO_CHAR(NDATE,'YYYY-MM-DD') NDATE FROM"
//				+ " (SELECT NID, NTITLE, NHITS, NDATE FROM MYCGV_NOTICE ORDER BY NID DESC)";
//		getPreparedStatement(sql);
//		
//		try {
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {
//				NoticeVo noticeVo = new NoticeVo(); 
//				noticeVo.setRno(rs.getInt(1));
//				noticeVo.setNid(rs.getString(2));
//				noticeVo.setNtitle(rs.getString(3));
//				noticeVo.setNhits(rs.getInt(4));
//				noticeVo.setNdate(rs.getString(5));
//				
//				list.add(noticeVo);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
	}
	
	/**
	 * insert - 게시글 등록
	 * */
	public int insert(NoticeVo noticeVo) {
		return sqlSession.insert("mapper.notice.insert", noticeVo);
	}
 
	
}
