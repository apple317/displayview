package com.apple.asset;

import java.util.HashMap;
import java.util.List;

/**
 * 柱状图形实体类
 * 
 * @author 胡少平
 * 
 */
public class ChartEntity {
	// 每个柱形纵向数据显示集：String = 显示内容
	public List<String> hList;
	// 每个柱形横向数据显示集：String = 显示内容,true|false
	public List<String> wList;
	//横向显示值
	public HashMap<String,Integer> map;
	//柱形比例度
	public int scale;
	// 每个柱形横向柱状显示宽度
	public int row_weight = 0;
	// 每个柱形横向柱状间距
	public int padding_weight = 0;
	//横向起始点x坐标
	public int startX = 65;
	//每个柱形纵向显示宽度
	public int row_height=0;
	//每个柱形纵向
	public int padding_height=40;
	public ChartEntity() {
		
		// TODO Auto-generated constructor stub
	}
}
