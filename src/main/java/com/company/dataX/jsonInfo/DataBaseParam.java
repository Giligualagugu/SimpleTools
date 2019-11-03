package com.company.dataX.jsonInfo;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 12:54
 */
@Data
public class DataBaseParam {

	private String username;
	private String password;
	private List<String> column;

	private List<DataConnection> connection;

}
