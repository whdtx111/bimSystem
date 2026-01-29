package org.springblade.modules.sp.utils;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * wangc 20200302 tree工具类
 */
public class TreeUtils {
	/**
	 * mybatis_plus 雪花id 生成类
	 */
	private static IdentifierGenerator snowIdGenerator = new DefaultIdentifierGenerator();

	/***
	 *
	 * @param wbsCode GL03.001.001.001.001 code编码
	 * @param index 4 大纲级别
	 * @return
	 */
	public static String getParentCode(String wbsCode, int index) {
		// 切割
		String[] temp = wbsCode.split("\\-");
		String parentCode = "";
		for (int i = 0; i < temp.length; i++) {
			// 拼接 父级code  index-1
			if (i<=index-1){
				if (i!=index-1){
					parentCode += temp[i]+"-";
				}else {
					parentCode += temp[i];
				}

			}
		}

		return parentCode;
	}

	/***
	 *
	 * @param codeParentIdMap
	 *  for (SeemUnit read  : list_read) {
	 *  // 插入 mybatis_plus 生成的雪花 treeID
	 *  read.setTreeId(snowIdGenerator.nextId(SeemUnit.class).longValue());
	 *  read.setUploadSign(uploadSign);
	 *  // 存放 code 和 treeId
	 *  map.put(read.getCode(),read.getTreeId());
	 *  }
	 * @param parentCode
	 * @return 返回父级id
	 */
	public static Long getParentId(Map<String, Long> codeParentIdMap, String parentCode) {
		Long ProjectId  = codeParentIdMap.get(parentCode);
		// 无父级code 说明是顶级
		if(ProjectId == null || ProjectId.longValue() == 0){
			ProjectId = new Long(0);
		}
		return ProjectId;
	}

	/***
	 * demo
	 */
	private static void demo(){
		TreeEntity t = new TreeEntity();
		t.setWbsCode("GL03");
		t.setTypeLevel("0");

		t.setWbsCode("GL03-001");
		t.setTypeLevel("1");

		TreeEntity t2 = new TreeEntity();
		t2.setWbsCode("GL03-001-001");
		t2.setTypeLevel("2");

		TreeEntity t3 = new TreeEntity();
		t3.setWbsCode("GL03-001-001-001");
		t3.setTypeLevel("3");

		TreeEntity t4 = new TreeEntity();
		t4.setWbsCode("GL03-001-001-001-001");
		t4.setTypeLevel("4");

		Map<String,Long> codeParentIdMap = new HashMap<>();
		List<TreeEntity> list = new ArrayList<>();
		list.add(t);
		list.add(t2);
		list.add(t3);
		list.add(t4);

		for (TreeEntity e : list) {
			e.setId(snowIdGenerator.nextId(TreeEntity.class).longValue());
			codeParentIdMap.put(e.getWbsCode(),e.getId());
		}

		for (TreeEntity new_e : list) {
			String ParentCode = TreeUtils.getParentCode(new_e.getWbsCode(),Integer.parseInt(new_e.getTypeLevel()));
			Long parentId = TreeUtils.getParentId(codeParentIdMap,ParentCode) ;
			System.out.println("本层级code,"+new_e.getWbsCode()+"本级id,"+new_e.getId());
			System.out.println("父级code,"+ParentCode+"---父级id,"+parentId);
			System.out.println("--------");
		}
	}

}
