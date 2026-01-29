package org.springblade.modules.sp.em;

/**
 * wangc 0624
 * 审批auditCode 枚举
 */
public enum TypeEM {

	// 枚举实例
	TEMPLATE_STATUS_0("wbs",1),
	TEMPLATE_STATUS_1("构件",2);



	// 成员变量
	private String name;
	private int code;

	// 构造方法
	private TypeEM(String name, int code) {
		this.name = name;
		this.code = code;
	}

	// 获取Name
	public static String getName(int code) {
		for (TypeEM c : TypeEM.values()) {
			if (c.getCode()==code) {
				return c.name;
			}
		}
		return null;
	}

	// 获取Code
	public static int getCode(String name) {
		for (TypeEM c : TypeEM.values()) {
			if (c.getName().equals(name)) {
				return c.code;
			}
		}
		return 3;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setIndex(int code) {
		this.code = code;
	}

	// 覆盖方法
	@Override
	public String toString() {
		return this.code + "_" + this.name;
	}


}
