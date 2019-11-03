package com.company.dataX.jsonInfo;

import lombok.Data;

import java.util.List;

/**
 * @AUTHOR xukele
 * @SINCE 2019/11/1 12:47
 */
@Data
public class Job {

	private Setting setting;

	private List<Operater> content;

}
