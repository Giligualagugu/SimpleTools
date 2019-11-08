package com.company.dataX.jsonInfo;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 12:56
 */
@Data
public class DataConnection {

	private String jdbcUrl;
	private List<String> table;

}
