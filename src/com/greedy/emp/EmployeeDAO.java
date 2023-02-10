package com.greedy.emp;

import static com.greedy.common.JDBCTemplate.close;
import static com.greedy.common.JDBCTemplate.getConnection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.greedy.model.dto.EmployeeDTO;

public class EmployeeDAO {

	/* 구현 조건
	 * 1. Connection 생성은 JDBC 템플릿 사용
	 * 2. query문은 xml 파일로 분리
	 * 3. 쿼리문에 값을 전달해야 하는 경우는 PreparedStatement, 아닌 경우는 Statement 사용
	 * 4. 한 행 정보는 DTO에 담아 출력, 여러 행 정보는 ArrayList에 담아 출력
	 * */
	
	public void findOneEmpByEmpId(String inputEmpId) {

		/* 사번으로 직원정보 조회 후 Employee에 담아 출력 */
		
		Connection con = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String query = "SELECT EMP_ID, EMP_NAME FROM EMPLOYEE WHERE EMP_ID = ?";	//? = 위치홀더
				
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, inputEmpId);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				System.out.println(rset.getString("EMP_ID") + ", " + rset.getString("EMP_NAME"));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(con);
			close(pstmt);
			close(rset);
		
		
	}
		
	}

	public void findAllEmp() {

		Connection con = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			pstmt = con.prepareStatement("SELECT EMP_ID, EMP_NAME FROM EMPLOYEE");
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				System.out.println(rset.getString("EMP_ID") + ", " + rset.getString("EMP_NAME"));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rset);
			close(pstmt);
			close(con);
		}
		
	}

	public void findEmpByGender(String inputEmpNo) {

		Connection con = getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		List<EmployeeDTO> empList = null;
		
		Properties prop = new Properties();
		
		try {
			prop.loadFromXML(new FileInputStream("src/com/greedy/emp/employee-query.xml"));
			
			String query = prop.getProperty("selectEmpByFamilyName");
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, inputEmpNo);
			
			rset = pstmt.executeQuery();
			
			empList = new ArrayList<>();
			
			while(rset.next()) {
				
				EmployeeDTO row = new EmployeeDTO();
				
				row.setEmpId(rset.getString("EMP_ID"));
				row.setEmpName(rset.getString("EMP_NAME"));
				row.setEmpNo(rset.getString("EMP_NO"));
				row.setEmail(rset.getString("PHONE"));
				row.setDeptCode(rset.getString("DEPT_CODE"));
				row.setJobCode(rset.getString("JOB_CODE"));
				row.setSalary(rset.getInt("SALARY"));
				row.setBonus(rset.getDouble("Bonus"));
				row.setManagerId(rset.getString("MANAGER_ID"));
				row.setHireDate(rset.getDate("HIRE_DATE"));
				
				empList.add(row);
				
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(con);
			close(pstmt);
			close(rset);
		}
		
		for(EmployeeDTO emp : empList) {
			System.out.println(emp);
		}
		
	}
	
}
