package kr.co.noir.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@Mapper
public class MyBatisHandler {
	private static MyBatisHandler mbh;	
	private static SqlSessionFactory ssf;
	
	private MyBatisHandler() {		
		org.apache.ibatis.logging.LogFactory.useLog4J2Logging();
	}		
	public static MyBatisHandler getInstance() {		
		if(mbh==null) {
			mbh = new MyBatisHandler();
		}		
		return mbh;
	}
	
	private static SqlSessionFactory getSessionFactory() throws IOException {
		if(ssf == null) {
			//1. 설정용 XML 연결
			Reader reader = Resources.getResourceAsReader("kr/co/noir/dao/mybatis-config.xml");
			//2. SqlSessionFactorytBuild 생성
			SqlSessionFactoryBuilder ssfb = new SqlSessionFactoryBuilder();
			//3. SqlSessionFactory를 building 한다.
			ssf = ssfb.build(reader);
			
			if(reader != null) {
				reader.close();
			}
		}
		
		return ssf;
	}
	
	 /**
	 * MyBatis Handler 얻기
	 * @param autoCommitFlag true - autocommit, false - non autocommit
	 * @return
	 * @throws IOException
	 */
	public SqlSession getMyBatisHandler(boolean autoCommitFlag) {
		 SqlSession ss= null;
		 
		 try {
			ss=getSessionFactory().openSession(autoCommitFlag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 return ss;
	 }
}
