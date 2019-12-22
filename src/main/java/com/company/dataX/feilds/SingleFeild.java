package com.company.dataX.feilds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;

/**
 * @AUTHOR xukele
 * @SINCE 2019/12/22 21:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleFeild {

	private String belongTable;
	private String feildname;
	private JDBCType jdbcType;

}
