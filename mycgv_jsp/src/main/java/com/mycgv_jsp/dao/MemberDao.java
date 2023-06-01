package com.mycgv_jsp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycgv_jsp.vo.MemberVo;

@Repository //Dao���� �������� ���� 
public class MemberDao{
	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	/**
	 * startCount, endCount - ����¡ ó��
	 * */
	public ArrayList<MemberVo> select(int startCount , int endCount){
		Map<String , Integer > param = new HashMap<String , Integer>();
		param.put("start", startCount);
		param.put("end", endCount);
		
		List<MemberVo> list =  sqlSession.selectList("mapper.member.list",param );
		
		return (ArrayList<MemberVo>)list;

	}
	
//	public ArrayList<MemberVo> select(){
//		return ArrayList<MemberVo> list = sqlSession.selectList("mapper.member.list2");
//		ArrayList<MemberVo> list = new ArrayList<MemberVo>();
//		
//		String sql = " SELECT ROWNUM RNO, ID , NAME, TO_CHAR(MDATE, 'YYYY-MM-DD') MDATE, GRADE" + 
//					"  FROM (SELECT ID , NAME, MDATE, GRADE FROM MYCGV_MEMBER ORDER BY MDATE DESC)";
//		getPreparedStatement(sql);
//		
//		try {
//			 rs = pstmt.executeQuery();
//			 
//			 while(rs.next()) {
//				 MemberVo memberVo = new MemberVo();
//				 memberVo.setRno(rs.getInt(1));
//				 memberVo.setId(rs.getString(2));
//				 memberVo.setName(rs.getString(3));
//				 memberVo.setMdate(rs.getString(4));
//				 memberVo.setGrade(rs.getString(5));
//				 
//				 list.add(memberVo); //�ܼ�â ����X ȭ�� ��� X
//			 }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return list;
//	}
	
	/**
	 * loginCheck - ���̵� �ߺ� üũ
	 */
	public int loginCheck(MemberVo memberVo) {
		return sqlSession.selectOne("mapper.member.login", memberVo);
	}
	
	/**
	 * idCheck - ���̵� �ߺ� üũ
	 */
	public int idCheck(String id) {
		return sqlSession.selectOne("mapper.member.idcheck", id);
	}
	
	/**
	 * insert --> ȸ������
	 */
	public int insert(MemberVo memberVo) {
		return sqlSession.insert("mapper.member.join", memberVo);
	}

}
